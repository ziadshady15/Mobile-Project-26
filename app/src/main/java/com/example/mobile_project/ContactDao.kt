package com.example.mobile_project

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDao {
    @Insert
    suspend fun insertContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contacts ORDER BY name")
    suspend fun getAllContacts(): List<Contact>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    @Query(
        "SELECT * FROM contacts " +
                "WHERE (name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%') " +
                "AND (:favOnly = 0 OR isFavorite = 1) " +
                "ORDER BY name"
    )
    suspend fun searchContacts(query: String, favOnly: Boolean): List<Contact>
}
