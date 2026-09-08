with open("app/src/main/java/com/example/ui/screens/ForgotPasswordScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "fun ForgotPasswordScreen(\n    onNavigateToLogin: () -> Unit,\n    viewModel: ForgotPasswordViewModel = viewModel()\n) {",
    "fun ForgotPasswordScreen(\n    onNavigateToLogin: () -> Unit,\n    onNavigateToDashboard: (com.example.data.model.AuthResponse) -> Unit,\n    viewModel: ForgotPasswordViewModel = viewModel()\n) {"
)

content = content.replace(
    "                            viewModel.verifyAndResetPassword(onSuccess = {\n                                // Will stay on screen to show success message,\n                                // or can navigate immediately. We'll show message.\n                            })",
    "                            viewModel.verifyAndResetPassword(onSuccess = { authResponse ->\n                                onNavigateToDashboard(authResponse)\n                            })"
)

with open("app/src/main/java/com/example/ui/screens/ForgotPasswordScreen.kt", "w") as f:
    f.write(content)
