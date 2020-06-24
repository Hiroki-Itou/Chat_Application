package com.basscolor.chatapp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Signin :Colleague{

    private lateinit var mediator: Mediator

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

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun SigninToChat(email:String,password: String){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    mediator.colleagueSuccess(this)
                } else {
                    Log.d(TAG,"サインイン中にエラーが発生しました"+task.exception)
                    mediator.colleagueError(this)
                }
            }
    }
}