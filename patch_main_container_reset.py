with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                ForgotPasswordScreen(
                    onNavigateToLogin = { viewModel.navigateTo(Screen.Login) }
                )""",
"""                ForgotPasswordScreen(
                    onNavigateToLogin = { viewModel.navigateTo(Screen.Login) },
                    onNavigateToDashboard = { authResponse ->
                        viewModel.handleAuthSuccess(authResponse)
                    }
                )"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
