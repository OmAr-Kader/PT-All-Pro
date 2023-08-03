@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
    versionCatalogs {
        create("ptAllPro") {
            version("appID", "pt.gallery.music.file.manager.alarm.notepad")
            //version("appID", "pt.gallery.music.file.manager.alarm.notepad.test")
            version("versionApp", "31")
            version("versionAppName", "2.1.1")

            version("appIDStore", "pt.gallery.music.file.manager.alarm.notepad")
            version("appIDTest", "pt.gallery.music.file.manager.alarm.notepad.test")
            version("archives.name", "PT-Gallery-Music-File-Manger-Alarm-Notepad")

        }
        create("libs") {
            version("common", ":common")

            version("gradleBuild", "8.1.0")
            version("kotlinVersion", "1.8.22")
            version("kotlinApiVersion", "1.8")

            version("coroutineVersion", "1.7.1")

            library(
                "coroutines",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-core-jvm"
            ).versionRef("coroutineVersion")
            library(
                "reflect",
                "org.jetbrains.kotlin",
                "kotlin-reflect"
            ).versionRef("kotlinVersion")
            library(
                "jet.script",
                "org.jetbrains.kotlin",
                "kotlin-script-runtime"
            ).versionRef("kotlinVersion")
            version("jet.annotation.version", "24.0.1")
            library(
                "jet.annotation",
                "org.jetbrains",
                "annotations"
            ).versionRef("jet.annotation.version")

            version("x.core.version", "1.11.0-beta02")
            library(
                "x.core",
                "androidx.core",
                "core-ktx"
            ).versionRef("x.core.version")

            version("x.core.anim.version", "1.0.0-beta01")
            library(
                "x.core.anim",
                "androidx.core",
                "core-animation"
            ).versionRef("x.core.anim.version")

            version("ads.admob.version", "22.1.0")
            library(
                "ads.native",
                "com.google.android.gms",
                "play-services-ads"
            ).versionRef("ads.admob.version")

            version("google.recognition.version", "19.0.0")
            library(
                "google.recognition",
                "com.google.android.gms",
                "play-services-mlkit-text-recognition"
            ).versionRef("google.recognition.version")

            version("google.review.version", "2.0.1")
            library(
                "google.review",
                "com.google.android.play",
                "review-ktx"
            ).versionRef("google.review.version")

            version("x.appcompat.version", "1.6.1")
            library(
                "x.appcompat",
                "androidx.appcompat",
                "appcompat"
            ).versionRef("x.appcompat.version")
            library(
                "x.appcompat.rec",
                "androidx.appcompat",
                "appcompat-resources"
            ).versionRef("x.appcompat.version")

            version("constraint.version", "2.2.0-alpha10")
            library(
                "constraint",
                "androidx.constraintlayout",
                "constraintlayout"
            ).versionRef("constraint.version")

            version("slide.up.version", "3.4.0")
            library(
                "slide.up",
                "com.sothree.slidinguppanel",
                "library"
            ).versionRef("slide.up.version")

            version("x.recycler.version", "1.3.1-rc01")
            library(
                "x.recycler",
                "androidx.recyclerview",
                "recyclerview"
            ).versionRef("x.recycler.version")

            version("x.dynamic.version", "1.0.0-alpha03")
            library(
                "x.dynamic",
                "androidx.dynamicanimation",
                "dynamicanimation-ktx"
            ).versionRef("x.dynamic.version")


            version("x.transition.version", "1.4.1")
            library(
                "x.transition",
                "androidx.transition",
                "transition-ktx"
            ).versionRef("x.transition.version")
            version("x.exifinterface.version", "1.3.6")
            library(
                "x.exifinterface",
                "androidx.exifinterface",
                "exifinterface"
            ).versionRef("x.exifinterface.version")

            version("x.preference.version", "1.2.0")
            library(
                "x.preference",
                "androidx.preference",
                "preference-ktx"
            ).versionRef("x.preference.version")

            version("x.palette.version", "1.0.0")
            library(
                "x.palette",
                "androidx.palette",
                "palette"
            ).versionRef("x.palette.version")

            version("x.coordinatorlayout.version", "1.2.0")
            library(
                "x.coordinatorlayout",
                "androidx.coordinatorlayout",
                "coordinatorlayout"
            ).versionRef("x.coordinatorlayout.version")

            version("x.collection.version", "1.3.0-alpha04")
            library(
                "x.collection",
                "androidx.collection",
                "collection-ktx"
            ).versionRef("x.collection.version")

            version("x.multidex.version", "2.0.1")
            library(
                "x.multidex",
                "androidx.multidex",
                "multidex"
            ).versionRef("x.multidex.version")

            version("x.core.splashscreen.version", "1.0.1")
            library(
                "x.core.splashscreen",
                "androidx.core",
                "core-splashscreen"
            ).versionRef("x.core.splashscreen.version")
            version("x.asynclayout.version", "1.1.0-alpha01")
            library(
                "x.asynclayout",
                "androidx.asynclayoutinflater",
                "asynclayoutinflater"
            ).versionRef("x.asynclayout.version")
            version("git.timePicker.version", "1.2.1")
            library(
                "git.timePicker",
                "com.github.Sztorm",
                "TimePicker"
            ).versionRef("git.timePicker.version")

            version("sub.scale.version", "3.10.0")
            library(
                "sub.scale",
                "com.davemorrissey.labs",
                "subsampling-scale-image-view-androidx"
            ).versionRef("sub.scale.version")

            version("glide.version", "4.15.1")
            library(
                "glide.lib",
                "com.github.bumptech.glide",
                "glide"
            ).versionRef("glide.version")
            library(
                "glide.comp",
                "com.github.bumptech.glide",
                "compiler"
            ).versionRef("glide.version")

            version("jsoup.version", "1.16.1")
            library(
                "jsoup",
                "org.jsoup",
                "jsoup"
            ).versionRef("jsoup.version")

            version("adonai.version", "2.3.14")
            library(
                "adonai",
                "com.github.Adonai",
                "jaudiotagger"
            ).versionRef("adonai.version")

            version("x.media3.version", "1.1.0-rc01")
            library(
                "x.media3.exoplayer",
                "androidx.media3",
                "media3-exoplayer"
            ).versionRef("x.media3.version")
            library(
                "x.media3.session",
                "androidx.media3",
                "media3-session"
            ).versionRef("x.media3.version")
            library(
                "x.media3.common",
                "androidx.media3",
                "media3-common"
            ).versionRef("x.media3.version")
            library(
                "x.media3.ui",
                "androidx.media3",
                "media3-ui"
            ).versionRef("x.media3.version")
            library(
                "x.media3.cast",
                "androidx.media3",
                "media3-cast"
            ).versionRef("x.media3.version")

            version("cast.framework.version", "21.2.0")
            library(
                "cast.framework",
                "com.google.android.gms",
                "play-services-cast-framework"
            ).versionRef("cast.framework.version")

            version("x.lifecycle.service.version", "2.6.1")
            library(
                "x.lifecycle.service",
                "androidx.lifecycle",
                "lifecycle-service"
            ).versionRef("x.lifecycle.service.version")

            version("x.work.version", "2.8.1")
            library(
                "x.work",
                "androidx.work",
                "work-runtime-ktx"
            ).versionRef("x.work.version")

            version("x.startup.version", "1.2.0-alpha02")
            library(
                "x.startup",
                "androidx.startup",
                "startup-runtime"
            ).versionRef("x.startup.version")

            version("x.activity.version", "1.7.2")
            library(
                "x.activity",
                "androidx.activity",
                "activity-ktx"
            ).versionRef("x.activity.version")

            version("x.fragment.version", "1.6.0-rc01")
            library(
                "x.fragment",
                "androidx.fragment",
                "fragment-ktx"
            ).versionRef("x.fragment.version")

            version("x.legacy.version", "1.0.0")
            library(
                "x.legacy",
                "androidx.legacy",
                "legacy-support-v4"
            ).versionRef("x.legacy.version")

            version("x.annotation.version", "1.7.0-alpha02")
            library(
                "x.annotation",
                "androidx.annotation",
                "annotation"
            ).versionRef("x.annotation.version")

            version("x.enterprise.version", "1.1.0")
            library(
                "x.enterprise",
                "androidx.enterprise",
                "enterprise-feedback"
            ).versionRef("x.enterprise.version")

            version("x.viewpager2.version", "1.1.0-beta02")
            library(
                "x.viewpager2",
                "androidx.viewpager2",
                "viewpager2"
            ).versionRef("x.viewpager2.version")

            version("x.cardview.version", "1.0.0")
            library(
                "x.cardview",
                "androidx.cardview",
                "cardview"
            ).versionRef("x.cardview.version")
        }
    }
}
/*fun proString(key: String): String {
    return project.findProperty(key) as String
}*/
rootProject.name = "PT-All-Pro"
include(":app")
include(":common")
