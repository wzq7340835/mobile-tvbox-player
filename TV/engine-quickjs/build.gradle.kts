plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.mozilla.rust-android-gradle.rust-android")
}

android {
    namespace = "com.fongmi.android.tv.engine"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

cargo {
    module = "src/main/rust/quickjs_engine"
    libname = "quickjs_engine"
    targets = listOf("arm64", "arm")
    profile = "release"
}

dependencies {
    implementation(project(":core-rust"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra.get("kotlinxCoroutinesVersion")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${rootProject.extra.get("kotlinxSerializationVersion")}")
    implementation("com.google.code.gson:gson:${rootProject.extra.get("gsonVersion")}")
    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra.get("okhttpVersion")}")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.startup:startup-runtime:1.2.0")
}

tasks.matching { it.name.matches(Regex("merge.*JniLibFolders")) }.configureEach {
    dependsOn("cargoBuild")
}
