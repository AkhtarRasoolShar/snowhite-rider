import re

with open("app/src/main/java/com/example/data/local/SessionManager.kt", "r") as f:
    content = f.read()

content = content.replace('private const val KEY_PHONE = "phone"', 'private const val KEY_PHONE = "phone"\n        private const val KEY_EMAIL = "email"')

old_save = """    fun saveUser(id: Int, name: String, phone: String) {
        try {
            prefs?.edit()
                ?.putBoolean(KEY_IS_LOGGED_IN, true)
                ?.putInt(KEY_USER_ID, id)
                ?.putInt(KEY_CUSTOMER_ID, id)
                ?.putString(KEY_NAME, name)
                ?.putString(KEY_CUSTOMER_NAME, name)
                ?.putString(KEY_PHONE, phone)
                ?.putString(KEY_CUSTOMER_PHONE, phone)
                ?.apply()
        } catch (_: Exception) {}
    }

    fun saveUserSession(id: Int, name: String, phone: String) {
        saveUser(id, name, phone)
    }"""

new_save = """    fun saveUser(id: Int, name: String, phone: String, email: String = "") {
        try {
            prefs?.edit()
                ?.putBoolean(KEY_IS_LOGGED_IN, true)
                ?.putInt(KEY_USER_ID, id)
                ?.putInt(KEY_CUSTOMER_ID, id)
                ?.putString(KEY_NAME, name)
                ?.putString(KEY_CUSTOMER_NAME, name)
                ?.putString(KEY_PHONE, phone)
                ?.putString(KEY_CUSTOMER_PHONE, phone)
                ?.putString(KEY_EMAIL, email)
                ?.apply()
        } catch (_: Exception) {}
    }

    fun saveUserSession(id: Int, name: String, phone: String, email: String = "") {
        saveUser(id, name, phone, email)
    }
    
    fun getUserEmail(): String? {
        return try {
            if (isLoggedIn()) {
                prefs?.getString(KEY_EMAIL, "")
            } else null
        } catch (_: Exception) {
            null
        }
    }"""

content = content.replace(old_save, new_save)

with open("app/src/main/java/com/example/data/local/SessionManager.kt", "w") as f:
    f.write(content)

