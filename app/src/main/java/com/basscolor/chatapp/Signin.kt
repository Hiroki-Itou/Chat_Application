package com.basscolor.chatapp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

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

    fun SigninToChat(email:String,password: String,userName:String){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    setProfile(userName)
                } else {
                    Log.d(TAG,"サインイン中にエラーが発生しました"+task.exception)
                    mediator.colleagueError(this)
                }
            }
    }

    private fun setProfile(userName:String){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .build()
        currentUser!!.updateProfile(request).addOnSuccessListener {
            mediator.colleagueSuccess(this)
        }.addOnFailureListener { e->
            mediator.colleagueError(this)
            Log.e(TAG, "プロフィールセット中にエラーが発生しました ", e)
        }
    }
}