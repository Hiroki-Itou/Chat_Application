package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import java.lang.Exception


class LoginActivityController(override val activity: Activity) : LoginActivityListener,LoginDelegate{

    private var _email : String? = null
    private var _password : String? = null
    private val authentication = Authentication()
    private val login = Login(this)
    private var loadingIndicator: LoadingIndicator = LoadingIndicator(activity)

    override fun loginCheck() {

        if (authentication.isLogin()){
            loadingIndicator.start()
            Log.d(TAG,"ログインしています")

            loginSuccess()

        }else{
            loadingIndicator.stop()
            Log.d(TAG,"ログインしていません")

        }
    }

    override fun onInputMailAddress(email: String) {
        this._email = email
        if (this._email == null)return
        Log.d(TAG,"E-mailを取得しました= "+this._email!!)
    }

    override fun onInputPassword(password: String) {
        this._password = password
        if (this._password == null)return
        Log.d(TAG,"Passwordを取得しました= "+this._password!!)
    }

    override fun onLogIn() {
        if (_email == null || _password == null) return
        loadingIndicator.start()
        login.loginToChat(_email!!,_password!!)
    }

    override fun toUserRregistration() {
        Log.d(TAG,"ユーザー登録画面へ遷移")
        activity.startActivity(Intent(activity, SigninActivity::class.java))
    }

    override fun loginSuccess() {
        Log.d(TAG, "認証確認が完了しました")
        loadingIndicator.stop()
        val intent = Intent(activity, ChatRoomListActivity::class.java)
        activity.startActivity(intent)
    }

    override fun loginFailure() {

    }

    override fun loginError(e: Exception?) {

        Log.e(TAG,"ログイン中にエラーが発生しました。"+e)
    }







}