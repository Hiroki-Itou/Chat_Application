package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout

class SigninActivity : Activity() {


    private lateinit var signinController: SigninActivityListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val signinButton = findViewById<Button>(R.id.signin_button)
        val nameText = findViewById<EditText>(R.id.username_editText)
        val emailText = findViewById<EditText>(R.id.mail_edittext)
        val passwordText = findViewById<EditText>(R.id.password_edittext)
        val confirmationPassText = findViewById<EditText>(R.id.confirmation_pass_edittext)
        val progressView = findViewById<ConstraintLayout>(R.id.progressView)
        progressView.visibility = android.widget.ProgressBar.INVISIBLE

        signinController = SigninActivityController(this)

        signinButton.setOnClickListener {
            signinController.onSignIn()
        }
        nameText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.onInputUserName(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        emailText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.onInputMailAddress(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        passwordText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.onInputPassword(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        confirmationPassText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.onInputConfirmationPass(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }
}