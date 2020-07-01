package com.basscolor.chatapp

import android.app.Activity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_friend_search.*
import kotlin.Exception

class UserSearchActivityController(override val activity: Activity) :UserSearchActivityListener,ChatRoomBuilderDelegate,UserSearcherDelegate{

    private val TAG = this.toString()
    private lateinit var searchUser : UserData
    private val searcher = UserSearcher(this)

    override fun search(userName:String){

        searcher.nameSearch(userName)
    }


    override fun found(userData: UserData) {
        Log.d(TAG,"入力したユーザーが見つかりました"+userData.getName())
        searchUser = userData
        activity.Friend_Name.text = searchUser.getName()
        activity.Friend_View.visibility = View.VISIBLE
    }

    override fun souldNotFound() {
        Log.d(TAG,"入力したユーザーは見つかりませんでした")
        activity.Friend_View.visibility = View.INVISIBLE
    }

    override fun searchError(e: Exception) {
        activity.Friend_View.visibility = View.INVISIBLE
        Log.d(TAG,"検索に失敗しました ",e)
    }



    override fun BuildSuccess() {
        activity.finish()
    }

    override fun BuildError(e: Exception) {
        Log.d(TAG,"ルーム作成に失敗しました ",e)
    }

    override fun addRoom() {

        val roomBuilder = ChatRoomBuilder(this)
        val roomData = roomBuilder.makeRoomData(searchUser)
        roomBuilder.build(roomData)

    }



}