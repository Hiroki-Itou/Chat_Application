package com.basscolor.chatapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class ChatRoomBuilder(val delegate:ChatRoomBuilderDelegate){

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun makeRoomData(userData: UserData):HashMap<String,Serializable>{
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..20)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

        val currentUser = FirebaseAuth.getInstance().currentUser

        val names = arrayListOf(userData.getName(),currentUser!!.displayName!!)
        val listID = arrayListOf(currentUser.uid,userData.getUserID())

        val data = hashMapOf(
            "userNames" to names,
            "listID" to listID,
            "roomID" to randomString,
            "doorMessagePlate" to ""
        )
        return data
    }

    fun build(chatroom: HashMap<String,Serializable>){

        val roomID = chatroom["roomID"] as String
        firestore.collection("chatrooms").document(roomID).set(chatroom).addOnSuccessListener {
            delegate.BuildSuccess()
        }.addOnFailureListener { e ->
            delegate.BuildError(e)

        }
    }
}