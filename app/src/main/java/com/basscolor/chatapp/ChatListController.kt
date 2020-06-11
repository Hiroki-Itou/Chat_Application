package com.basscolor.chatapp

import android.app.Activity
import android.util.Log
import android.widget.ListView
import android.widget.SimpleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class ChatListController(override val activity: Activity, override var listView: ListView) :ChatListActivityListener {


    private val TAG = this.toString()
    val users = ArrayList<User>()
    private val _firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val room_list: MutableList<MutableMap<String,Any>> = mutableListOf()

    override fun load_ChatList() {

        _firestore.collection("users").get().addOnSuccessListener{ result ->
            for (doc in result) {
                val user = User(doc.data)
                users.add(user)
            }
            users.forEach Loop@{user ->
                Log.d(TAG, user.get_name()+"/"+user.get_email() +"/"+user.get_lastMessage())
                val room = mutableMapOf("imageView" to R.drawable.icon,"name" to user.get_name(), "last_message" to user.get_lastMessage())
                room_list.add(room)
            }
            val from = arrayOf("imageView","name", "last_message")
            val to = intArrayOf(R.id.imageView,R.id.friend_name, R.id.last_message)
            val adapter = SimpleAdapter(activity,room_list,R.layout.list_items,from,to)
            listView.adapter = adapter

        }.addOnFailureListener{e ->
            Log.w(TAG, "チャットリストの取得に失敗しました", e)
        }

//        _firestore.collection("users").addSnapshotListener{values,e ->
//         //リアルタイム取得方法
//        }
    }

    override fun add_Chatlist() {


    }

    override fun select_ChatList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logOut() {

        _firebaseAuth.signOut()
        activity.finish()
    }
}