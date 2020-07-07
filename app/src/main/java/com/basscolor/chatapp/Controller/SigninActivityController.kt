package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import com.basscolor.chatapp.*
import com.basscolor.chatapp.FireBase.Authentication
import com.basscolor.chatapp.FireBase.UserDatabase
import com.basscolor.chatapp.Listener.SigninActivityListener
import com.google.firebase.auth.FirebaseAuth


class SigninActivityController(override val activity: Activity):SigninActivityListener {

    private var _userName : String? = null
    private var _email : String? = null
    private var _password : String? = null
    private var _confirmationPass : String? = null
    private var loadingIndicator: LoadingIndicator = LoadingIndicator(activity)
//
//    init {
//        cureateColleagues()
//    }


    private fun getUri(): Uri {

        val resId = R.drawable.icon

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
        if(!password_Check())return
        loadingIndicator.start()

        val authentication = Authentication()
        authentication.signin(_email!!,_password!!,
            {signin ->
                Log.d(TAG,signin)
                val userDatabase = UserDatabase()
                userDatabase.registration(preparationRegistration(),
                    {registration->
                        Log.d(TAG,registration)
                        val firebaseAuth = FirebaseAuth.getInstance()
                        authentication.setProfile(_userName!!,getUri(),
                            {setProfile->
                                Log.d(TAG,setProfile)
                                firebaseAuth.signOut()
                                loadingIndicator.stop()
                                activity.finish()//Login画面へ
                            },{e->
                                Log.d(TAG,"プロフィール登録中にエラーが発生しました$e")
                                loadingIndicator.stop()
                            })
                    },{e->
                        Log.d(TAG,"ユーザー登録中にエラーが発生しました$e")
                        loadingIndicator.stop()
                    })
            },{e->
                Log.d(TAG,"サインイン中にエラーが発生しました$e")
                loadingIndicator.stop()
            })


        //signin.SigninToChat(_email!!,_password!!,_userName!!)
    }

//    override fun cureateColleagues() {
//        signin.setMediator(this)
//        landlordList.setMediator(this)
//
//    }
//
//    override fun colleagueSuccess(colleague: Colleague) {
//
//        when (colleague) {
//            signin -> {
//
//                val userData = preparationRegistration()
//                landlordList.registration(userData)
//            }
//            landlordList ->{
//                Log.d(TAG,"ユーザー登録が完了しました")
//                loadingIndicator.stop()
//                FirebaseAuth.getInstance().signOut()
//                activity.finish()//Login画面へ
//            }
//            else -> Log.d(TAG, "")
//        }
//    }
//    override fun colleagueFailure(colleague: Colleague) {
//
//    }
//
//    override fun colleagueError(colleague: Colleague) {
//        Log.d(TAG,"collgueError")
//    }

    private fun preparationRegistration(): UserData {
        val uid =  FirebaseAuth.getInstance().currentUser!!.uid

        val registData = mapOf<String,Any>(
            "name" to _userName as String,
            "email" to _email as String,
            "userID" to uid
        )

        return UserData(registData)
    }

    private fun password_Check():Boolean{
        return _password == _confirmationPass
    }
}