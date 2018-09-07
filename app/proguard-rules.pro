# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
###############云刷脸动作活体混淆规则 faceverify-BEGIN##################
#不混淆内部类
-keepattributes InnerClasses
-keep public class com.webank.faceaction.tools.WbCloudFaceVerifySdk{
    public <methods>;
    public static final *;
}
-keep public class com.webank.faceaction.tools.WbCloudFaceVerifySdk$*{
    *;
}
-keep public class com.webank.faceaction.tools.ErrorCode{
    *;
}
-keep public class com.webank.faceaction.ui.FaceVerifyStatus{

}
-keep public class com.webank.faceaction.ui.FaceVerifyStatus$Mode{
    *;
}
-keep public class com.webank.faceaction.tools.IdentifyCardValidate{
    public <methods>;
}
-keep public class com.tencent.youtulivecheck.**{
    *;
}
-keep public class com.webank.faceaction.Request.*$*{
    *;
}
-keep public class com.webank.faceaction.Request.*{
    *;
}
##############云刷脸动作活体混淆规则 faceverify-END######################
您可以将如上代码拷贝到您的混淆文件中，也可以将 SDK 中的webank-cloud-face-verify-proguard-rules.pro拷贝到主工程根目录下，然后通过-include webank-cloud-face-verify-rules.pro加入到您的混淆文件中。

云刷脸 SDK（数字活体）的混淆规则

 ###############云刷脸混淆规则 faceverify-BEGIN##################
#不混淆内部类
-keepattributes InnerClasses
-keep public class com.webank.facenum.tools.WbCloudFaceVerifySdk{
    public <methods>;
    public static final *;
}
-keep public class com.webank.facenum.tools.WbCloudFaceVerifySdk$*{
    *;
}
-keep public class com.webank.facenum.tools.ErrorCode{
    *;
}
-keep public class com.webank.facenum.ui.FaceVerifyStatus{

}
-keep public class com.webank.facenum.ui.FaceVerifyStatus$Mode{
    *;
}
-keep public class com.webank.facenum.tools.IdentifyCardValidate{
    public <methods>;
}
-keep public class com.tencent.youtulivecheck.**{
    *;
}
-keep public class com.webank.facenum.Request.*$*{
    *;
}
-keep public class com.webank.facenum.Request.*{
    *;
}
################云刷脸混淆规则 faceverify-END########################

#############webank normal混淆规则-BEGIN###################
#不混淆内部类
-keepattributes InnerClasses
-keepattributes *Annotation*
-keepattributes Signature

-keep, allowobfuscation @interface com.webank.normal.xview.Inflater
-keep, allowobfuscation @interface com.webank.normal.xview.Find
-keep, allowobfuscation @interface com.webank.normal.xview.BindClick

-keep @com.webank.normal.xview.Inflater class *
-keepclassmembers class * {
    @com.webank.normal.Find *;
    @com.webank.normal.BindClick *;
}

-keep public class com.webank.normal.net.*$*{
    *;
}
-keep public class com.webank.normal.net.*{
    *;
}
-keep public class com.webank.normal.thread.*$*{
   *;
}
-keep public class com.webank.normal.thread. *{
   *;
}
-keep public class com.webank.normal.tools.WLogger{
    *;
}

#wehttp混淆规则
-dontwarn com.webank.mbank.okio.**

-keep class com.webank.mbank.wehttp.**{
    public <methods>;
}
-keep interface com.webank.mbank.wehttp.**{
    public <methods>;
}
-keep public class com.webank.mbank.wehttp.WeLog$Level{
    *;
}
-keep class com.webank.mbank.wejson.WeJson{
    public <methods>;
}

#webank normal包含的第三方库bugly
-keep class com.tencent.bugly.webank.**{
    *;
}
###########webank normal混淆规则-END#######################

########云产品依赖的第三方库 混淆规则-BEGIN############

## support:appcompat-v7
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

#########云产品依赖的第三方库 混淆规则-END#############
