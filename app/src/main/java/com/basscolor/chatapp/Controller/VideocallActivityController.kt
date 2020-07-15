package com.basscolor.chatapp.Controller

import android.app.Activity
import android.opengl.Visibility
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.basscolor.chatapp.Deta.Chatroom
import com.basscolor.chatapp.Listener.VideocallActivityListener
import com.basscolor.chatapp.Model.SkywayBridhe
import com.basscolor.chatapp.R
import io.skyway.Peer.Browser.Canvas

class VideocallActivityController(override val activity: Activity, override val peerUserID: String) :VideocallActivityListener{



    private val skywayBridhe: SkywayBridhe
    private val callCatchView : ConstraintLayout

    init {
        val localStreamView =  activity.findViewById<Canvas>(R.id.localStreamView)
        val remoteStreamView = activity.findViewById<Canvas>(R.id.remoteStreamView)
        skywayBridhe = SkywayBridhe(activity, localStreamView, remoteStreamView,peerUserID)
        skywayBridhe.checkPermission()

        callCatchView = activity.findViewById<ConstraintLayout>(R.id.CallCatchView)
        skywayBridhe.setReceiver{ callCatchView.visibility = View.VISIBLE }
    }


    override fun toVideoSetup() {
        skywayBridhe.setupPeer()
    }

    override fun toCall() {
        skywayBridhe.call({

        },{

        },{

        })
    }

    override fun toAnswer() {
        skywayBridhe.answer()
    }

    override fun toHangUp() {
        skywayBridhe.hangUp {
            callCatchView.visibility = View.INVISIBLE
        }
    }
    override fun todestroy() {
        skywayBridhe.destroy()
    }


}