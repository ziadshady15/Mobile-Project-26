package com.example.mobile_project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val email: String = "",
    val isFavorite: Boolean = false
) {
    fun getInitials(): String {
        val words = name.trim().split(" ").filter { it.isNotEmpty() }
        return words.take(2).map { it.first().uppercaseChar() }.joinToString("")
    }
}