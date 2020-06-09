package com.basscolor.chatapp

interface Login_Activity_Listener {

    fun login_Check()
    fun get_mailAddress(email:String)
    fun get_password(password:String)
    fun log_in()
    fun to_user_registration_screen()

}