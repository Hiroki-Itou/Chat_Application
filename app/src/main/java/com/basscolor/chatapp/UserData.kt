package com.basscolor.chatapp

class UserData(val dec:Map<String, Any>) {

    fun getEmail():String{
        var email =  dec["email"] as String?
        if (email == null) {
            email = "no_email"
        }
        return email
    }
    fun getName():String{
        var name = dec["name"] as String?
        if (name == null) {
            name = "anknown"
        }
        return name
    }
    fun getUserID():String{
        var uid = dec["userID"] as String?
        if (uid == null) {
            uid = "no_ID"
        }
          return uid
    }

}
