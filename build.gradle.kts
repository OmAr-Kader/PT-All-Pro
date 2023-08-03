buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${libs.versions.gradleBuild.get()}")
        classpath(kotlin("gradle-plugin", libs.versions.kotlinVersion.get()))
        classpath(kotlin("allopen", libs.versions.kotlinVersion.get()))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
tasks.register(name = "type", type = Delete::class) {
    delete(rootProject.buildDir)
}
