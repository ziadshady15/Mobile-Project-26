package com.example.mobile_project

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Add Contact screen (Edit mode is added later in the same screen)
class AddEditContactActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var txtNameError: TextView
    private lateinit var txtPhoneError: TextView
    private lateinit var txtEmailError: TextView
    private lateinit var db: ContactDatabase

    private val validator = ContactValidator()
    private var errorsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // ime() = keyboard: keeps the form above the keyboard
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etName = findViewById(R.id.et_name)
        etPhone = findViewById(R.id.et_phone)
        etEmail = findViewById(R.id.et_email)
        txtNameError = findViewById(R.id.txt_name_error)
        txtPhoneError = findViewById(R.id.txt_phone_error)
        txtEmailError = findViewById(R.id.txt_email_error)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        val btnSave = findViewById<Button>(R.id.btn_save)

        db = ContactDatabase.getInstance(this)

        // After rotation: restore what the user typed (Lab 02 hands-on)
        savedInstanceState?.let {
            etName.setText(it.getString("name"))
            etPhone.setText(it.getString("phone"))
            etEmail.setText(it.getString("email"))
            if (it.getBoolean("errorsShown")) {
                validateForm()
            }
        }

        btnSave.setOnClickListener { saveContact() }
        // Cancel and back discard the form
        btnCancel.setOnClickListener { finish() }
        btnBack.setOnClickListener { finish() }
    }

    // Save the form before the activity is destroyed (e.g. rotation) (Lab 02)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", etName.text.toString())
        outState.putString("phone", etPhone.text.toString())
        outState.putString("email", etEmail.text.toString())
        outState.putBoolean("errorsShown", errorsShown)
    }

    // Check every field with ContactValidator; show the red error under each wrong field
    private fun validateForm(): Boolean {
        errorsShown = true
        val nameOk = validator.isValidName(etName.text.toString().trim())
        val phoneOk = validator.isValidPhone(etPhone.text.toString().trim())
        val emailOk = validator.isValidEmail(etEmail.text.toString().trim())

        showFieldError(etName, txtNameError, nameOk)
        showFieldError(etPhone, txtPhoneError, phoneOk)
        showFieldError(etEmail, txtEmailError, emailOk)

        // Put the cursor in the first wrong field
        when {
            !nameOk -> etName.requestFocus()
            !phoneOk -> etPhone.requestFocus()
            !emailOk -> etEmail.requestFocus()
        }
        return nameOk && phoneOk && emailOk
    }

    private fun showFieldError(field: EditText, errorText: TextView, isValid: Boolean) {
        errorText.visibility = if (isValid) View.GONE else View.VISIBLE
        field.setBackgroundResource(if (isValid) R.drawable.bg_input else R.drawable.bg_input_error)
    }

    // Insert the new contact in Room, then go back (Lab 04)
    private fun saveContact() {
        if (!validateForm()) {
            Toast.makeText(this, R.string.fix_errors, Toast.LENGTH_SHORT).show()
            return
        }

        val contact = Contact(
            name = etName.text.toString().trim(),
            phone = validator.cleanPhone(etPhone.text.toString().trim()),
            email = etEmail.text.toString().trim()
        )

        lifecycleScope.launch(Dispatchers.IO) {
            db.contactDao().insertContact(contact)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddEditContactActivity, R.string.contact_saved, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
