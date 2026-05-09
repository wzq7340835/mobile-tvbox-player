plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.fongmi.android.tv.remote"
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra.get("kotlinxCoroutinesVersion")}")
    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra.get("okhttpVersion")}")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("androidx.annotation:annotation:1.9.1")
}
