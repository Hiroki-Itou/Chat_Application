package com.basscolor.chatapp.Activity

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.basscolor.chatapp.Controller.LoginActivityController
import com.basscolor.chatapp.Listener.LoginActivityListener
import com.basscolor.chatapp.Model.setOnDelayClickListener
import com.basscolor.chatapp.R

class LoginActivity : Activity() {

    private lateinit var _login_Controller : LoginActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val accountCreationButton = findViewById<Button>(R.id.account_creation_button)
        val loginButton = findViewById<Button>(R.id.login_button)
        val emailText = findViewById<EditText>(R.id.mail_edit_text)
        val passwordText = findViewById<EditText>(R.id.password_edit_text)

        _login_Controller = LoginActivityController(this)

        emailText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p: Editable?) {
                _login_Controller.onInputMailAddress(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        passwordText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p: Editable?) {
                _login_Controller.onInputPassword(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        loginButton.setOnDelayClickListener( {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            _login_Controller.onLogIn()
        })

        accountCreationButton.setOnDelayClickListener ({
            _login_Controller.toUserRregistration()
        })
    }

    override fun onStart() {
        super.onStart()
        _login_Controller.loginCheck()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroy",this.localClassName+"は破壊されました")
    }
}
