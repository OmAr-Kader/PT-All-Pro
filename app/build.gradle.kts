@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

inline val isDebugType: Boolean
    get() = ptAllPro.versions.appID.get() == ptAllPro.versions.appIDTest.get()

android {
    compileSdk = 33
    namespace = "com.pt.pro"
    defaultConfig {
        applicationId = ptAllPro.versions.appID.get()
        minSdk = 21
        targetSdk = 33
        versionCode = ptAllPro.versions.versionApp.get().toInt()
        versionName = ptAllPro.versions.versionAppName.get()

        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        multiDexKeepProguard = file("multidex-config.pro")
        if (isDebugType) {
            println("DEBUG=true")
            proguardFile(file("proguard-rules_test.pro"))
        } else {
            println("DEBUG=false")
            proguardFile(file("proguard-rules.pro"))
        }
        setProperty(
            "archivesBaseName",
            ptAllPro.versions.archives.name.get() + "-${ptAllPro.versions.versionApp.get()}"
        )
    }
    signingConfigs {
        create("release") {
            keyAlias = ptAllPro.versions.keyStore.name.release.get()
            keyPassword = ptAllPro.versions.keyStore.password.get()
            storeFile = file(ptAllPro.versions.keyStore.file.release.get())
            storePassword = ptAllPro.versions.keyStore.password.get()
            storeType = ptAllPro.versions.keyStore.type.get()
        }
    }
    buildTypes {
        release {
            val proguard = file("proguard-rules.pro")
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false

            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
            isPseudoLocalesEnabled = false
            postprocessing {
                isRemoveUnusedCode = true
                isRemoveUnusedResources = true
                isObfuscate = true
                isOptimizeCode = true
                proguardFile(proguard)
            }
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            val proguard = file("proguard-rules_test.pro")
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false

            enableUnitTestCoverage = false
            enableAndroidTestCoverage = false
            isPseudoLocalesEnabled = false
            postprocessing {
                isRemoveUnusedCode = true
                isRemoveUnusedResources = true
                isObfuscate = true
                isOptimizeCode = true
                proguardFile(proguard)
            }
            signingConfig = signingConfigs.getByName("release")
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
    bundle {
        language {
            enableSplit = false
        }
        storeArchive {
            enable = false
        }
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    packagingOptions.jniLibs.useLegacyPackaging = false
    buildToolsVersion = "33.0.2"
    lint {
        disable.addAll(setOf("Recycle", "UnusedAttribute", "UselessParent"))
    }
}

dependencies {
    api(libs.coroutines)
    implementation(libs.reflect)
    implementation(libs.jet.annotation)
    implementation(libs.google.review)
    implementation(libs.x.media3.exoplayer)
    implementation(libs.x.media3.session)
    implementation(libs.x.media3.common)
    /*implementation(libs.x.media3.ui) {
        isTransitive = true
    }*/
    /*implementation(libs.x.media3.cast) {
        isTransitive = true
    }
    implementation(libs.cast.framework)*/

    implementation(libs.x.lifecycle.service)
    implementation(libs.x.core)
    implementation(libs.x.core.anim)
    implementation(libs.x.work)
    implementation(libs.x.startup)
    implementation(libs.x.activity)
    implementation(libs.x.fragment)
    implementation(libs.x.legacy)
    implementation(libs.x.appcompat)
    implementation(libs.x.appcompat.rec)

    implementation(libs.x.annotation)
    implementation(libs.x.enterprise)

    implementation(libs.x.viewpager2)
    implementation(libs.x.cardview)
    implementation(libs.x.recycler)
    implementation(libs.constraint)

    implementation(libs.slide.up)
    implementation(libs.x.asynclayout)
    implementation(libs.git.timePicker)
    implementation(libs.sub.scale)
    implementation(libs.x.transition)
    implementation(libs.x.exifinterface)
    implementation(libs.x.preference)
    implementation(libs.x.collection)
    implementation(libs.x.multidex)
    implementation(project(path = libs.versions.common.get()))
    implementation(libs.x.core.splashscreen)

}