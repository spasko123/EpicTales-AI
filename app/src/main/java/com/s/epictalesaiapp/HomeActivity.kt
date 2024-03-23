package com.s.epictalesaiapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    lateinit var txtTest: TextView
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var bottomNavMenu: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        txtTest = findViewById(R.id.txtTest)
        bottomNavMenu = findViewById(R.id.bottomNavMenu)

        bottomNavMenu.selectedItemId = R.id.home

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        val uid = currentUser?.uid

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Set system UI flags to enable layout fullscreen and light status bar
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

            // Set status bar color to transparent
            window.statusBarColor = Color.TRANSPARENT
        }





        if (uid != null) {
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val username = document.getString("username")
                        txtTest.text = username.toString()
                    } else {
//                        Toast.makeText(this, "User document does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to retrieve username: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
