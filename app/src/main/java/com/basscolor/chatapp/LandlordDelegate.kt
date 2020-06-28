package com.basscolor.chatapp

interface LandlordDelegate {

    fun readInformationSuccess(chatrooms:ArrayList<Chatroom>)
    fun readInformationFailure()
    fun readInformationError(e:Exception)



}