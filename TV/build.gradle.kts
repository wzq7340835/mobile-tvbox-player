plugins {
    id("com.android.application") version "9.1.1" apply false
    id("com.android.library") version "9.1.1" apply false
    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.21" apply false
    id("org.mozilla.rust-android-gradle.rust-android") version "0.9.6" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

project(":app").tasks.matching { it.name.matches(Regex("assemble.+Release")) }.configureEach {
    doLast {
        copy {
            from(fileTree("$rootDir/app/build/outputs/apk") { include("**/release/*.apk") })
            into("$rootDir/Release/apk")
            eachFile { it.path = it.name }
            includeEmptyDirs = false
        }
    }
}

extra.apply {
    set("gsonVersion", "2.13.2")
    set("glideVersion", "5.0.5")
    set("okhttpVersion", "5.3.2")
    set("media3Version", "1.10.0")
    set("composeBomVersion", "2025.05.00")
    set("kotlinxCoroutinesVersion", "1.10.2")
    set("kotlinxSerializationVersion", "1.8.1")
    set("roomVersion", "2.8.4")
    set("lifecycleVersion", "2.10.0")
    set("hiltVersion", "2.56.2")
    set("hiltNavigationComposeVersion", "1.2.0")
}
