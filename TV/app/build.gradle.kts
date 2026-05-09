plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.fongmi.android.tv"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.fongmi.android.tv"
        minSdk = 24
        targetSdk = 28
        versionCode = 541
        versionName = "5.4.1"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "eventBusIndex" to "com.fongmi.android.tv.event.EventIndex"
                ))
            }
        }
    }

    flavorDimensions += listOf("mode", "abi")

    productFlavors {
        create("leanback") { dimension = "mode" }
        create("mobile") { dimension = "mode" }
        create("arm64_v8a") {
            dimension = "abi"
            ndk { abiFilters += "arm64-v8a" }
        }
        create("armeabi_v7a") {
            dimension = "abi"
            ndk { abiFilters += "armeabi-v7a" }
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "proguard-rules-media.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "META-INF/beans.xml"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

    configurations.all {
        exclude(group = "com.android.support")
        resolutionStrategy {
            force("com.squareup.okhttp3:okhttp:${rootProject.extra.get("okhttpVersion")}")
        }
    }

    lint {
        disable += "UnsafeOptInUsageError"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation(project(":core-rust"))
    implementation(project(":engine-quickjs"))
    implementation(project(":feature-dlna"))
    implementation(project(":feature-config"))
    implementation(project(":feature-remote"))

    val composeBom = platform("androidx.compose:compose-bom:${rootProject.extra.get("composeBomVersion")}")
    implementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.tv:tv-foundation:1.0.0")
    implementation("androidx.tv:tv-material:1.0.0")

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation("androidx.lifecycle:lifecycle-service:${rootProject.extra.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra.get("lifecycleVersion")}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${rootProject.extra.get("lifecycleVersion")}")
    implementation("androidx.media:media:1.7.1")

    val media3Version = rootProject.extra.get("media3Version") as String
    implementation("androidx.media3:media3-common:$media3Version")
    implementation("androidx.media3:media3-container:$media3Version")
    implementation("androidx.media3:media3-database:$media3Version")
    implementation("androidx.media3:media3-datasource:$media3Version")
    implementation("androidx.media3:media3-datasource-okhttp:$media3Version")
    implementation("androidx.media3:media3-datasource-rtmp:$media3Version")
    implementation("androidx.media3:media3-decoder:$media3Version")
    implementation("androidx.media3:media3-effect:$media3Version")
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version")
    implementation("androidx.media3:media3-exoplayer-hls:$media3Version")
    implementation("androidx.media3:media3-exoplayer-rtsp:$media3Version")
    implementation("androidx.media3:media3-exoplayer-smoothstreaming:$media3Version")
    implementation("androidx.media3:media3-extractor:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")

    implementation("androidx.palette:palette:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.room:room-runtime:${rootProject.extra.get("roomVersion")}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    implementation("cat.ereza:customactivityoncrash:2.4.0")
    implementation("com.airbnb.android:lottie:6.7.1")
    implementation("com.airbnb.android:lottie-compose:6.7.1")
    implementation("com.github.bassaer:materialdesigncolors:1.0.0")
    implementation("com.github.bumptech.glide:glide:${rootProject.extra.get("glideVersion")}")
    implementation("com.github.bumptech.glide:annotations:${rootProject.extra.get("glideVersion")}")
    implementation("com.github.bumptech.glide:avif-integration:${rootProject.extra.get("glideVersion")}")
    implementation("com.github.bumptech.glide:okhttp3-integration:${rootProject.extra.get("glideVersion")}")
    implementation("com.github.jahirfiquitiva:TextDrawable:1.0.3")
    implementation("com.github.TeamNewPipe:NewPipeExtractor:v0.26.1")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.google.android.material:material:1.14.0-beta01")
    implementation("com.guolindev.permissionx:permissionx:1.8.1")

    implementation("org.jupnp:org.jupnp:3.0.4")
    implementation("org.jupnp:org.jupnp.android:3.0.4")
    implementation("org.jupnp:org.jupnp.support:3.0.4")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("org.simpleframework:simple-xml:2.7.1") {
        exclude(group = "stax", module = "stax-api")
        exclude(group = "xpp3", module = "xpp3")
    }
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.31")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra.get("kotlinxCoroutinesVersion")}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${rootProject.extra.get("kotlinxSerializationVersion")}")
    implementation("com.google.code.gson:gson:${rootProject.extra.get("gsonVersion")}")
    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra.get("okhttpVersion")}")

    "leanbackImplementation"("androidx.leanback:leanback:1.2.0")
    "leanbackImplementation"("com.github.JessYanCoding:AndroidAutoSize:1.2.1")
    "mobileImplementation"("androidx.biometric:biometric:1.1.0")
    "mobileImplementation"("com.journeyapps:zxing-android-embedded:4.3.0")

    kapt("androidx.room:room-compiler:${rootProject.extra.get("roomVersion")}")
    kapt("com.github.bumptech.glide:compiler:${rootProject.extra.get("glideVersion")}")
    kapt("org.greenrobot:eventbus-annotation-processor:3.3.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs_nio:2.1.5")
}
