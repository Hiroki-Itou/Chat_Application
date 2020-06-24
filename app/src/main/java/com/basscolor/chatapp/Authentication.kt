package com.basscolor.chatapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Authentication : Colleague {


    private lateinit var mediator: Mediator
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    fun get_currentUid():String?{
        return if(isLogin()){
            firebaseAuth.currentUser!!.uid
        }else{
            null
        }
    }

    fun isLogin():Boolean {
        return firebaseAuth.currentUser != null
    }

    fun re_isLogin():FirebaseUser? {
        return firebaseAuth.currentUser
    }


//    fun signin(email:String,password: String,signinDelegate: Signin_Delegate){
//
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task: Task<AuthResult> ->
//                if (task.isSuccessful) {
//                    signinDelegate.success()
//                } else {
//                    signinDelegate.error(task.exception)
//                }
//            }
//    }

//    fun login(email:String,password:String){
//
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task :Task<AuthResult>->
//                if (task.isSuccessful) {
//                    setcolleagueSuccess()
//                } else {
//                    setcolleagueError()
//                    Log.e(TAG,"ログイン中にエラーが発生しました"+task.exception)
//                }
//            }
//    }

    override fun setMediator(mediator: Mediator) {
        this.mediator = mediator
    }

    override fun setColleagueSuccess(): Colleague {
        return this
    }

    override fun setColleagueFailure(): Colleague {
        return this
    }

    override fun setColleagueError(): Colleague {
        return this
    }


//
//    interface Signin_Delegate{
//
//        fun success()
//
//        fun error(exception: Exception?)
//    }
//    interface Login_Delegate{
//
//        fun success()
//
//        fun error(exception: Exception?)
//    }

}