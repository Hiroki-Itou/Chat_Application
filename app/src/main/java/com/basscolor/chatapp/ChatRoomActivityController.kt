package com.basscolor.chatapp

import android.app.Activity
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.core.view.size
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat

class ChatRoomActivityController(override val activity: Activity, override val chatroom: Chatroom, override val listView: ListView) :ChatRoomActivityListener ,receiveMessageListener{

    private var message : String = ""
    init {
        chatroom.interfase = this
        chatroom.receiveMessage()
    }

    override fun receive(snapshots: QuerySnapshot) {
        val roomList: MutableList<MutableMap<String,Any>> = mutableListOf()

        for (snapshot in snapshots) {
            val message = snapshot["message"] as String
            val sdf = SimpleDateFormat("MM-dd HH:mm")
            val d = snapshot["date"] as Timestamp
            val timeStamp = sdf.format(d.toDate())


            val roomData = mutableMapOf("userIcon" to R.drawable.icon,"messageView" to message,"timeStamp" to timeStamp)
            roomList.add(roomData)
        }

        val from = arrayOf("userIcon","messageView","timeStamp")
        val to = intArrayOf(R.id.left_userIcon,R.id.left_messageView,R.id.left_timeStamp)
        val adapter = SimpleAdapter(activity,roomList,R.layout.left_speech_bubble,from,to)
        listView.adapter = adapter

        listView.setSelection(listView.count-1)
    }


    override fun onInputMessage(message: String) {
        this.message = message
    }
    override fun sendMessage() {
        if(message == "") return

        chatroom.toSpeak(message)
    }




}