package com.basscolor.chatapp

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationManagement :Colleague{

    private lateinit var mediator: Mediator
    private val firestore = FirebaseFirestore.getInstance()

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


    fun registration(registData:Map<String,String?>){

        firestore.collection("users")
            .document(registData["uid"]as String)
            .set(registData)
            .addOnSuccessListener {
                mediator.colleagueSuccess(this)
            }
            .addOnFailureListener { e ->
                mediator.colleagueError(this)
                Log.e(ContentValues.TAG, "ユーザー登録中にエラーが発生しました ", e)
            }
    }

}