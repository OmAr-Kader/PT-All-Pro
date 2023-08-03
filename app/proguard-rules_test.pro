#-keep class com.pt.** { *; }
#-dontwarn com.pt.**
-dontwarn android.graphics.Canvas
-keep class androidx.core.app.CoreComponentFactory

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keep class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keep class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }

-keep class androidx.work.** { *; }
-keep public class * extends androidx.work.ListenableWorker {
    public <init>(...);
}
-keep public class * extends androidx.work.CoroutineWorker {
    public <init>(...);
}
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.InputMerger
-keep class androidx.work.WorkerParameters

-keep class com.pt.pro.main.odd.WorkInitializer.** { *; }
-keep class androidx.work.WorkerParameters
-keep class androidx.startup.AppInitializer
-keep class * extends androidx.startup.Initializer
-keepnames class * extends androidx.startup.Initializer
-keep class androidx.core.app.CoreComponentFactory
-keep class * extends androidx.startup.Initializer {
    <init>();
}

-keep class androidx.multidex.** { *; }

-dontwarn androidx.core.**
-dontnote androidx.core.**

-dontwarn androidx.media3.**
-dontnote androidx.media3.**

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-dontobfuscate
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    #public static *** e(...);
    #public static *** w(...);
}
-keepclasseswithmembernames class * {
native <methods>;
}

####################################################################################################
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

-dontusemixedcaseclassnames
-verbose
-keepattributes AnnotationDefault,
                EnclosingMethod,
                InnerClasses,
                RuntimeVisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations,
                Signature

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.google.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-dontnote android.support.**
-dontnote androidx.**
-dontwarn android.support.**
-dontwarn androidx.**

-dontwarn android.util.FloatMath

-keep class android.support.annotation.Keep
-keep class androidx.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}
-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

-dontnote org.apache.http.**
-dontnote android.net.http.**

-dontnote java.lang.invoke.**
