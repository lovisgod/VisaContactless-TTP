package com.lovisgod.testVisaTTP.handlers

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.scottyab.rootbeer.BuildConfig
import com.scottyab.rootbeer.RootBeer

val isRunningOnEmulator: Boolean by lazy {
    // Android SDK emulator
    return@lazy ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
            && Build.FINGERPRINT.endsWith(":user/release-keys")
    && Build.MANUFACTURER == "Google" && Build.PRODUCT.startsWith("sdk_gphone_") && Build.BRAND == "google"
    && Build.MODEL.startsWith("sdk_gphone_"))
    || Build.HARDWARE.contains("goldfish")
    || Build.HARDWARE.contains("ranchu")
    || Build.PRODUCT.contains("sdk_gphone64_arm64")
    || Build.PRODUCT.contains("vbox86p")
    || Build.PRODUCT.contains("emulator")
    || Build.PRODUCT.contains("simulator")
    || Build.FINGERPRINT.startsWith("generic")
    || Build.FINGERPRINT.startsWith("unknown")
    || Build.PRODUCT.contains("sdk_google")
    || Build.MODEL.contains("google_sdk")
    || Build.PRODUCT.contains("sdk_x86")
    || Build.MODEL.contains("Emulator")
    || Build.MODEL.contains("Android SDK built for x86")
    || "QC_Reference_Phone" == Build.BOARD && !"Xiaomi".equals(
    Build.MANUFACTURER,
    ignoreCase = true
    )
    || Build.MANUFACTURER.contains("Genymotion")
    || Build.HOST.startsWith("Build") //MSI App Player
    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
    || Build.PRODUCT == "google_sdk")
}


// check if developer mode is on
fun isDevMode(context: Context): Boolean {
    return when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN -> {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) != 0
        }
        Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN -> {
            @Suppress("DEPRECATION")
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) != 0
        }
        else -> false
    }
}


fun checkRooted(context: Context) : Boolean {
    val rootBeer = RootBeer(context)
    return !BuildConfig.DEBUG && rootBeer.isRooted
//    else if (!BuildConfig.DEBUG && rootBeer.isRootedWithBusyBoxCheck) {
//        true
//    }
}