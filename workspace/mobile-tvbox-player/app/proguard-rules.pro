# Add project specific ProGuard rules here.

# libmpv
-keep class com.player.tvbox.player.MpvPlayer { *; }
-keepclassmembers class com.player.tvbox.player.MpvPlayer {
    private native *** mpvCreate();
    private native *** mpvInitialize(long);
    private native *** mpvSetOptionString(long, java.lang.String, java.lang.String);
    private native *** mpvCommand(long, java.lang.String);
    private native *** mpvSetPropertyString(long, java.lang.String, java.lang.String);
    private native *** mpvGetPropertyString(long, java.lang.String);
    private native *** mpvGetPropertyLong(long, java.lang.String);
    private native *** mpvGetPropertyDouble(long, java.lang.String);
    private native *** mpvDestroy(long);
}

# ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
-keep class com.google.android.exoplayer.** { *; }
-dontwarn com.google.android.exoplayer2.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.player.tvbox.model.** { *; }
-keepclassmembers class com.player.tvbox.model.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# ZXing
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.** { *; }

# Model classes
-keep class com.player.tvbox.model.TvBoxSource { *; }
-keep class com.player.tvbox.model.VideoSite { *; }
-keep class com.player.tvbox.model.HistoryItem { *; }
-keep class com.player.tvbox.model.FavoriteItem { *; }

# Database
-keep class com.player.tvbox.db.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep custom view constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Keep setters in Views
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# Keep Parcelables
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-dontwarn androidx.**

# Material Design
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**
