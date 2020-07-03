package com.basscolor.chatapp

interface LandlordDelegate {

    fun readInformationSuccess(chatrooms:ArrayList<ChatRoom>)
    fun readInformationFailure()
    fun readInformationError(e:Exception)



}