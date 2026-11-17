package com.example.mobile_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Contact List screen (start screen of the app)
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var listView: ListView
    private lateinit var etSearch: EditText
    private lateinit var switchFavorites: Switch
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var txtEmptyTitle: TextView
    private lateinit var txtEmptyMessage: TextView
    private lateinit var db: ContactDatabase
    private lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: ContactAdapter

    // The contacts currently shown in the list (filled by loadContacts)
    private val contacts = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lst_contacts)
        etSearch = findViewById(R.id.et_search)
        switchFavorites = findViewById(R.id.switch_favorites)
        layoutEmpty = findViewById(R.id.layout_empty)
        txtEmptyTitle = findViewById(R.id.txt_empty_title)
        txtEmptyMessage = findViewById(R.id.txt_empty_message)
        db = ContactDatabase.getInstance(this)
        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Bind adapter to ListView; the star button calls toggleFavorite (lambda callback)
        adapter = ContactAdapter(this, contacts) { contact -> toggleFavorite(contact) }
        listView.adapter = adapter

        // Item click: open the Details screen with an explicit intent (Lab 03 / Lab 04)
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = contacts[position]
            val intent = Intent(this, ContactDetailsActivity::class.java)
            intent.putExtra("contactId", selectedContact.id)
            startActivity(intent)
        }

        // "+ Add" opens the empty form (explicit intent, Lab 03)
        val btnAdd = findViewById<Button>(R.id.btn_add)
        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddEditContactActivity::class.java))
        }

        // About icon opens the Compose About screen
        val btnAbout = findViewById<ImageButton>(R.id.btn_about)
        btnAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        // Restore the last "Favorites only" choice from SharedPreferences (Lab 04)
        switchFavorites.isChecked = sharedPref.getBoolean("favoritesOnly", false)
        switchFavorites.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            editor.putBoolean("favoritesOnly", isChecked)
            editor.apply()
            loadContacts()
        }

        // Search while typing
        etSearch.doAfterTextChanged { loadContacts() }

        loadContacts()
        Log.d(TAG, "OnCreate MainActivity")
    }

    // Read contacts from Room on a background thread, then update the list on the main thread (Lab 04)
    private fun loadContacts() {
        val query = etSearch.text.toString().trim()
        val favoritesOnly = switchFavorites.isChecked

        lifecycleScope.launch(Dispatchers.IO) {
            val result = if (query.isEmpty() && !favoritesOnly) {
                db.contactDao().getAllContacts()
            } else {
                db.contactDao().searchContacts(query, favoritesOnly)
            }

            withContext(Dispatchers.Main) {
                contacts.clear()
                contacts.addAll(result)
                adapter.notifyDataSetChanged()
                showEmptyState(query.isEmpty() && !favoritesOnly)
            }
        }
    }

    // Show the "No contacts yet" message when the list is empty
    private fun showEmptyState(noFilter: Boolean) {
        if (contacts.isEmpty()) {
            listView.visibility = View.GONE
            layoutEmpty.visibility = View.VISIBLE
            if (noFilter) {
                txtEmptyTitle.setText(R.string.empty_title)
                txtEmptyMessage.visibility = View.VISIBLE
            } else {
                txtEmptyTitle.setText(R.string.no_results)
                txtEmptyMessage.visibility = View.GONE
            }
        } else {
            listView.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
        }
    }

    // Star button in a row: flip isFavorite and save it in Room
    private fun toggleFavorite(contact: Contact) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.contactDao().updateContact(contact.copy(isFavorite = !contact.isFavorite))
            withContext(Dispatchers.Main) {
                loadContacts()
            }
        }
    }

    // Activity lifecycle callbacks with logs (Lab 02)
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "OnStart MainActivity")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume MainActivity")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "OnPause MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "OnStop MainActivity")
    }

    // Coming back from another screen: reload, the data may have changed (Lab 04)
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "OnRestart MainActivity")
        loadContacts()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy MainActivity")
    }
}