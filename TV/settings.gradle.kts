pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        flatDir { dirs "$rootDir/app/libs" }
        maven { url = "https://jitpack.io" }
    }
}

include ':app'
include ':core-rust'
include ':engine-quickjs'
include ':feature-dlna'
include ':feature-config'
include ':feature-remote'

rootProject.name = "TV"
