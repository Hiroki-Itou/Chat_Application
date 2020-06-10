package com.basscolor.chatapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SigninController(override val progressView: ConstraintLayout, override val activity: Activity) :SigninActivityListener{

    private val _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = this.toString()

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
        _firebaseAuth.createUserWithEmailAndPassword(_email!!, _password!!)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Log.d(TAG,"サインインが完了しました")
                    registration( this._firebaseAuth.currentUser!!.uid)
                } else {

                    progress(false)
                    Log.d(TAG, "サインイン中にエラーが発生しました", task.exception)

                }
            }
    }

    private fun registration(uid:String){

        val user = hashMapOf(
            "name" to _userName,
            "email" to _email,
            "uid" to uid
        )

        _firestore.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG," 登録が完了しました")
                progress(false)
                activity.startActivity(Intent(activity, ChatList_Activity::class.java))
            }
            .addOnFailureListener { e ->
                progress(false)
                Log.w(TAG, " 登録中にエラーが発生しました ", e)
            }
    }

    private fun password_Check():Boolean{
        return _password == _confirmationPass
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