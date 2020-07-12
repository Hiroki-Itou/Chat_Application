package com.basscolor.chatapp.Model.FireBase

import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Deta.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class ChatroomDatabase {

    private val firestore = FirebaseFirestore.getInstance()

    fun registration(chatroom: HashMap<String, Serializable>,success:(String)->Unit,failure:(Exception)->Unit){

        val roomID = chatroom["roomID"] as String
        firestore.collection("chatrooms").document(roomID).set(chatroom).addOnSuccessListener {
           success("チャットルーム情報を登録しました")
        }.addOnFailureListener { e ->
           failure(e)
        }
    }

    fun loadChatroomList(found:(ArrayList<Chatroom>)->Unit, empty:(String)->Unit, failure:(Exception)->Unit ){

        val currentUser = FirebaseAuth.getInstance().currentUser
        firestore.collection("chatrooms").whereArrayContains("userIDs",currentUser!!.uid).get().addOnSuccessListener {result ->
            val chatrooms = ArrayList<Chatroom>()

            if(result.isEmpty){
                empty("チャットルームが見つかりませんでした")
                return@addOnSuccessListener
            }

            for (doc in result) {
                val chatroom = Chatroom(doc.data)
                chatrooms.add(chatroom)
            }
            found(chatrooms)

        }.addOnFailureListener {e ->
            failure(e)
        }
    }

    fun makeRoomData(userData: UserData):HashMap<String,Serializable>{
        val currentUser = FirebaseAuth.getInstance().currentUser

        val names = arrayListOf(userData.getName(),currentUser!!.displayName!!)
        val userIDs = arrayListOf(currentUser.uid,userData.getUserID())

        val data = hashMapOf(
            "userNames" to names,
            "userIDs" to userIDs,
            "roomID" to randomString(),
            "doorMessagePlate" to ""
        )
        return data
    }

    private fun randomString():String{

        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..20)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}