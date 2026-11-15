package com.example.mobile_project


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactDetailsActivity : AppCompatActivity() {
    lateinit var txtAvatar: TextView
    lateinit var txtName: TextView
    lateinit var txtPhone: TextView
    lateinit var txtEmail: TextView
    lateinit var switchFavorite: Switch

    lateinit var db: ContactDatabase

    private var contactId = 0
    private var contact: Contact? = null // 3ashan lama screen bt load awel mara msh byb2a fe contact lesa (null)

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

        db = ContactDatabase.getInstance(this)
        contactId = intent.getIntExtra("contactId", 0)

       // load Contact
        loadContact()

        //btnback 3ashan nerga3 lel activity ely ablha
        btnBack.setOnClickListener { finish() }

        //call contact
        btnCall.setOnClickListener {
            val c = contact
            if (c != null) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${c.phone}"))
                startActivity(intent)
            }
        }

        //sms
        btnSms.setOnClickListener {
            val c = contact
            if (c != null) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${c.phone}"))
                startActivity(intent)
            }
        }

        //email contact
        btnEmail.setOnClickListener {
            val c = contact
            if (c != null) {
                if (c.email.isEmpty()) {
                    Toast.makeText(this, "No email for this contact", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${c.email}"))
                    startActivity(intent)
                }

            }
        }

        //explicit intent
        btnEdit.setOnClickListener {
            val c = contact
            if (c != null) {
                val intent = Intent(this, AddEditContactActivity::class.java)
                intent.putExtra("contactId", c.id)
                intent.putExtra("name", c.name)
                intent.putExtra("phone", c.phone)
                intent.putExtra("email", c.email)
                intent.putExtra("isFavorite", c.isFavorite)
                startActivity(intent)
            }
        }

        // deletee contact
        btnDelete.setOnClickListener {
            val c = contact
            if (c != null) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.delete_title)
                    .setMessage(getString(R.string.delete_message, c.name))
                    .setNegativeButton(R.string.btn_cancel, null)
                    .setPositiveButton(R.string.btn_delete) { _, _ ->
                        //law das confirm -> delete
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.contactDao().deleteContact(c)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@ContactDetailsActivity, R.string.contact_deleted, Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    }
                    .show()
            }
        }

        switchFavorite.setOnCheckedChangeListener { _, isChecked ->
            val c = contact
            if (c != null && c.isFavorite != isChecked) {
                val updated = c.copy(isFavorite = isChecked)
                lifecycleScope.launch(Dispatchers.IO) {
                    db.contactDao().updateContact(updated)
                    withContext(Dispatchers.Main) {
                        contact = updated
                        Toast.makeText(this@ContactDetailsActivity, "Favorite updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // ne2ra el contact men Room w show it
    private fun loadContact() {
        lifecycleScope.launch(Dispatchers.IO) {
            val loaded = db.contactDao().getContactById(contactId)
            withContext(Dispatchers.Main) {
                if (loaded == null) {
                    Toast.makeText(this@ContactDetailsActivity, R.string.contact_not_found, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    contact = loaded
                    showContact(loaded)
                }
            }
        }
    }

    private fun showContact(c: Contact) {
        txtAvatar.text = c.getInitials()
        txtName.text = c.name
        txtPhone.text = c.phone
        txtEmail.text = c.email.ifEmpty { getString(R.string.no_email) }
        switchFavorite.isChecked = c.isFavorite
    }
    

    // returning from Edit screen (show the updated data)
    override fun onRestart() {
        super.onRestart()
        loadContact()
    }
}



