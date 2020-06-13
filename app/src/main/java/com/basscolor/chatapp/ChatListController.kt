package com.basscolor.chatapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.ListView
import android.widget.SimpleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class ChatListController(override val activity: Activity, override var listView: ListView) :ChatListActivityListener {


    private val TAG = this.toString()

    private val _firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun load_ChatList() {

        _firestore.collection("users").document(_firebaseAuth.uid.toString()).collection("chatrooms").get().addOnSuccessListener{ result ->
            val users = ArrayList<User>()
            val roomList: MutableList<MutableMap<String,Any>> = mutableListOf()

            for (doc in result) {
                val user = User(doc.data)
                 users.add(user)
            }
            users.forEach Loop@{user ->
                Log.d(TAG, user.get_name()+"/"+user.get_email() +"/"+user.get_uid())
                val room = mutableMapOf("imageView" to R.drawable.icon,"name" to user.get_name(), "last_message" to "チャットルームが作成されました")

                roomList.add(room)
            }

            val from = arrayOf("imageView","name", "last_message")
            val to = intArrayOf(R.id.imageView,R.id.friend_name, R.id.last_message)
            val adapter = SimpleAdapter(activity,roomList,R.layout.list_items,from,to)
            listView.adapter = adapter

        }.addOnFailureListener{e ->
            Log.w(TAG, "チャットリストの取得に失敗しました", e)
        }

//        _firestore.collection("users").addSnapshotListener{values,e ->
//         //リアルタイム取得方法
//        }
    }

    override fun add_Chatlist() {
        activity.startActivity(Intent(activity, FriendSearchActivity::class.java))

    }

    override fun select_ChatList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logOut() {

        _firebaseAuth.signOut()
        activity.finish()
    }

    override fun reload() {

        load_ChatList()
    }

}