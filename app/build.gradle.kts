plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.baseline.profile)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "co.aquidigital.lloyds_tech_test"
    compileSdk = 34

    defaultConfig {
        applicationId = "co.aquidigital.lloyds_tech_test"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            // TODO: for development purposes, remember to create a release signing config when releasing proper app
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    composeCompiler {
        enableStrongSkippingMode = true
    }

    kotlin {
        jvmToolchain(17)
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

baselineProfile {
    dexLayoutOptimization = true
}

dependencies {
    implementation(project(":core"))
    implementation(project(":features"))

    implementation(libs.hilt)
    implementation(libs.navigation) // needed for Room
    implementation(libs.room.ktx)
    implementation(libs.timber)

    implementation(libs.test.android.profile.installer)

    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}