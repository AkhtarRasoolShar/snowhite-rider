package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordState(
    val currentStep: Int = 1,
    val email: String = "",
    val otp: String = "",
    val newPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class ForgotPasswordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState.asStateFlow()

    private val apiService = RetrofitClient.apiService

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null, successMessage = null) }
    }

    fun onOtpChanged(otp: String) {
        _uiState.update { it.copy(otp = otp, errorMessage = null, successMessage = null) }
    }

    fun onNewPasswordChanged(password: String) {
        _uiState.update { it.copy(newPassword = password, errorMessage = null, successMessage = null) }
    }

    fun requestOtp() {
        val email = _uiState.value.email
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val response = apiService.forgotPasswordRequest(mapOf("email" to email))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success" || body?.success == true) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                currentStep = 2,
                                successMessage = body.message ?: "OTP sent to your email."
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = body?.message ?: "Failed to send OTP."
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error: \${response.code()} \${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Network error: \${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun verifyAndResetPassword(onSuccess: (com.example.data.model.AuthResponse) -> Unit) {
        val email = _uiState.value.email
        val otp = _uiState.value.otp
        val newPassword = _uiState.value.newPassword

        if (otp.isBlank() || newPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "OTP and New Password cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val response = apiService.resetPasswordWithOtp(
                    mapOf(
                        "email" to email,
                        "otp" to otp,
                        "new_password" to newPassword
                    )
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success" || body?.success == true) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = body.message ?: "Password reset successful!"
                            )
                        }
                        onSuccess(body)
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = body?.message ?: "Failed to reset password."
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error: \${response.code()} \${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Network error: \${e.localizedMessage}"
                    )
                }
            }
        }
    }
}
