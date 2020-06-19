package com.basscolor.chatapp

import android.content.ContentValues
import android.util.Log

object CurrentUser {

    private lateinit var userData : UserData


    fun cureateUserData(name:String,email:String,uid:String){
        userData = UserData(name,email,uid)
        Log.w(ContentValues.TAG, "クラスにユーザーデータが登録されました  ")
    }

    fun getCurrentUserData():UserData{
        return this.userData
    }


}