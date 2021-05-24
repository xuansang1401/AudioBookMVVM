package com.t3h.basemvvm.util

import android.content.Context
import android.content.SharedPreferences

class AppConfig(context: Context) {

    private var myShared: SharedPreferences = context.getSharedPreferences(
        "MyAPP",
        Context.MODE_PRIVATE
    )

    companion object {
        fun create(context: Context): AppConfig {
            return AppConfig(context)
        }
    }
}