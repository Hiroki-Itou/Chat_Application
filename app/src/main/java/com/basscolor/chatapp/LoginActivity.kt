package com.basscolor.chatapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class LoginActivity : Activity() {


    val _login_Controller = Login_Controller()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val accountCreationButton = findViewById<Button>(R.id.account_creation_button)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            _login_Controller.log_in()
            val _progressView = findViewById<ConstraintLayout>(R.id.progressView)
            _progressView.visibility = android.widget.ProgressBar.VISIBLE

        }

        accountCreationButton.setOnClickListener {
            _login_Controller.to_user_registration_screen()

            val intent = Intent(this,SigninActivity::class.java)
            startActivity(intent)
        }


    }



}