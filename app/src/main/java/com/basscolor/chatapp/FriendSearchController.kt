package com.basscolor.chatapp

import android.app.Activity
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_friend_search.*

class FriendSearchController(override val activity: Activity) :FriendSearchActivityListener{

    private val _firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun search(userName:String){

       _firestore.collection("users").whereEqualTo("name",userName).get().addOnSuccessListener { result ->

           for (doc in result) {
               val user = User(doc.data)

              Log.d("search",user.get_uid())
               activity.Friend_Name.text = user.get_name()
               activity.Friend_View.visibility = View.VISIBLE
           }

       }.addOnFailureListener { e ->

           Log.d("search","検索に失敗しました ",e)
       }
    }

}