package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.basscolor.chatapp.Activity.ChatRoomListActivity
import com.basscolor.chatapp.Activity.SigninActivity
import com.basscolor.chatapp.FireBase.Authentication
import com.basscolor.chatapp.LoadingIndicator
import com.basscolor.chatapp.Listener.LoginActivityListener


class LoginActivityController(override val activity: Activity) :
    LoginActivityListener {

    private var _email : String? = null
    private var _password : String? = null
    private val authentication = Authentication()
    private var loadingIndicator: LoadingIndicator =
        LoadingIndicator(activity)

    override fun loginCheck() {

        if (authentication.isLogin()){
            loadingIndicator.start()
            Log.d(TAG,"ログインしています")

            toActivity()

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

    override  fun  onLogIn() {
        if (_email == null || _password == null) return
        loadingIndicator.start()

        val authentication = Authentication()

        authentication.login(_email!!,_password!!,{s ->
            Log.d(TAG, s)
            loadingIndicator.stop()
            toActivity()
        }, {e ->
            Log.e(TAG,"ログイン中にエラーが発生しました。"+e)
        })
    }

    private fun toActivity(){
        val intent = Intent(activity, ChatRoomListActivity::class.java)
        activity.startActivity(intent)
    }

    override fun toUserRregistration() {
        Log.d(TAG,"ユーザー登録画面へ遷移")
        activity.startActivity(Intent(activity, SigninActivity::class.java))
    }









}