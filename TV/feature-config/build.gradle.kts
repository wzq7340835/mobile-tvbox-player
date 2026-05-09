plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.fongmi.android.tv.config"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core-rust"))
    implementation(project(":engine-quickjs"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra.get("kotlinxCoroutinesVersion")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${rootProject.extra.get("kotlinxSerializationVersion")}")
    implementation("com.google.code.gson:gson:${rootProject.extra.get("gsonVersion")}")
    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra.get("okhttpVersion")}")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.startup:startup-runtime:1.2.0")
    implementation("androidx.room:room-runtime:${rootProject.extra.get("roomVersion")}")
}
