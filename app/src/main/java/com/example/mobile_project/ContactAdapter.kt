package com.example.mobile_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

// for each Contact (badal el list item)
class ContactAdapter(
    context: Context,
    private val contacts: List<Contact>,
    private val onFavoriteClick: (Contact) -> Unit
) : ArrayAdapter<Contact>(context, R.layout.row_contact, contacts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Reuse an old row if ListView gives one, else make new row
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.row_contact, parent, false)
        val contact = contacts[position]

        val txtAvatar = view.findViewById<TextView>(R.id.txt_avatar)
        val txtName = view.findViewById<TextView>(R.id.txt_name)
        val txtPhone = view.findViewById<TextView>(R.id.txt_phone)
        val btnFavorite = view.findViewById<ImageButton>(R.id.btn_favorite)

        txtAvatar.text = contact.getInitials()
        txtName.text = contact.name
        txtPhone.text = contact.phone

        //  favorites / others
        val starIcon = if (contact.isFavorite) R.drawable.ic_star else R.drawable.ic_star_outline
        btnFavorite.setImageResource(starIcon)
        btnFavorite.setOnClickListener { onFavoriteClick(contact) }

        return view
    }
}