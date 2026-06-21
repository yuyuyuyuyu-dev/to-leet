plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.spotless)
}

spotless {
    // Root-level Gradle scripts (build.gradle.kts / settings.gradle.kts).
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
    // Version catalog formatting via Prettier (prettier-plugin-toml).
    format("toml") {
        target("gradle/libs.versions.toml")
        prettier(
            mapOf(
                "prettier" to "3.8.3",
                "prettier-plugin-toml" to "2.0.6",
            ),
        ).config(
            mapOf(
                "parser" to "toml",
                "plugins" to listOf("prettier-plugin-toml"),
            ),
        )
    }
}
