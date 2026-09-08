import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

old_signup = "onSignUpClick = { name, phone, pass -> viewModel.registerUser(name, phone, pass) }"
new_signup = "onSignUpClick = { name, phone, email, pass -> viewModel.registerUser(name, phone, email, pass) }"
content = content.replace(old_signup, new_signup)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

old_register = """    fun registerUser(name: String, phone: String, pass: String) {
        val cleanName = name.trim()
        val cleanPhone = phone.trim()
        val cleanPass = pass.trim()
        if (cleanName.isBlank() || cleanPhone.isBlank() || cleanPass.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please fill in all registration fields.") }
            return
        }

        _uiState.update { it.copy(isAuthLoading = true) }

        viewModelScope.launch {
            try {
                val req = mapOf("name" to cleanName, "phone" to cleanPhone, "password" to cleanPass)"""

new_register = """    fun registerUser(name: String, phone: String, email: String, pass: String) {
        val cleanName = name.trim()
        val cleanPhone = phone.trim()
        val cleanEmail = email.trim()
        val cleanPass = pass.trim()
        if (cleanName.isBlank() || cleanPhone.isBlank() || cleanEmail.isBlank() || cleanPass.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please fill in all registration fields.") }
            return
        }

        _uiState.update { it.copy(isAuthLoading = true) }

        viewModelScope.launch {
            try {
                val req = mapOf("name" to cleanName, "phone" to cleanPhone, "email" to cleanEmail, "password" to cleanPass)"""

content = content.replace(old_register, new_register)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)

