package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.basscolor.chatapp.Activity.ChatroomListActivity
import com.basscolor.chatapp.Activity.SigninActivity
import com.basscolor.chatapp.Listener.LoginActivityListener
import com.basscolor.chatapp.Model.FireBase.Authentication
import com.basscolor.chatapp.Model.LoadingIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivityController(override val activity: Activity) : LoginActivityListener {

    private var email : String? = null
    private var password : String? = null
    private val authentication = Authentication()
    private var loadingIndicator: LoadingIndicator = LoadingIndicator(activity)

    override fun loginCheck() {
        if (authentication.isLogin()){
            Log.d(TAG,"既にログインしています")
            transition()
        }else{
            Log.d(TAG,"ログインしていません")
        }
    }

    override fun onInputMailAddress(email: String) {
        this.email = email
        Log.d(TAG, "E-mailを取得しました= $email")
    }

    override fun onInputPassword(password: String) {
        this.password = password
        Log.d(TAG, "Passwordを取得しました= $password")
    }

    override fun onLogIn() {
        if (email == null || password == null){
            val message = "必要項目に記入漏れがあります"
            Log.d(TAG, "message")
            Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
            return
        }
        loadingIndicator.start()
        val authentication = Authentication()
        CoroutineScope(Dispatchers.Main).launch{
            try {
                val success = withContext(Dispatchers.IO) { authentication.login(email!!, password!!) }
                Log.d(TAG, success)
                transition()
            }catch (e:Exception){
                val message = "ログインに失敗しました"
                Log.e(TAG, "$message:$e")
                loadingIndicator.stop()
                Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
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