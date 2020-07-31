package com.basscolor.chatapp.Controller

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Toast
import com.basscolor.chatapp.Deta.UserData
import com.basscolor.chatapp.Model.FireBase.ChatroomDatabase
import com.basscolor.chatapp.Model.FireBase.UserDatabase
import com.basscolor.chatapp.Listener.UserSearchActivityListener
import kotlinx.android.synthetic.main.activity_friend_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserSearchActivityController(override val activity: Activity) : UserSearchActivityListener{

    private lateinit var searchUser : UserData

    override fun search(userName:String) {
        val userDatabase = UserDatabase()
        CoroutineScope(Dispatchers.Main).launch{
            try {
                val log : String
                val userData = withContext(Dispatchers.IO){userDatabase.nameSearch(userName)}
                if(userData != null){
                    searchUser = userData
                    log = "入力したユーザーが見つかりました"
                    activity.Friend_Name.text = searchUser.getName()
                    activity.Friend_View.visibility = View.VISIBLE
                }else{
                    log = "入力したユーザーは見つかりませんでした"
                    Toast.makeText(activity,log,Toast.LENGTH_LONG).show()
                    activity.Friend_View.visibility = View.INVISIBLE
                }
                Log.d(TAG, log)
            }catch (e :Exception){
                val message = "検索に失敗しました"
                Log.e(TAG, "$message:$e")
                Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun addRoom() {
        val chatroomDatabase = ChatroomDatabase()
        val roomData = chatroomDatabase.makeRoomData(searchUser)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val log = chatroomDatabase.registration(roomData)
                Log.d(TAG, log)
                withContext(Dispatchers.Main) { activity.finish() }
            } catch (e: Exception) {
                Log.e(TAG, "ルーム作成に失敗しました ", e)
            }
        }
    }
}