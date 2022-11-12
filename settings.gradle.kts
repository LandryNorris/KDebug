pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KDebug"
include(":androidApp")
include(":desktopApp")
include(":shared")
include(":memory-reader")