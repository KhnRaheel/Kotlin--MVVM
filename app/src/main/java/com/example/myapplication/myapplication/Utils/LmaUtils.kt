package com.example.myapplication.myapplication.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class LmaUtils {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}