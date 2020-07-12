package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.ListView
import android.widget.SimpleAdapter
import com.basscolor.chatapp.*
import com.basscolor.chatapp.Activity.ChatroomActivity
import com.basscolor.chatapp.Activity.UserSearchActivity
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Model.FireBase.ChatroomDatabase
import com.basscolor.chatapp.Listener.ChatroomListActivityListener
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable
import kotlin.collections.ArrayList

class ChatroomListActivityController(override val activity: Activity, override var listView: ListView) : ChatroomListActivityListener {

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun loadChatList() {
        val chatroomDatabase = ChatroomDatabase()
        chatroomDatabase.loadChatroomList({list-> displayChatroomList(list) },{s-> Log.d(TAG, s) },{e-> Log.e(TAG, "チャットリストの取得に失敗しました", e) })
    }

    private fun displayChatroomList(chatRooms: ArrayList<Chatroom>){
        val roomList: MutableList<MutableMap<String,Serializable>> = mutableListOf()

        chatRooms.forEach Loop@{room ->
            val roomData = mutableMapOf("imageView" to R.drawable.ic_user,"roomName" to room.getPeerUserName(), "doorMessagePlate" to room.getDoorMessagePlate(), "class" to room)
            roomList.add(roomData)
        }

        val from = arrayOf("imageView","roomName", "doorMessagePlate")
        val to = intArrayOf(
            R.id.imageView,
            R.id.roomName,
            R.id.doorMessagePlate
        )
        val adapter = SimpleAdapter(activity,roomList,
            R.layout.list_items,from,to)
        listView.adapter = adapter
    }

    override fun addChatlist() {
        activity.startActivity(Intent(activity, UserSearchActivity::class.java))
    }

    override fun selectChatRoom(mutableMap:MutableMap<String,Any>) {

        val chatroom = mutableMap["class"] as Chatroom
        val intent = Intent(activity, ChatroomActivity::class.java)
        intent.putExtra("chatroom",chatroom)
        activity.startActivity(intent)
    }

    override fun logOut() {
        firebaseAuth.signOut()
        activity.finish()
    }

    override fun reload() {
        loadChatList()
    }
}