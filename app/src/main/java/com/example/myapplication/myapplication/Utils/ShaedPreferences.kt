package com.example.myapplication.myapplication.Utils

import android.app.Activity
import android.content.Context

class ShaedPreferences {
    fun putStringSharedPrefs(context: Activity, value: String?, key: String?) {
        val mPrefs = context.getSharedPreferences("SharedPrefs", 0)
        val editor = mPrefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun putBooleanSharedPrefs(context:Activity, value:Boolean?,key: String?){
        val mPrefs = context.getSharedPreferences("SharedPrefs", 0)
        val editor=mPrefs.edit()
        if (value != null) {
            editor.putBoolean(key, value)
        }
        editor.apply()
    }
    fun getSharedPrefernces(context: Activity, key:String): String? {
        val mPrefs = context.getSharedPreferences("SharedPrefs", 0)
        return mPrefs.getString(key, "")

    }
    fun getBooleanSharedPrefs(context: Context, key: String?): Boolean {
        val mPrefs = context.getSharedPreferences("SharedPrefs", 0)
        return mPrefs.getBoolean(key, false)
    }

}