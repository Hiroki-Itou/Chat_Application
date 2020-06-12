package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout

class LoginActivity : Activity() {

    private lateinit var _login_Controller : LoginActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val accountCreationButton = findViewById<Button>(R.id.account_creation_button)
        val loginButton = findViewById<Button>(R.id.login_button)
        val emailText = findViewById<EditText>(R.id.mail_edit_text)
        val passwordText = findViewById<EditText>(R.id.password_edit_text)
        val progressView = findViewById<ConstraintLayout>(R.id.progressView)
        progressView.visibility = android.widget.ProgressBar.INVISIBLE

        _login_Controller = LoginController(this)

        emailText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p: Editable?) {
                _login_Controller.get_mailAddress(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        passwordText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p: Editable?) {
                _login_Controller.get_password(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        loginButton.setOnClickListener {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            _login_Controller.log_in()
        }

        accountCreationButton.setOnClickListener {
            _login_Controller.to_user_registration_screen()
        }
    }

    override fun onStart() {
        super.onStart()
        _login_Controller.login_Check()
    }


}