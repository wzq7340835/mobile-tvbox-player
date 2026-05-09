plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.mozilla.rust-android-gradle.rust-android")
}

android {
    namespace = "com.fongmi.android.tv.rust"
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
    module = "src/main/rust/tv_core"
    libname = "tv_core"
    targets = listOf("arm64", "arm")
    profile = "release"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra.get("kotlinxCoroutinesVersion")}")
    implementation("androidx.annotation:annotation:1.9.1")
}

tasks.matching { it.name.matches(Regex("merge.*JniLibFolders")) }.configureEach {
    dependsOn("cargoBuild")
}
