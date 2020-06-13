package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView

class FriendSearchActivity : Activity() {

    private lateinit var friendSearchController :FriendSearchActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_search)

        friendSearchController = FriendSearchController(this)

        val addFriendButton = findViewById<Button>(R.id.Add_Friend)
        addFriendButton.setOnClickListener {
            friendSearchController.addRoom()
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {


                return if(query != null){
                    friendSearchController.search(query)

                    true
                }else{
                    false
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true

            }
        })


    }
}