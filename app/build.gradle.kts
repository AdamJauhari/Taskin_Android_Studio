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
    buildFeatures {
        viewBinding = true
    }
}

val room_version = "2.6.1"

// Read JDK home path from gradle.properties
val projectJdkHomePath = project.extra.properties.get("org.gradle.java.home")?.toString()

kapt {
    correctErrorTypes = true
    if (projectJdkHomePath != null && projectJdkHomePath.isNotEmpty()) {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas".toString())
        }
        println("Kapt arguments potentially using JDK home: $projectJdkHomePath")
    }

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

// Read JDK home path from gradle.properties
// val jdkHomePath = System.getProperty("org.gradle.java.home") ?: project.extra.properties.get("org.gradle.java.home")?.toString() // Commented out problematic block

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        // if (jdkHomePath != null && jdkHomePath.isNotEmpty()) { // Commented out problematic block
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
}