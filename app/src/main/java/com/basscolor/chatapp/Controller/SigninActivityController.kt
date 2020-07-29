package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.basscolor.chatapp.*
import com.basscolor.chatapp.Deta.UserData
import com.basscolor.chatapp.Model.FireBase.Authentication
import com.basscolor.chatapp.Model.FireBase.UserDatabase
import com.basscolor.chatapp.Listener.SigninActivityListener
import com.basscolor.chatapp.Model.LoadingIndicator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class SigninActivityController(override val activity: Activity):SigninActivityListener,CoroutineScope {

    private var _userName : String? = null
    private var _email : String? = null
    private var _password : String? = null
    private var _confirmationPass : String? = null
    private var loadingIndicator: LoadingIndicator =
        LoadingIndicator(activity)
    private val userDatabase = UserDatabase()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private fun getUri(): Uri {

        val resId = R.drawable.ic_user
        val resources = activity.resources
        val uri =  Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(
                resId
            ) + '/'.toString() + resources.getResourceTypeName(resId) + '/'.toString() + resources.getResourceEntryName(
                resId
            )
        )
        return uri
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
        loadingIndicator.start()
        inputDataCheck()
    }

    private fun signinAction(){
        val authentication = Authentication()
        launch {
            try {
                val signinSuccess = authentication.signin(_email!!,_password!!)
                Log.d(TAG,signinSuccess)
                val registrationSuccess = userDatabase.registration(preparationRegistration())
                Log.d(TAG,registrationSuccess)
                val profileSuccess = authentication.setProfile(_userName!!,getUri())
                Log.d(TAG,profileSuccess)
                FirebaseAuth.getInstance().signOut()
                loadingIndicator.stop()
                activity.finish()//Login画面へ
            }catch (e:Exception){
                val message = "サインイン中にエラーが発生しました"
                Log.e(TAG, "$message:$e")
                loadingIndicator.stop()
                Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun preparationRegistration(): UserData {
        val uid =  FirebaseAuth.getInstance().currentUser!!.uid

        val registData = mapOf<String,Any>(
            "name" to _userName as String,
            "email" to _email as String,
            "userID" to uid
        )
        return UserData(registData)
    }

    private fun inputDataCheck(){

        launch{
            try {
                if(!userDatabase.isEnabledName(_userName!!)){
                    loadingIndicator.stop()
                    Toast.makeText(activity,"入力したユーザー名は既に使用されています",Toast.LENGTH_LONG).show()
                    return@launch
                }
                if(!userDatabase.isEnabledEmail(_email!!)){
                    loadingIndicator.stop()
                    Toast.makeText(activity,"入力したメールアドレスは既に使用されています",Toast.LENGTH_LONG).show()
                    return@launch
                }
                if(_password != _confirmationPass){
                    loadingIndicator.stop()
                    Toast.makeText(activity,"入力したパスワードが一致していません",Toast.LENGTH_LONG).show()
                    return@launch
                }
                Log.d(TAG,"コルーチンがすべて通過しました")
                signinAction()
            }catch (e:Exception){
                Log.e(TAG,"重複チェック中にエラーが発生しました:$e")
            }
        }
    }
}