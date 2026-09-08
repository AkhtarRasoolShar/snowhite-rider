with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

func = """
    fun handleAuthSuccess(body: com.example.data.model.AuthResponse) {
        val userId = body.extractUserId() ?: 101
        val userName = body.extractUserName() ?: "Akhtar Hussain"
        val userPhone = body.extractUserPhone() ?: "Unknown"

        sessionManager.saveUser(userId, userName, userPhone)

        val targetScreen = _uiState.value.postLoginTargetScreen ?: Screen.Home

        _uiState.update {
            it.copy(
                isLoggedIn = true,
                currentCustomerId = userId,
                userProfileName = userName,
                userProfilePhone = userPhone,
                isAuthLoading = false,
                currentScreen = targetScreen,
                postLoginTargetScreen = null,
                snackbarMessage = body.message ?: "Welcome back!"
            )
        }
    }
"""

content = content.replace("    fun loginUser(phone: String, pass: String) {", func + "\n    fun loginUser(phone: String, pass: String) {")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
