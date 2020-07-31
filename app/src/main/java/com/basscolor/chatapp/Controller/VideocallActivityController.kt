package com.basscolor.chatapp.Controller

import android.app.Activity
import com.basscolor.chatapp.Listener.VideocallActivityListener
import com.basscolor.chatapp.Model.SkywayBridhe
import com.basscolor.chatapp.R
import io.skyway.Peer.Browser.Canvas

class VideocallActivityController(override val activity: Activity, override val action: String) :VideocallActivityListener{

    private val skywayBridhe: SkywayBridhe


    init {
        val localStreamView =  activity.findViewById<Canvas>(R.id.localStreamView)
        val remoteStreamView = activity.findViewById<Canvas>(R.id.remoteStreamView)
        skywayBridhe = SkywayBridhe(activity, localStreamView, remoteStreamView)
        if(action == "CALL"){
            skywayBridhe.call()
        }
    }

    override fun toHangUp() {
        skywayBridhe.hangUp()
    }

}