-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-dontwarn com.razorpay.**

-optimizations !method/inlining/*

-keepclasseswithmembers class * {
  public void onPayment*(...);
}