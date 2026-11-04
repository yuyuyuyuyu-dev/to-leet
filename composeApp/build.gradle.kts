import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.aboutLibraries)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composePwa)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

ktlint {
    version = libs.versions.ktlintCli.get()
    filter {
        val buildPath =
            layout.buildDirectory
                .get()
                .asFile.path + File.separator
        exclude { it.file.path.startsWith(buildPath) }
    }
}

detekt {
    source.setFrom("src")
    buildUponDefaultConfig = true
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
}

kotlin {
    compilerOptions {
        allWarningsAsErrors.set(true)
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.aboutlibraries.compose.m3)
            implementation(libs.symbols.materialVectorsOutlined)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.myComposables)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.compose.uiTest)
        }
    }
}

aboutLibraries {
    export {
        outputFile = file("src/commonMain/composeResources/files/aboutlibraries.json")
    }
}

compose.resources {
    customDirectory(
        sourceSetName = "commonMain",
        directoryProvider =
            tasks
                .named("exportLibraryDefinitions")
                .map { layout.projectDirectory.dir("src/commonMain/composeResources") },
    )
}

val composeVersion = libs.versions.composeMultiplatform.get()
val nextComposeLine = composeVersion.split(".").let { "${it[0]}.${it[1].toInt() + 1}" }

dependencies {
    constraints {
        listOf(
            "org.jetbrains.compose.runtime:runtime",
            "org.jetbrains.compose.ui:ui",
            "org.jetbrains.compose.foundation:foundation",
            "org.jetbrains.compose.animation:animation",
            "org.jetbrains.compose.material:material-ripple",
            "org.jetbrains.compose.components:components-animatedimage",
            "org.jetbrains.compose.components:components-resources",
            "org.jetbrains.compose.components:components-splitpane",
            "org.jetbrains.compose.components:components-ui-tooling-preview",
        ).forEach { module ->
            add("commonMainImplementation", module) {
                version { reject("($composeVersion,)") }
            }
        }
        add("commonMainImplementation", "org.jetbrains.compose.material3:material3") {
            version { reject("[$nextComposeLine,)") }
        }
    }
}
