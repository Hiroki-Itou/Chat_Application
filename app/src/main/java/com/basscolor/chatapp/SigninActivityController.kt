package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class SigninActivityController(override val activity: Activity) :SigninActivityListener,Mediator{

    private var _userName : String? = null
    private var _email : String? = null
    private var _password : String? = null
    private var _confirmationPass : String? = null
    private val signin = Signin()
    private val landlordList = LandlordList()
    private var loadingIndicator: LoadingIndicator = LoadingIndicator(activity)

    init {
        cureateColleagues()
    }

    override fun onInputUserName(name: String) {
        this._userName = name
        if (this._userName == null)return
        Log.d(TAG,"_userNameを取得しました= "+this._userName!!)
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

    override fun onInputConfirmationPass(confirmationPass: String) {
        this._confirmationPass = confirmationPass
        if (this._confirmationPass == null)return
        Log.d(TAG,"_confirmationPassを取得しました= "+this._confirmationPass!!)
    }

    override fun onSignIn() {
        if (_email == null || _password == null || _userName == null ) return
        if(!password_Check())return
        loadingIndicator.start()

        signin.SigninToChat(_email!!,_password!!,_userName!!)
    }

    override fun cureateColleagues() {
        signin.setMediator(this)
        landlordList.setMediator(this)

    }

    override fun colleagueSuccess(colleague: Colleague) {

        when (colleague) {
            signin -> {

                val userData = preparationRegistration()
                landlordList.registration(userData)
            }
            landlordList ->{
                Log.d(TAG,"ユーザー登録が完了しました")
                loadingIndicator.stop()
                FirebaseAuth.getInstance().signOut()
                activity.finish()//Login画面へ
            }
            else -> Log.d(TAG, "")
        }
    }
    override fun colleagueFailure(colleague: Colleague) {

    }

    override fun colleagueError(colleague: Colleague) {
        Log.d(TAG,"collgueError")
    }

    private fun preparationRegistration():UserData{
        val uid =  FirebaseAuth.getInstance().currentUser!!.uid

        val registData = mapOf<String,Any>(
            "name" to _userName as String,
            "email" to _email as String,
            "userID" to uid
        )

        return  UserData(registData)
    }

    private fun password_Check():Boolean{
        return _password == _confirmationPass
    }
}