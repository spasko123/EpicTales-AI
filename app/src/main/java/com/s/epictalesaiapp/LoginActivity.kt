package com.s.epictalesaiapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var txtRegister: TextView
    lateinit var edtxEmailAddress: EditText
    lateinit var edtxPassword: EditText
    lateinit var btnSignIn: Button
    var email: String = ""
    var password: String = ""
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtRegister = findViewById(R.id.txtRegister)
        edtxEmailAddress = findViewById(R.id.edtxEmailAddress)
        edtxPassword = findViewById(R.id.edtxPassword)
        btnSignIn = findViewById(R.id.btnSignIn)

        auth = FirebaseAuth.getInstance()

        txtRegister.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener(){
            email = edtxEmailAddress.text.toString().trim()
            if(email.isEmpty() || !email.contains('@')){
                edtxEmailAddress.error = "Please provide a valid email address"
                return@setOnClickListener
            }
            password = edtxPassword.text.toString().trim()
            if(password.isEmpty()){
                edtxPassword.error = "Please provide a password!"
                return@setOnClickListener
            }
            signIn(email, password)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }
    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "There is no registered account with the provided data", Toast.LENGTH_LONG).show()
//                    Toast.makeText(
//                        baseContext, "Login failed. ${task.exception?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
    }
}
