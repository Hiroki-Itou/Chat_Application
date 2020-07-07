package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.ListView
import android.widget.SimpleAdapter
import com.basscolor.chatapp.*
import com.basscolor.chatapp.Activity.ChatRoomActivity
import com.basscolor.chatapp.Activity.UserSearchActivity
import com.basscolor.chatapp.FireBase.ChatroomDatabase
import com.basscolor.chatapp.Listener.ChatRoomListActivityListener
import com.google.firebase.auth.FirebaseAuth
import kotlin.Exception
import kotlin.collections.ArrayList

class ChatRoomListActivityController(override val activity: Activity, override var listView: ListView) : ChatRoomListActivityListener {



    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun loadChatList() {


        val chatroomDatabase = ChatroomDatabase()
        chatroomDatabase.loadChatroomList(
            {list->
                displayChatroomList(list)
            },{s->
                Log.d(TAG, s)
            },{e->
                Log.e(TAG, "チャットリストの取得に失敗しました", e)
            })

        //landlord.loadChatroomListInformation()


    }

    private fun displayChatroomList(chatRooms: ArrayList<ChatRoom>){
        val roomList: MutableList<MutableMap<String,Any?>> = mutableListOf()

        val currentName = firebaseAuth.currentUser!!.displayName

        chatRooms.forEach Loop@{room ->

            val userNames = room.document["userNames"] as ArrayList<String>
            val roomName :String
            roomName = if(userNames[0] == currentName){
                userNames[1]
            }else{
                userNames[0]
            }

            val roomData = mutableMapOf("imageView" to R.drawable.ic_user,"roomName" to roomName, "doorMessagePlate" to room.document["doorMessagePlate"], "class" to room)
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

        val chatroom = mutableMap["class"] as ChatRoom

        val intent = Intent(activity, ChatRoomActivity::class.java)
        intent.putExtra("chatRoom",chatroom)
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