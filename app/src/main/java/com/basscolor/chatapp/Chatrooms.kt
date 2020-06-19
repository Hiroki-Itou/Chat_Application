package com.basscolor.chatapp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.util.ArrayList

class Chatrooms(val uid:String) {

    private val firestore = FirebaseFirestore.getInstance()

    fun load_Chatrooms(load_Chatrooms_Delegate:Load_Chatrooms_Delegate){
        firestore.collection("users").document(uid).collection("chatrooms").get().addOnSuccessListener { result ->
            val containers = ArrayList<UserContainer>()

            for (doc in result) {
                val container = UserContainer(doc.data)
                containers.add(container)
            }
            load_Chatrooms_Delegate.success(containers)

        }.addOnFailureListener { e ->
            load_Chatrooms_Delegate.error(e)
            Log.w(TAG, "チャットリストの取得に失敗しました", e)
        }
    }
    interface Load_Chatrooms_Delegate{

        fun success(containers:ArrayList<UserContainer>)
        fun error(exception: Exception)
    }


}