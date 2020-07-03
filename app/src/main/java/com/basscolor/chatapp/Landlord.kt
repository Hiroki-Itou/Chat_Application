package com.basscolor.chatapp

import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class Landlord(val userID:String,val delegate:LandlordDelegate) {

    private val firestore = FirebaseFirestore.getInstance()


    fun loadChatroomListInformation(){

        firestore.collection("chatrooms").whereArrayContains("listID",userID).get().addOnSuccessListener {result ->
            val containers = ArrayList<ChatRoom>()

            if(result.isEmpty){
                delegate.readInformationFailure()
                return@addOnSuccessListener
            }

            for (doc in result) {
                val container = ChatRoom(doc.data)
                containers.add(container)
            }
            delegate.readInformationSuccess(containers)
        }.addOnFailureListener {e ->
            delegate.readInformationError(e)
        }

    }



}