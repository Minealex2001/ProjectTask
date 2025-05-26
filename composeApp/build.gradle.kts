import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.lombok") version "2.1.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3) // This pulls in org.jetbrains.compose.material3:material3
                implementation(compose.components.resources)
                implementation(compose.desktop.currentOs) // This brings in necessary UI components
                implementation(compose.uiTooling)
                implementation("io.github.oshai:kotlin-logging-jvm:5.1.0") // Example version
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Example version
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "org.queststudios.projecttask.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Dmg)
            packageName = "Project Task"
            packageVersion = "1.0.0"
            windows {
                menuGroup = "ProjectTask"
                dirChooser = true
                shortcut = true
                iconFile.set(project.file("src/desktopMain/resources/icon.png"))
            }
            macOS {
                iconFile.set(project.file("src/desktopMain/resources/icon.png"))
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/icon.png"))
            }
        }
    }
}
