package com.basscolor.chatapp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.io.Serializable
import java.text.SimpleDateFormat
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class ChatRoom (val document:Map<String, Any>):Serializable{

//    fun sendMessage(message:String){
//
//        val currentUser = FirebaseAuth.getInstance().currentUser!!
//
//        val message = hashMapOf("userID" to currentUser.uid ,"message" to message , "date" to Timestamp(Date()))
//        firestore!!.collection("chatrooms").document(document["roomID"] as String).collection("messages").document().set(message).addOnSuccessListener {
//            Log.d(TAG,"メッセージを送信しました")
//        }.addOnFailureListener { e->
//            Log.e(TAG,"メッセージの送信でエラーが発生しました"+e)
//        }
//    }
//
//    fun receiveMessage(){
//        firestore = FirebaseFirestore.getInstance()
//        firestore!!.collection("chatrooms").document(document["roomID"] as String).collection("messages").orderBy("date", Query.Direction.ASCENDING)
//            .addSnapshotListener{values,e ->
//
//            if(values == null)return@addSnapshotListener
//
//            if(values.isEmpty){
//                Log.d(TAG,"取得したmessageは空です")
//                return@addSnapshotListener
//            }
//            interfase?.receive(values)
//        }
//    }
}