package com.basscolor.chatapp.Controller

import android.app.Activity
import android.opengl.Visibility
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Listener.VideocallActivityListener
import com.basscolor.chatapp.Model.CallData
import com.basscolor.chatapp.Model.SkywayBridhe
import com.basscolor.chatapp.R
import io.skyway.Peer.Browser.Canvas

class VideocallActivityController(override val activity: Activity) :VideocallActivityListener{

    private val skywayBridhe: SkywayBridhe

    init {
        val localStreamView =  activity.findViewById<Canvas>(R.id.localStreamView)
        localStreamView.setZOrderOnTop(true)
        val remoteStreamView = activity.findViewById<Canvas>(R.id.remoteStreamView)
        localStreamView.setZOrderOnTop(false)
        skywayBridhe = SkywayBridhe(activity, localStreamView, remoteStreamView)
    }

    override fun toHangUp() {
        skywayBridhe.destroy()
        activity.finish()
    }

}