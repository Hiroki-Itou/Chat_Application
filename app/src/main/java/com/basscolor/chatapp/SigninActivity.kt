package com.basscolor.chatapp

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_signin.*

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

        signinController = SigninController(progressView,this)

        signinButton.setOnClickListener {
            signinController.sign_in()
        }
        nameText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.get_userName(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        emailText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.get_mailAddress(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        passwordText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.get_password(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        confirmationPassText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
                signinController.get_confirmationPass(p.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }
}