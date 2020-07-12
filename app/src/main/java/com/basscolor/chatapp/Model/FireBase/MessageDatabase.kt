package com.basscolor.chatapp.Model.FireBase

import android.content.ContentValues.TAG
import android.util.Log
import com.basscolor.chatapp.Deta.Chatroom
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class MessageDatabase {

    private val firestore = FirebaseFirestore.getInstance()

    fun sendMessage(chatroom: Chatroom,message:String,success:(String)->Unit ,failure:(Exception)->Unit ){

        val currentUser = FirebaseAuth.getInstance().currentUser!!

        val message = hashMapOf("userID" to currentUser.uid ,"message" to message , "date" to Timestamp(Date()))

        firestore.collection("chatrooms").document(chatroom.getRoomID()).collection("messages").document().set(message).addOnSuccessListener {
            success("メッセージを取得しました")
        }.addOnFailureListener { e->
            failure(e)
        }
    }

    fun receiveMessage(chatroom: Chatroom,receive:(QuerySnapshot)->Unit){

        firestore.collection("chatrooms").document(chatroom.getRoomID()).collection("messages").orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener{values,e ->

                if(values == null)return@addSnapshotListener

                if(values.isEmpty){
                    Log.d(TAG,"取得したmessageは空です")
                    return@addSnapshotListener
                }
                receive(values)
            }
    }
}