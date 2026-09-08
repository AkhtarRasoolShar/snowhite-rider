import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

# Add states to UIState
if "val isProfileOtpDialogVisible: Boolean = false" not in content:
    content = content.replace("val userProfileEmail: String = \"\",", 
        "val userProfileEmail: String = \"\",\n    val isProfileOtpDialogVisible: Boolean = false,\n    val profileUpdatePhone: String = \"\",\n    val profileUpdateEmail: String = \"\",")

# Add OTP Logic
otp_logic = """
    fun updateProfileParams(newPhone: String, newEmail: String) {
        val currentPhone = _uiState.value.userProfilePhone
        val currentEmail = _uiState.value.userProfileEmail
        
        if (newPhone == currentPhone && newEmail == currentEmail) {
            _uiState.update { it.copy(snackbarMessage = "No changes to save.") }
            return
        }
        
        _uiState.update { it.copy(
            isAuthLoading = true,
            profileUpdatePhone = newPhone,
            profileUpdateEmail = newEmail
        ) }
        
        viewModelScope.launch {
            try {
                val req = mapOf(
                    "customer_id" to _uiState.value.currentCustomerId.toString(),
                    "phone" to newPhone,
                    "email" to newEmail
                )
                val response = RetrofitClient.apiService.requestProfileUpdateOtp(req)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(
                        isAuthLoading = false,
                        isProfileOtpDialogVisible = true,
                        snackbarMessage = "OTP sent to your email/phone."
                    ) }
                } else {
                    _uiState.update { it.copy(isAuthLoading = false, snackbarMessage = "Failed to send OTP.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isAuthLoading = false, snackbarMessage = "Network error: ${e.message}") }
            }
        }
    }

    fun verifyProfileOtp(otp: String) {
        if (otp.isBlank()) return
        
        _uiState.update { it.copy(isAuthLoading = true) }
        
        viewModelScope.launch {
            try {
                val req = mapOf(
                    "customer_id" to _uiState.value.currentCustomerId.toString(),
                    "otp" to otp,
                    "phone" to _uiState.value.profileUpdatePhone,
                    "email" to _uiState.value.profileUpdateEmail
                )
                val response = RetrofitClient.apiService.verifyAndUpdateProfile(req)
                if (response.isSuccessful) {
                    val newPhone = _uiState.value.profileUpdatePhone
                    val newEmail = _uiState.value.profileUpdateEmail
                    val newName = _uiState.value.userProfileName
                    
                    sessionManager.saveUserSession(_uiState.value.currentCustomerId, newName, newPhone, newEmail)
                    
                    _uiState.update { it.copy(
                        isAuthLoading = false,
                        isProfileOtpDialogVisible = false,
                        userProfilePhone = newPhone,
                        userProfileEmail = newEmail,
                        snackbarMessage = "Profile updated successfully!"
                    ) }
                } else {
                    _uiState.update { it.copy(isAuthLoading = false, snackbarMessage = "Invalid OTP.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isAuthLoading = false, snackbarMessage = "Network error: ${e.message}") }
            }
        }
    }

    fun dismissProfileOtpDialog() {
        _uiState.update { it.copy(isProfileOtpDialogVisible = false) }
    }
"""

if "fun updateProfileParams" not in content:
    # insert before the last closing brace
    parts = content.rsplit("}", 1)
    content = parts[0] + otp_logic + "\n}"
    
    # We also need to retrieve email from session manager on init
    load_profile = "val email = sessionManager.getUserEmail() ?: \"\""
    update_state = "userProfileEmail = email"
    
    # Check init block to inject email retrieval
    # For now, let's just do a simple replace in checkSession
    old_check = """                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        currentCustomerId = cid,
                        userProfileName = name,
                        userProfilePhone = phone"""
    new_check = """                val email = sessionManager.getUserEmail() ?: ""
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        currentCustomerId = cid,
                        userProfileName = name,
                        userProfilePhone = phone,
                        userProfileEmail = email"""
    content = content.replace(old_check, new_check)

    with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
        f.write(content)
