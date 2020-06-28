package com.basscolor.chatapp

import android.app.Activity
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_login.*

class LoadingIndicator(val activity:Activity) {

    init {
        progress(false)
    }

    fun start(){
        progress(true)
    }


    fun stop(){
        progress(false)
    }

    private fun progress(switch:Boolean){

        if(switch){
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            activity.progressView.visibility = View.VISIBLE
        }else{
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            activity.progressView.visibility = View.INVISIBLE
        }
    }
}