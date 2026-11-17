package com.example.mobile_project

import android.util.Patterns

// Helper class that checks the form fields (like the Addition class in Lab 01)
class ContactValidator {

    // At least 2 characters that are not spaces
    fun isValidName(name: String): Boolean {
        return name.replace(" ", "").length >= 2
    }

    // Egyptian mobile number: 01XXXXXXXXX (11 digits), spaces are allowed
    fun isValidPhone(phone: String): Boolean {
        val digits = cleanPhone(phone)
        return digits.length == 11 && digits.startsWith("01") && digits.all { it.isDigit() }
    }

    // Email is optional: empty is OK, otherwise it must look like an email address
    fun isValidEmail(email: String): Boolean {
        return email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // "010 1234 5678" -> "01012345678" (saved without spaces)
    fun cleanPhone(phone: String): String {
        return phone.replace(" ", "")
    }
}
