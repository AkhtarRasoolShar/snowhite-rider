import re
with open("app/src/main/java/com/example/ui/viewmodel/ForgotPasswordViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    "fun verifyAndResetPassword(onSuccess: () -> Unit)",
    "fun verifyAndResetPassword(onSuccess: (com.example.data.model.AuthResponse) -> Unit)"
)

content = content.replace(
    """                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = body.message ?: "Password reset successful!"
                            )
                        }
                        onSuccess()""",
    """                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = body.message ?: "Password reset successful!"
                            )
                        }
                        onSuccess(body)"""
)

with open("app/src/main/java/com/example/ui/viewmodel/ForgotPasswordViewModel.kt", "w") as f:
    f.write(content)
