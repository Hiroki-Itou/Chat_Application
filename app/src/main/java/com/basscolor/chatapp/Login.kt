package com.basscolor.chatapp

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Login : Colleague{

    private lateinit var mediator: Mediator
    private val firebaseAuth = FirebaseAuth.getInstance()

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


    fun loginToChat(email:String,password:String){

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task : Task<AuthResult> ->
                if (task.isSuccessful) {
                    mediator.colleagueSuccess(this)
                } else {
                    mediator.colleagueError(this)
                    Log.e(ContentValues.TAG,"ログイン中にエラーが発生しました"+task.exception)
                }
            }
    }

}