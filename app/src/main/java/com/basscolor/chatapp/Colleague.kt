package com.basscolor.chatapp

interface Colleague {

    fun setMediator(mediator:Mediator)
    fun setColleagueSuccess():Colleague
    fun setColleagueFailure():Colleague
    fun setColleagueError():Colleague
}