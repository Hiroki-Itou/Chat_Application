package com.basscolor.chatapp.Activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import com.basscolor.chatapp.R
import com.basscolor.chatapp.Controller.UserSearchActivityController
import com.basscolor.chatapp.Listener.UserSearchActivityListener
import com.basscolor.chatapp.Model.setOnDelayClickListener

class UserSearchActivity : Activity() {

    private lateinit var friendSearchController : UserSearchActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_search)

        friendSearchController = UserSearchActivityController(this)

        val addFriendButton = findViewById<Button>(R.id.Add_Friend)
        addFriendButton.setOnDelayClickListener{
            friendSearchController.addRoom()
        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnDelayClickListener {
            finish()
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


