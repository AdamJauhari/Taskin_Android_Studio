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
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf(
                "-Xjvm-default=all",
                "-Xjdk-release=11"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

val room_version = "2.6.1"

kapt {
    javacOptions {
        option("--add-opens=java.base/java.lang=ALL-UNNAMED")
        option("--add-opens=java.base/java.io=ALL-UNNAMED")
        option("--add-opens=java.base/java.util=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED")
        option("--add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED")
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
}