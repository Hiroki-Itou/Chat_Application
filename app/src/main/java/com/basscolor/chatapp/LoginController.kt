package com.basscolor.chatapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception


class LoginController(override val activity: Activity) : LoginActivityListener,Mediator{

    private var _email : String? = null
    private var _password : String? = null
    private val authentication = Authentication()
    private val login = Login()
    private val currentUserManagemaent = CurrentUserManagemaent()


    override fun login_Check() {

        cureateColleagues()


        if (authentication.isLogin()){
            progress(true)
            Log.d(TAG,"ログインしています")
            currentUserManagemaent.userDataAcquisition(authentication.get_currentUid()!!)
            //userDataAcquisition()
           // activity.startActivity(Intent(activity, ChatList_Activity::class.java))
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
        login.loginToChat(_email!!,_password!!)
       // authenticationConfirmation(_email!!,_password!!)
    }

    override fun cureateColleagues() {
        login.setMediator(this)
        currentUserManagemaent.setMediator(this)

    }

    override fun colleagueSuccess(colleague: Colleague) {

        when (colleague) {
            login ->{
                Log.d(TAG, "認証確認が完了しました")
                currentUserManagemaent.userDataAcquisition(authentication.get_currentUid()!!)
            }
            currentUserManagemaent ->{
                Log.d(TAG, "ユーザーデータの取得に成功しました")
                val user = currentUserManagemaent.getUserContainer()
                CurrentUser.cureateUserData(user.get_name(),user.get_email(),user.get_uid())
                progress(false)
                val intent = Intent(activity, ChatList_Activity::class.java)
                activity.startActivity(intent)
            }
            else -> Log.d(TAG,"")
        }
    }

    override fun colleagueFailure(colleague: Colleague) {

    }

    override fun colleagueError(colleague: Colleague) {

    }


    private fun authenticationConfirmation(email: String,password: String){

        //authentication.login(email,password)
//        authentication.login(email,password,object : Authentication.Login_Delegate {
//
//            override fun success() {
//                progress(false)
//                Log.d(TAG, "認証確認が完了しました")
//                userDataAcquisition()
//            }
//            override fun error(exception: Exception?) {
//                progress(false)
//                Toast.makeText(activity, "認証確認に失敗しました。",
//                    Toast.LENGTH_LONG).show()
//                Log.d(TAG, "認証確認中にエラーが発生しました", exception)
//            }
//        })
    }

//    private fun userDataAcquisition(){
//        val dataBase = DataBase()
//        dataBase.get_currentUserDatabase(authentication.get_currentUid()!!,object : DataBase.Get_currentUserDatabase_Delegate {
//
//            override fun success(user: UserContainer) {
//                Log.d(TAG, "ユーザーデータの取得に成功しました")
//                CurrentUser.cureateUserData(user.get_name(),user.get_email(),user.get_uid())
//
//                val intent = Intent(activity, ChatList_Activity::class.java)
//                activity.startActivity(intent)
//            }
//
//            override fun error(exception: Exception) {
//                Log.d(TAG, "ユーザーデータ取得中にエラーが発生しました", exception)
//            }
//        })
//    }

    override fun to_user_registration_screen() {
        Log.d(TAG,"ユーザー登録画面へ遷移")
        activity.startActivity(Intent(activity, SigninActivity::class.java))
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