package com.ar.of_pro.utils

import android.content.Context
import android.content.SharedPreferences



class SharedPrefUtils(context: Context, sharedFile: String = "my_preference") {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(sharedFile, Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()




    fun saveUserData(userType: String, userId: String) {
        editor.putString("userType", userType)
        editor.putString("clientId", userId)
        editor.apply()
    }

    fun removeUserData() {
        editor.remove("userType")
        editor.remove("clientId")
        editor.apply()
    }


     fun getFromSharedPrefs(variable: String?): String? {
        return sharedPreferences.getString(variable, "")
    }

    fun removeSharedPref(variable: String) {
        editor.remove(variable)
        editor.apply()
    }
}