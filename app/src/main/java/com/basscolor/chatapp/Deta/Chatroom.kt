package com.basscolor.chatapp.Deta

import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable
import kotlin.collections.HashMap

class Chatroom (val document:Map<String, Any>):Serializable{


    fun getRoomID():String{
        return document["roomID"] as String
    }
    fun getDoorMessagePlate():String{
        return document["doorMessagePlate"] as String
    }
    fun getPeerUserID():String{
        val userIDs = document["userIDs"] as ArrayList<*>
        return if(FirebaseAuth.getInstance().currentUser!!.uid == userIDs[0]){
            userIDs[1] as String
        }else{
            userIDs[0] as String
        }
    }
    fun getPeerUserName():String{
        val userNames = document["userNames"] as ArrayList<*>
        return if(FirebaseAuth.getInstance().currentUser!!.uid == userNames[0]){
            userNames[1] as String
        }else{
            userNames[0] as String
        }
    }


}