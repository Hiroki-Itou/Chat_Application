package com.basscolor.chatapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


class LoginController(override val progressView: ConstraintLayout, override val activity: Activity) : LoginActivityListener{


    private val TAG = this.toString()

    private var _email : String? = null
    private var _password : String? = null
    private val _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun login_Check() {

        if (_firebaseAuth.currentUser != null){
            Log.d(TAG,"ログインしています")
            activity.startActivity(Intent(activity, ChatList_Activity::class.java))
        }else{
            Log.d(TAG,"ログインしていません")
        }
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
       progress(true)
        _firebaseAuth.signInWithEmailAndPassword(_email!!, _password!!)
            .addOnCompleteListener { task :Task<AuthResult>->
                if (task.isSuccessful) {
                   progress(false)

                    val intent = Intent(activity, ChatList_Activity::class.java)

                    activity.startActivity(intent)
                    Log.d(TAG, "ログインが完了しました")

                } else {

                    progress(false)
                    Toast.makeText(activity, "ログインに失敗しました。",
                        Toast.LENGTH_LONG).show()
                    Log.d(TAG, "ログイン中にエラーが発生しました", task.exception)
                }
            }
    }

    override fun to_user_registration_screen() {
        Log.d(TAG,"ユーザー登録画面へ遷移")

        activity.startActivity(Intent(activity, SigninActivity::class.java))

    }

    private fun progress(switch:Boolean){

        if(switch){
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressView.visibility = android.widget.ProgressBar.VISIBLE
        }else{
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressView.visibility = android.widget.ProgressBar.INVISIBLE
        }
    }




}