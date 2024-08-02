plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.chsteam.mypets"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chsteam.mypets"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val room_version = "2.6.1"
    val carmera_version = "1.3.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.work:work-runtime:2.9.0")

    implementation("androidx.camera:camera-camera2:$carmera_version")
    implementation("androidx.camera:camera-lifecycle:$carmera_version")
    implementation("androidx.camera:camera-view:$carmera_version")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0")
    implementation("androidx.credentials:credentials:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.paging:paging-runtime:3.3.0")
    implementation("androidx.paging:paging-compose:3.3.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.google.accompanist:accompanist-pager:0.32.0")
    implementation("com.github.Jasonchenlijian:FastBle:2.4.0")
    implementation("androidx.documentfile:documentfile:1.0.1")
    implementation("io.insert-koin:koin-android:3.5.3")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.electronwill.night-config:core_android:3.6.6")
    implementation("com.electronwill.night-config:yaml_android:3.6.6")
    implementation("com.electronwill.night-config:json_android:3.6.6")
    implementation("com.electronwill.night-config:hocon_android:3.6.6")
    implementation("com.electronwill.night-config:toml_android:3.6.6")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("dev.shreyaspatil:capturable:2.1.0")
    implementation("com.himanshoe:kalendar:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("com.google.mlkit:digital-ink-recognition:17.0.0")
    implementation("me.zhanghai.compose.preference:library:1.0.0")
    implementation("androidx.glance:glance-appwidget:1.1.0")




    kapt("androidx.room:room-compiler:$room_version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}