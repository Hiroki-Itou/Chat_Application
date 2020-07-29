package com.basscolor.chatapp.Activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.SearchView
import com.basscolor.chatapp.R
import com.basscolor.chatapp.Controller.UserSearchActivityController
import com.basscolor.chatapp.Listener.UserSearchActivityListener

class UserSearchActivity : Activity() {

    private lateinit var friendSearchController : UserSearchActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_search)

        friendSearchController = UserSearchActivityController(this)

        val addFriendButton = findViewById<Button>(R.id.Add_Friend)
        addFriendButton.setOnClickListener {
            friendSearchController.addRoom()
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return if(query != null){
                    friendSearchController.search(query)

                    val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(this@UserSearchActivity.currentFocus?.windowToken,0)
                    true
                }else{
                    false
                }
            }
            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }
}