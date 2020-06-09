package com.basscolor.chatapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isInvisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.activity_login.*


class Login_Controller(override val progressView: ConstraintLayout, override val activity: Activity) : Login_Activity_Listener{


    private val TAG = this.toString()

    private var _email : String? = null
    private var _password : String? = null
    private var _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


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
        progressView.visibility = android.widget.ProgressBar.VISIBLE
        _firebaseAuth.signInWithEmailAndPassword(_email!!, _password!!)
            .addOnCompleteListener { task :Task<AuthResult>->
                if (task.isSuccessful) {
                    progressView.visibility = android.widget.ProgressBar.INVISIBLE
                    activity.startActivity(Intent(activity, ChatList_Activity::class.java))
                    Log.d(TAG, "ログインが完了しました")

                } else {

                    progressView.visibility = android.widget.ProgressBar.INVISIBLE
                    Log.d(TAG, "ログイン中にエラーが発生しました", task.exception)
                }
            }






//        _firebaseAuth.createUserWithEmailAndPassword(_email!!, _password!!)
//            .addOnCompleteListener { task: Task<AuthResult> ->
//                if (task.isSuccessful) {
//                    //Registration OK
//                    activity.progressBar.visibility = android.widget.ProgressBar.INVISIBLE
//                    activity.startActivity(Intent(activity, ChatList_Activity::class.java))
//
//                   // val firebaseUser = this._firebaseAuth.currentUser!!
//                } else {
//
//                    Log.d(TAG, "ログイン中にエラーが発生しました")
//                    activity.progressBar.visibility = android.widget.ProgressBar.INVISIBLE
//
//                }
//            }
    }

    override fun to_user_registration_screen() {
        Log.d(TAG,"ユーザー登録画面へ遷移")

        activity.startActivity(Intent(activity, SigninActivity::class.java))

    }




}