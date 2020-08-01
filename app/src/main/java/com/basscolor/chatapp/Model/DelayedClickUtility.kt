package com.basscolor.chatapp.Model

import android.media.Image
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView


fun Button.setOnDelayClickListener(action:()->Unit, delayMillis:Long){
    this.setOnClickListener {
        this.isEnabled = false
        Handler().postDelayed({
            kotlin.run { this.isEnabled = true }
        },delayMillis)
        action()
    }
}
fun ImageButton.setOnDelayClickListener(action:()->Unit, delayMillis:Long){
    this.setOnClickListener {
        this.isEnabled = false
        Handler().postDelayed({
            kotlin.run { this.isEnabled = true }
        },delayMillis)
        action()
    }
}

fun Button.setOnDelayClickListener(action:()->Unit){
    this.setOnDelayClickListener ({ action() },3000L)
}

fun ImageButton.setOnDelayClickListener(action:()->Unit){
    this.setOnDelayClickListener ({ action() },3000L)
}

fun ListView.setOnDelayItemClickListener(action:(AdapterView<*>, View, Int, Long )->Unit){

    this.setOnItemClickListener { parent, view, position, id ->
        this.isEnabled = false
        Handler().postDelayed({
            kotlin.run { this.isEnabled = true }
        },1500L)
        action(parent,view,position,id)
    }
}