@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

inline val isDebugType: Boolean
    get() = ptAllPro.versions.appID.get() == ptAllPro.versions.appIDTest.get()

android {
    compileSdk = 33
    namespace = "com.pt.common"

    defaultConfig {
        minSdk = 21
        multiDexEnabled = true
        multiDexKeepProguard = file("multidex-config.pro")
        if (isDebugType) {
            println("DEBUG=true")
            consumerProguardFiles(file("consumer-rules_test.pro"))
            proguardFile(file("proguard-rules_test.pro"))
            buildType("app_test.properties", "true")
        } else {
            println("DEBUG=false")
            consumerProguardFiles(file("consumer-rules.pro"))
            proguardFile(file("proguard-rules.pro"))
            buildType("app.properties", "false")
        }
    }

    buildTypes {
        release {
            val consumer = file("consumer-rules.pro")
            val proguard = file("proguard-rules.pro")
            isJniDebuggable = false
            isRenderscriptDebuggable = false

            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
            isPseudoLocalesEnabled = false
            postprocessing {
                isRemoveUnusedCode = true
                isObfuscate = true
                isOptimizeCode = true
                consumerProguardFile(consumer)
                proguardFile(proguard)
            }
        }
        debug {
            val consumer = file("consumer-rules_test.pro")
            val proguard = file("proguard-rules_test.pro")
            isJniDebuggable = false
            isRenderscriptDebuggable = false

            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
            isPseudoLocalesEnabled = false
            postprocessing {
                isRemoveUnusedCode = true
                isObfuscate = true
                isOptimizeCode = true
                consumerProguardFile(consumer)
                proguardFile(proguard)
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin.jvmToolchain(17)
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
    kotlinOptions {
        jvmTarget = "17"
        suppressWarnings = false
        languageVersion = libs.versions.kotlinApiVersion.get()
        apiVersion = libs.versions.kotlinApiVersion.get()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.contracts.ExperimentalContracts",
        )
    }
    ndkVersion = "25.2.9519653"
    buildToolsVersion = "33.0.2"
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    packagingOptions.jniLibs.useLegacyPackaging = false
    lint {
        disable.addAll(setOf("Recycle", "UnusedAttribute", "UselessParent"))
    }
}

fun com.android.build.api.dsl.VariantDimension.buildType(str: String, boolStr: String) {
    buildConfigField("boolean", "VERSION_DEBUG", boolStr)
    buildConfigField("String", "APPLICATION_ID", "\"" + ptAllPro.versions.appID.get() + "\"")
    println("VERSION_DEBUG=${boolStr}")
    FileInputStream(
        "${projectDir.parent}/$str"
    ).use {
        return@use Properties().apply { load(it) }
    }.onEach { (key, value) ->
        buildConfigField("String", (key as String), "\"" + (value as String) + "\"")
    }
}

dependencies {
    api(libs.coroutines)
    implementation(libs.reflect)

    implementation(libs.x.core)
    implementation(libs.x.core.anim)
    implementation(libs.x.appcompat)
    implementation(libs.x.appcompat.rec)
    implementation(libs.constraint)
    implementation(libs.x.dynamic)
    implementation(libs.x.transition)
    implementation(libs.x.exifinterface)
    implementation(libs.x.preference)
    implementation(libs.x.palette)
    implementation(libs.x.recycler)
    implementation(libs.x.coordinatorlayout)
    implementation(libs.x.collection)
    implementation(libs.x.multidex)
    implementation(libs.jsoup)
    implementation(libs.adonai)
    implementation(libs.glide.lib) {
        exclude(group = "glide-parent")
        isTransitive = true
    }
    kapt(libs.glide.comp)
    implementation(libs.google.recognition)

}