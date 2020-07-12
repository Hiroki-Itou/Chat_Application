package com.basscolor.chatapp.Controller

import android.app.Activity
import com.basscolor.chatapp.Activity.VideocallActivity
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Listener.VideocallActivityListener
import com.basscolor.chatapp.Model.ViewModel
import com.basscolor.chatapp.R
import io.skyway.Peer.Browser.Canvas

class VideocallActivityController(override val activity: Activity, override val chatroom: Chatroom) :VideocallActivityListener{

    private lateinit var viewModel: ViewModel

    init {

        val localStreamView =  activity.findViewById<Canvas>(R.id.localStreamView)
        val remoteStreamView = activity.findViewById<Canvas>(R.id.remoteStreamView)
        viewModel = ViewModel(activity, localStreamView, remoteStreamView,chatroom)
        viewModel.checkPermission()
    }


    override fun VideoSetup() {
        viewModel.setupPeer()
    }

    override fun Call() {
       viewModel.toCall()
    }

    override fun HangUp() {

    }


}