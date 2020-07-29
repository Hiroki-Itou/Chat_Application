package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.basscolor.chatapp.Activity.ChatroomListActivity
import com.basscolor.chatapp.Activity.SigninActivity
import com.basscolor.chatapp.Model.FireBase.Authentication
import com.basscolor.chatapp.Model.LoadingIndicator
import com.basscolor.chatapp.Listener.LoginActivityListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class LoginActivityController(override val activity: Activity) :
    LoginActivityListener, CoroutineScope {

    private var _email : String? = null
    private var _password : String? = null
    private val authentication = Authentication()
    private var loadingIndicator: LoadingIndicator =
        LoadingIndicator(activity)
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() =  Dispatchers.Main + job

    override fun loginCheck() {
        if (authentication.isLogin()){
            Log.d(TAG,"既にログインしています")
            transition()
        }else{
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
        val authentication = Authentication()

        launch{
            try {
                val loginSuccess = authentication.login(_email!!,_password!!)
                Log.d(TAG, loginSuccess)
                transition()
            }catch (e:Exception){
                val message = "ログインに失敗しました"
                Log.e(TAG, "$message:$e")
                loadingIndicator.stop()
                Toast.makeText(activity,message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun transition(){
        loadingIndicator.stop()
        val intent = Intent(activity, ChatroomListActivity::class.java)
        activity.startActivity(intent)
    }

    override fun toUserRregistration() {
        Log.d(TAG,"ユーザー登録画面へ遷移")
        activity.startActivity(Intent(activity, SigninActivity::class.java))
    }
}