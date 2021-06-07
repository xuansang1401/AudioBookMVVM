package com.t3h.basemvvm.util

import android.content.Context
import android.content.SharedPreferences

class AppConfig(context: Context) {

    private var myShared: SharedPreferences = context.getSharedPreferences(
        "MyAPP",
        Context.MODE_PRIVATE
    )

    fun setIsLogin(isLogin : Boolean) {
        val editor = myShared.edit()
        editor.putBoolean("ISLOGIN", isLogin)
        editor.commit()
    }
    fun getIsLogin() : Boolean{
        return myShared.getBoolean("ISLOGIN", false)
    }

    fun setNameUser(name: String) {
        val editor = myShared.edit()
        editor.putString("NAMEUSER", name)
        editor.commit()
    }
    fun getNameUser() : String{
        return myShared.getString("NAMEUSER", "Sang").toString()
    }

    fun setAvatarUser(name: String) {
        val editor = myShared.edit()
        editor.putString("AVATAR", name)
        editor.commit()
    }
    fun getAvatarUser() : String{
        return myShared.getString("AVATAR", "Sang").toString()
    }


    companion object {
        fun create(context: Context): AppConfig {
            return AppConfig(context)
        }
    }
}