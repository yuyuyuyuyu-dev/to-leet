import json
import os
import re
import sys
import urllib.request
from datetime import datetime, timedelta, timezone

MAVEN = "https://repo1.maven.org/maven2"
CHANGELOG = "https://raw.githubusercontent.com/JetBrains/compose-multiplatform/master/CHANGELOG.md"
CATALOG = "gradle/libs.versions.toml"
BODY = "compose-update.md"
COMPOSE_KEY = "composeMultiplatform"
MATERIAL3_KEY = "material3"
ADAPTIVE_KEY = "compose-multiplatform-adaptive"
COMPOSE_PLUGIN_GROUP = "org.jetbrains.compose"
MATERIAL3_GROUP = "org.jetbrains.compose.material3"
ADAPTIVE_GROUP = "org.jetbrains.compose.material3.adaptive"
CHANGELOG_GRACE = timedelta(days=3)
VERSION = re.compile(r"(\d+)\.(\d+)\.(\d+)(?:-(alpha|beta|rc)(\d+))?")
STAGES = {"alpha": 0, "beta": 1, "rc": 2, None: 3}
ENTRY = re.compile(r'^([\w-]+)\s*=\s*\{\s*(module|id)\s*=\s*"([^"]+)"\s*,\s*version\.ref\s*=\s*"([^"]+)"\s*\}', re.M)


class UpdateError(Exception):
    pass


def version_key(version):
    match = VERSION.fullmatch(version or "")
    if not match:
        return None
    major, minor, patch, stage, number = match.groups()
    return int(major), int(minor), int(patch), STAGES[stage], int(number or 0)


def line_of(version):
    return version.split(".")[:2]


def fetch(url):
    request = urllib.request.Request(url, headers={"User-Agent": "compose-update"})
    with urllib.request.urlopen(request, timeout=60) as response:
        return response.read().decode()


def artifact_url(group, artifact):
    return f"{MAVEN}/{group.replace('.', '/')}/{artifact}"


def published_versions(group, artifact):
    return re.findall(r"<version>([^<]+)</version>", fetch(f"{artifact_url(group, artifact)}/maven-metadata.xml"))


def latest_stable_compose():
    versions = published_versions(COMPOSE_PLUGIN_GROUP, "compose-gradle-plugin")
    return max((v for v in versions if re.fullmatch(r"\d+\.\d+\.\d+", v)), key=version_key)


def compose_requirement(material3):
    module = json.loads(fetch(f"{artifact_url(MATERIAL3_GROUP, 'material3')}/{material3}/material3-{material3}.module"))
    required = [
        dependency.get("version", {}).get("requires") or dependency.get("version", {}).get("strictly")
        for variant in module.get("variants", [])
        for dependency in variant.get("dependencies", [])
        if dependency.get("group", "").startswith(f"{COMPOSE_PLUGIN_GROUP}.")
        and not dependency.get("group", "").startswith(MATERIAL3_GROUP)
    ]
    keys = [version_key(version) for version in required]
    if not keys or None in keys:
        return None
    return max(keys)


def select_material3(compose):
    candidates = sorted(
        (v for v in published_versions(MATERIAL3_GROUP, "material3") if version_key(v) and line_of(v) == line_of(compose)),
        key=version_key,
        reverse=True,
    )
    for candidate in candidates:
        requirement = compose_requirement(candidate)
        if requirement and requirement <= version_key(compose):
            return candidate
    raise UpdateError(f"No material3 release on the {'.'.join(line_of(compose))} line works with Compose Multiplatform {compose}.")


def released_at(compose):
    listing = fetch(f"{artifact_url(COMPOSE_PLUGIN_GROUP, 'compose-gradle-plugin')}/")
    match = re.search(rf'href="{re.escape(compose)}/"[^>]*>[^<]*</a>\s+(\d{{4}}-\d{{2}}-\d{{2}} \d{{2}}:\d{{2}})', listing)
    if not match:
        return None
    return datetime.strptime(match.group(1), "%Y-%m-%d %H:%M").replace(tzinfo=timezone.utc)


def overdue_notice(compose, reason):
    released = released_at(compose)
    if released is None or datetime.now(timezone.utc) - released < CHANGELOG_GRACE:
        return ""
    return f"{reason} Compose Multiplatform {compose} was released on {released:%Y-%m-%d}."


def select_adaptive(compose, artifact):
    section = re.search(rf"^# {re.escape(compose)} \(.*?(?=^# |\Z)", fetch(CHANGELOG), re.M | re.S)
    if not section:
        return None, overdue_notice(compose, f"The Compose Multiplatform CHANGELOG still has no section for {compose}.")
    row = re.search(rf"^\|\s*Material3 Adaptive\s*\|\s*`{re.escape(ADAPTIVE_GROUP)}:adaptive\*:([^`]+)`", section.group(0), re.M)
    if not row or not version_key(row.group(1)):
        return None, f"The Compose Multiplatform CHANGELOG section for {compose} has no Material3 Adaptive row that could be read."
    adaptive = row.group(1)
    if adaptive not in published_versions(ADAPTIVE_GROUP, artifact):
        return None, overdue_notice(compose, f"Material3 Adaptive {adaptive} is not on Maven Central yet.")
    return adaptive, ""


def versions_table(text):
    match = re.search(r"^\[versions\]\n(.*?)(?=^\[|\Z)", text, re.M | re.S)
    if not match:
        raise UpdateError("The version catalog has no [versions] table.")
    return match


def read_versions(text):
    return dict(re.findall(r'^([\w-]+)\s*=\s*"([^"]+)"', versions_table(text).group(1), re.M))


def with_versions(text, updates):
    table = versions_table(text)
    body = table.group(1)
    for key, value in updates.items():
        body = re.sub(rf'^({re.escape(key)}\s*=\s*)"[^"]*"', lambda match: f'{match.group(1)}"{value}"', body, count=1, flags=re.M)
    return text[: table.start(1)] + body + text[table.end(1) :]


def expected_key(group):
    if group == MATERIAL3_GROUP:
        return MATERIAL3_KEY
    if group == ADAPTIVE_GROUP:
        return ADAPTIVE_KEY
    return COMPOSE_KEY


def catalog_problems(text):
    managed = {COMPOSE_KEY, MATERIAL3_KEY, ADAPTIVE_KEY}
    problems = [f"Cannot read {line.strip()}" for line in text.splitlines() if f'"{COMPOSE_PLUGIN_GROUP}' in line and not ENTRY.match(line)]
    for name, _, coordinate, ref in ENTRY.findall(text):
        group = coordinate.split(":")[0]
        compose = group == COMPOSE_PLUGIN_GROUP or group.startswith(f"{COMPOSE_PLUGIN_GROUP}.")
        if compose and ref != expected_key(group):
            problems.append(f"{name} uses {ref} instead of {expected_key(group)}.")
        if not compose and ref in managed:
            problems.append(f"{name} uses {ref}, which only Compose Multiplatform libraries may use.")
    return problems


def adaptive_artifact(text):
    for _, kind, coordinate, ref in ENTRY.findall(text):
        if kind == "module" and ref == ADAPTIVE_KEY:
            return coordinate.split(":")[1]
    return None


def pull_request_body(versions, adaptive_paired):
    rows = "\n".join(f"| `{key}` | `{versions[key]}` |" for key in (COMPOSE_KEY, MATERIAL3_KEY, ADAPTIVE_KEY) if key in versions)
    notes = [
        "Keeps Compose Multiplatform on its latest stable release and moves the libraries that have to match it.",
        f"| Version key | Version |\n| --- | --- |\n{rows}",
        f"`{MATERIAL3_KEY}` is the newest release on the same line as Compose Multiplatform that does not require newer Compose libraries.",
    ]
    if ADAPTIVE_KEY in versions and adaptive_paired:
        notes.append(f"`{ADAPTIVE_KEY}` is the version the Compose Multiplatform CHANGELOG pairs with this release.")
    elif ADAPTIVE_KEY in versions:
        notes.append(f"`{ADAPTIVE_KEY}` was left unchanged because the CHANGELOG does not pair a version with this release yet.")
    notes.append("`yarn.lock` was regenerated with `./gradlew kotlinWasmUpgradeYarnLock`.")
    return "\n\n".join(notes) + "\n"


def write_outputs(values):
    lines = "".join(f"{key}={value}\n" for key, value in values.items())
    if "GITHUB_OUTPUT" in os.environ:
        with open(os.environ["GITHUB_OUTPUT"], "a") as output:
            output.write(lines)
    print(lines, end="")


def main():
    with open(CATALOG) as catalog:
        text = catalog.read()
    problems = catalog_problems(text)
    if problems:
        raise UpdateError(" ".join(problems))
    versions = read_versions(text)
    if COMPOSE_KEY not in versions or MATERIAL3_KEY not in versions:
        raise UpdateError(f"The version catalog needs both {COMPOSE_KEY} and {MATERIAL3_KEY}.")
    compose = latest_stable_compose()
    if (version_key(versions[COMPOSE_KEY]) or ()) > version_key(compose):
        compose = versions[COMPOSE_KEY]
    updates = {COMPOSE_KEY: compose, MATERIAL3_KEY: select_material3(compose)}
    adaptive, notice = None, ""
    artifact = adaptive_artifact(text)
    if artifact:
        adaptive, notice = select_adaptive(compose, artifact)
        if adaptive:
            updates[ADAPTIVE_KEY] = adaptive
    updated = with_versions(text, updates)
    changed = updated != text
    if changed:
        with open(CATALOG, "w") as catalog:
            catalog.write(updated)
        with open(BODY, "w") as body:
            body.write(pull_request_body(read_versions(updated), adaptive is not None))
    write_outputs({"changed": str(changed).lower(), "notice": notice})


if __name__ == "__main__":
    try:
        main()
    except UpdateError as error:
        print(f"::error::{error}")
        sys.exit(1)
