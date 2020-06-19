package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class SigninController( override val activity: Activity) :SigninActivityListener{

    private  val authentication = Authentication()
    private var _userName : String? = null
    private var _email : String? = null
    private var _password : String? = null
    private var _confirmationPass : String? = null

    override fun get_userName(name: String) {
        this._userName = name
        if (this._userName == null)return
        Log.d(TAG,"_userNameを取得しました= "+this._userName!!)
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

    override fun get_confirmationPass(confirmationPass: String) {
        this._confirmationPass = confirmationPass
        if (this._confirmationPass == null)return
        Log.d(TAG,"_confirmationPassを取得しました= "+this._confirmationPass!!)
    }

    override fun sign_in() {

        if (_email == null || _password == null) return
        if(!password_Check())return
        progress(true)

        authentication.signin(_email!!,_password!!,object : Authentication.Signin_Delegate {
            override fun success() {
                Log.d(TAG,"認証登録が完了しました")
                registration( authentication.get_currentUid()!!)
            }

            override fun error(exception: Exception?) {
                progress(false)
                Log.d(TAG, "認証登録中にエラーが発生しました",exception)
            }
        })
    }

    private fun registration(uid:String){

        val user = hashMapOf(
            "name" to _userName,
            "email" to _email,
            "uid" to uid
        )

        val dataBase = DataBase()
        dataBase.registration(user,object : DataBase.Registration_Delegate {
            override fun success() {
                Log.d(TAG,"ユーザー登録が完了しました")

                CurrentUser.cureateUserData(_userName!!,_email!!,uid)

                progress(false)
                activity.startActivity(Intent(activity, ChatList_Activity::class.java))
            }

            override fun error(exception: Exception) {
                progress(false)
                Log.w(TAG, "ユーザー登録中にエラーが発生しました ", exception)
            }

        })
    }

    private fun password_Check():Boolean{
        return _password == _confirmationPass
    }

    private fun progress(switch:Boolean){

        if(switch){
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            activity.progressView.visibility = View.VISIBLE
        }else{
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            activity.progressView.visibility = View.INVISIBLE
        }
    }


}