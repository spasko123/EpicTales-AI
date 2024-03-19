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
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {

    lateinit var edtxEmailAddress: EditText
    lateinit var edtxUsername: EditText
    lateinit var edtxPassword: EditText
    lateinit var edtxRepeatPassword: EditText
    lateinit var btnRegister: Button
    lateinit var txtLogIn: TextView
    lateinit var txtContinueAsGuest: TextView
    var email: String = ""
    var username: String = ""
    var password: String = ""
    var repeatedPassword: String = ""
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }

        auth = FirebaseAuth.getInstance()

        edtxEmailAddress = findViewById(R.id.edtxEmailAddress)
        edtxUsername = findViewById(R.id.edtxUsername)
        edtxPassword = findViewById(R.id.edtxPassword)
        edtxRepeatPassword = findViewById(R.id.edtxRepeatPassword)
        btnRegister = findViewById(R.id.btnRegister)
        txtLogIn = findViewById(R.id.txtLogIn)
        txtContinueAsGuest = findViewById(R.id.txtContinueAsGuest)

        txtLogIn.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener(){
            email = edtxEmailAddress.text.toString().trim()
            if(email.isEmpty() || !email.contains('@')){
                edtxEmailAddress.error = "Please provide valid email address"
                return@setOnClickListener
            }
            username = edtxUsername.text.toString().trim()
            if(username.length < 6){
                edtxUsername.error = "Username must be at least 6 characters long"
                return@setOnClickListener
            }
            password = edtxPassword.text.toString().trim()
            if(password.length < 6){
                edtxPassword.error = "Password must be at least 6 characters long"
                return@setOnClickListener
            }
            repeatedPassword = edtxRepeatPassword.text.toString().trim()
            if(repeatedPassword != password){
                edtxRepeatPassword.error = "The repeated password doesn't match the first password"
                return@setOnClickListener
            }
            registerUser()
        }

    }



    private fun registerUser() {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = auth.currentUser
                                val uid = firebaseUser?.uid


                                val user = hashMapOf(
                                    "email" to email,
                                    "username" to username
                                )

                                if (uid != null) {
                                    db.collection("users")
                                        .document(uid)
                                        .set(user)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "Registration successful.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(this, LoginActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "Failed to save user data: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            } else {
                                Toast.makeText(
                                    baseContext, "Registration failed. ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {

                    Toast.makeText(
                        baseContext, "Username '$username' is already taken. Please choose a different one.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->

                Toast.makeText(
                    this@RegisterActivity,
                    "Failed to check username existence: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}