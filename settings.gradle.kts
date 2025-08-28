pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.8"
}

stonecutter {
    create(rootProject) {
        versions("1.21.5", "1.21.7", "1.21.8")

        vcsVersion = "1.21.8"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}

rootProject.name = "Apec"
