# Edge Integrated App ProGuard Rules

# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Moshi
-keep class com.squareup.moshi.** { *; }
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <fields>;
    @com.squareup.moshi.ToJson <fields>;
}

# Keep Google AI Edge
-keep class com.google.ai.edge.** { *; }
-keep class com.google.mediapipe.** { *; }
-keep class org.tensorflow.lite.** { *; }

# Keep Termux
-keep class com.termux.** { *; }

# Keep Winlator
-keep class com.winlator.** { *; }

# Keep Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Keep Retrofit
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Keep DataStore
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

# Keep gRPC
-dontwarn io.grpc.**
-dontwarn com.google.protobuf.**
-keep class io.grpc.** { *; }

# Keep JSON
-keep class org.json.** { *; }
