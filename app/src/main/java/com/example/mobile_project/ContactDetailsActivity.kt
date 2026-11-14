package com.example.mobile_project


import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ContactDetailsActivity : AppCompatActivity() {
    lateinit var txtAvatar: TextView
    lateinit var txtName: TextView
    lateinit var txtPhone: TextView
    lateinit var txtEmail: TextView
    lateinit var switchFavorite: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtAvatar = findViewById<TextView>(R.id.txt_avatar)
        txtName = findViewById<TextView>(R.id.txt_name)
        txtPhone = findViewById<TextView>(R.id.txt_phone)
        txtEmail = findViewById<TextView>(R.id.txt_email)
        switchFavorite = findViewById<Switch>(R.id.switch_favorite)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnCall = findViewById<LinearLayout>(R.id.btn_call)
        val btnSms = findViewById<LinearLayout>(R.id.btn_sms)
        val btnEmail = findViewById<LinearLayout>(R.id.btn_email)
        val btnEdit = findViewById<Button>(R.id.btn_edit)
        val btnDelete = findViewById<Button>(R.id.btn_delete)

    }

}
