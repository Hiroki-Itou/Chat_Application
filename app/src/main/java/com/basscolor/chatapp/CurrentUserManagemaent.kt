package com.basscolor.chatapp

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class CurrentUserManagemaent : Colleague{

    private lateinit var mediator: Mediator
    private lateinit var currentUser :UserContainer
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

    fun getUserContainer():UserContainer{
        return currentUser
    }


    fun userDataAcquisition(uid:String){

        firestore.collection("users").document(uid).get().addOnSuccessListener { result ->
            currentUser = UserContainer(result.data!!)
            mediator.colleagueSuccess(this)

        }.addOnFailureListener { e->
            Log.e(ContentValues.TAG,""+e)
            mediator.colleagueError(this)
        }
    }


}