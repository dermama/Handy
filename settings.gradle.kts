pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Handy"

include(":app")
include(":termux-module:terminal-emulator")
include(":termux-module:terminal-view")
include(":termux-module:termux-shared")
include(":winlator-core")
