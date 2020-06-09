package com.basscolor.chatapp

import android.util.Log

class Login_Controller : Login_Activity_Listener{


    private val TAG = this.toString()

    private var _email : String? = null
    private var _password : String? = null


    override fun login_Check() {
        Log.d(TAG,"ログインのCheck")
    }

    override fun get_mailAddress(email: String) {
      this._email = email
        if (this._email == null)return
        Log.d(TAG,"E-mailを取得しました= "+this._email!!)
    }

    override fun get_password(password: String) {
       this._password = password
        if (this._password == null)return
        Log.d(TAG,"Passwordを取得しました= "+this._password!!)
    }

    override fun log_in() {
        if (_email == null || _password == null) return
        Log.d(TAG,"ログインを行います")

    }

    override fun to_user_registration_screen() {
        Log.d(TAG,"ユーザー登録画面へ遷移")
    }


}