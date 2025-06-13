plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "uca.aidama.taskin"
    compileSdk = 34

    defaultConfig {
        applicationId = "uca.aidama.taskin"
        minSdk = 31
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // For Room schemas
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas".toString()
            }
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
    
    lint {
        disable += "MissingTranslation"
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

val room_version = "2.6.1"

kapt {
    correctErrorTypes = true
    // Arguments for Room
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas".toString())
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        // val jdkHomePath = System.getProperty("org.gradle.java.home") ?: project.extra.properties.get("org.gradle.java.home")?.toString()
        // if (jdkHomePath != null && jdkHomePath.isNotEmpty()) {
        //     jdkHome = jdkHomePath
        //     println("Set KotlinCompile JDK home to: $jdkHomePath for task $name")
        // } else {
        //     println("Warning: JDK_HOME for KotlinCompile not found in gradle.properties or system properties for task $name.")
        // }
        freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
    
    // Gson for JSON serialization/deserialization
    implementation("com.google.code.gson:gson:2.10.1")
}