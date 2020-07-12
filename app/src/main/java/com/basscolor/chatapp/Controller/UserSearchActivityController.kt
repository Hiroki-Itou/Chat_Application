package com.basscolor.chatapp.Controller

import android.app.Activity
import android.util.Log
import android.view.View
import com.basscolor.chatapp.Deta.UserData
import com.basscolor.chatapp.Model.FireBase.ChatroomDatabase
import com.basscolor.chatapp.Model.FireBase.UserDatabase
import com.basscolor.chatapp.Listener.UserSearchActivityListener
import kotlinx.android.synthetic.main.activity_friend_search.*

class UserSearchActivityController(override val activity: Activity) : UserSearchActivityListener {

    private val TAG = this.toString()
    private lateinit var searchUser : UserData

    override fun search(userName:String){

        val userDatabase = UserDatabase()
        userDatabase.nameSearch(userName,
            {u-> Log.d(TAG,"入力したユーザーが見つかりました"+u.getName())
                searchUser = u
                activity.Friend_Name.text = searchUser.getName()
                activity.Friend_View.visibility = View.VISIBLE
            },{s->
                Log.d(TAG,s)
                activity.Friend_View.visibility = View.INVISIBLE
            },{e->
                activity.Friend_View.visibility = View.INVISIBLE
                Log.d(TAG,"検索に失敗しました ",e)
            })
    }

    override fun addRoom() {

        val chatroomDatabase = ChatroomDatabase()
        val roomData = chatroomDatabase.makeRoomData(searchUser)

        chatroomDatabase.registration(roomData,
            {s->
                Log.d(TAG,s)
                activity.finish()
            },{e->
                Log.d(TAG,"ルーム作成に失敗しました ",e)
            })
    }
}