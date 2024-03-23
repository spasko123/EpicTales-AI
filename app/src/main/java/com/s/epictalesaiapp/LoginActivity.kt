package com.s.epictalesaiapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var txtRegister: TextView
    lateinit var txtForgotPassword: TextView
    lateinit var edtxEmailAddress: EditText
    lateinit var edtxPassword: EditText
    lateinit var btnSignIn: Button
    lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    var email: String = ""
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtRegister = findViewById(R.id.txtRegister)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        edtxEmailAddress = findViewById(R.id.edtxEmailAddress)
        edtxPassword = findViewById(R.id.edtxPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.GONE

        auth = FirebaseAuth.getInstance()

        txtRegister.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        txtForgotPassword.setOnClickListener{
            email = edtxEmailAddress.text.toString().trim()
            if(email.isEmpty() || !email.contains('@')){
                edtxEmailAddress.error = "Please provide a valid email address!"
                return@setOnClickListener
            }
            resetPassword(email)
        }

        btnSignIn.setOnClickListener(){
            progressBar.visibility = View.VISIBLE
            btnSignIn.visibility = View.INVISIBLE
            email = edtxEmailAddress.text.toString().trim()
            if(email.isEmpty() || !email.contains('@')){
                edtxEmailAddress.error = "Please provide a valid email address"
                progressBar.visibility = View.GONE
                btnSignIn.visibility = View.VISIBLE
                return@setOnClickListener
            }
            password = edtxPassword.text.toString().trim()
            if(password.isEmpty()){
                edtxPassword.error = "Please provide a password!"
                progressBar.visibility = View.GONE
                btnSignIn.visibility = View.VISIBLE
                return@setOnClickListener
            }
            signIn(email, password)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password reset email sent. Check your email inbox.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send reset email. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "We couldn't create account. Try again later", Toast.LENGTH_LONG).show()
//                    Toast.makeText(
//                        baseContext, "Login failed. ${task.exception?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
    }
}
