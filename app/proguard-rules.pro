# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

###################################
# API 통신 데이터 모델
###################################
-keep public class kr.co.rolling.moment.library.network.data.** {*;}
-keep public class kr.co.rolling.moment.library.data.** {*;}

###################################
# androidx.annotation.Keep
###################################
-keep class androidx.annotation.Keep {*;}

###################################
# Kotlin
###################################
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

###################################
### Kotlin Coroutine
###################################
# https://github.com/Kotlin/kotlinx.coroutines/blob/master/README.md
# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler { *; }
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }
# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembernames class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

-dontwarn kotlinx.atomicfu.**
-dontwarn kotlinx.coroutines.**
-dontwarn kotlinx.coroutines.flow.**

###################################
# RxJava, RxAndroid (https://gist.github.com/kosiara/487868792fbd3214f9c9)
###################################
-dontwarn org.reactivestreams.FlowAdapters
-dontwarn org.reactivestreams.**
-dontwarn java.util.concurrent.Flow*
-dontwarn java.util.concurrent.flow.**
-dontwarn java.util.concurrent.**

###################################
# Gson
###################################
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class okio.** { *; }
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type
-keep class **$GsonConverter { *; }
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault


###################################
# Firebase
###################################
-keep public class com.google.firebase.analytics.FirebaseAnalytics {
    public *;
}

-keep public class com.google.android.gms.measurement.AppMeasurement {
    public *;
}

###################################
# Retrofit 2
###################################
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }


###################################
# Skydoves Sandwich
###################################
-keep class com.skydoves.sandwich.** { *; }
-keep interface com.skydoves.sandwich.** { *; }


###################################
# OkHttp3
###################################
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }


###################################
# Crash report
###################################
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-dontwarn com.google.errorprone.annotations.*

#-------------------------------------------------
# JetPack Navigation
# This fixes: Caused by: androidx.fragment.app.Fragment$InstantiationException: Unable to instantiate fragment androidx.navigation.fragment.NavHostFragment: make sure class name exists
#-------------------------------------------------
-keepnames class androidx.navigation.fragment.NavHostFragment
-keep class * extends androidx.fragment.app.Fragment{}

# Parcelize
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# kakao login
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep interface com.kakao.sdk.**.*Api