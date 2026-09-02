package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences? = try {
        context.getSharedPreferences("snowwhite_user_session", Context.MODE_PRIVATE)
    } catch (_: Exception) {
        null
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_CUSTOMER_ID = "customer_id"
        private const val KEY_CUSTOMER_NAME = "customer_name"
        private const val KEY_CUSTOMER_PHONE = "customer_phone"
        private const val KEY_CUSTOMER_ADDRESS = "customer_address"
        private const val KEY_COMPLETED_ORDERS_COUNT = "completed_orders_count"
        private const val KEY_HAS_PROMPTED_REVIEW = "has_prompted_review"
    }

    fun incrementCompletedOrdersCount(): Int {
        val current = getCompletedOrdersCount()
        val next = current + 1
        try {
            prefs?.edit()?.putInt(KEY_COMPLETED_ORDERS_COUNT, next)?.apply()
        } catch (_: Exception) {}
        return next
    }

    fun getCompletedOrdersCount(): Int {
        return try {
            prefs?.getInt(KEY_COMPLETED_ORDERS_COUNT, 2) ?: 2 // Defaults to 2 so 3rd order triggers review prompt
        } catch (_: Exception) {
            2
        }
    }

    fun hasPromptedForReview(): Boolean {
        return try {
            prefs?.getBoolean(KEY_HAS_PROMPTED_REVIEW, false) ?: false
        } catch (_: Exception) {
            false
        }
    }

    fun setPromptedForReview(prompted: Boolean = true) {
        try {
            prefs?.edit()?.putBoolean(KEY_HAS_PROMPTED_REVIEW, prompted)?.apply()
        } catch (_: Exception) {}
    }

    fun saveUserSession(id: Int, name: String, phone: String) {
        try {
            prefs?.edit()
                ?.putBoolean(KEY_IS_LOGGED_IN, true)
                ?.putInt(KEY_CUSTOMER_ID, id)
                ?.putString(KEY_CUSTOMER_NAME, name)
                ?.putString(KEY_CUSTOMER_PHONE, phone)
                ?.apply()
        } catch (_: Exception) {}
    }

    fun saveAddress(address: String) {
        try {
            prefs?.edit()
                ?.putString(KEY_CUSTOMER_ADDRESS, address)
                ?.apply()
        } catch (_: Exception) {}
    }

    fun getAddress(): String {
        return try {
            prefs?.getString(KEY_CUSTOMER_ADDRESS, "") ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    fun isLoggedIn(): Boolean {
        return try {
            prefs?.getBoolean(KEY_IS_LOGGED_IN, false) ?: false
        } catch (_: Exception) {
            false
        }
    }

    fun getUserId(): Int {
        return try {
            prefs?.getInt(KEY_CUSTOMER_ID, 1) ?: 1
        } catch (_: Exception) {
            1
        }
    }

    fun getUserName(): String? {
        return try {
            if (isLoggedIn()) prefs?.getString(KEY_CUSTOMER_NAME, null) else null
        } catch (_: Exception) {
            null
        }
    }

    fun getUserPhone(): String? {
        return try {
            if (isLoggedIn()) prefs?.getString(KEY_CUSTOMER_PHONE, null) else null
        } catch (_: Exception) {
            null
        }
    }

    fun clearSession() {
        try {
            prefs?.edit()?.clear()?.apply()
        } catch (_: Exception) {}
    }

    fun logout() {
        clearSession()
    }
}

