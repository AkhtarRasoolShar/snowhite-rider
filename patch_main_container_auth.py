import re
with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

# Add forgot password routing
content = content.replace(
    "onNavigateToSignUp = { viewModel.navigateTo(Screen.SignUp) },",
    "onNavigateToSignUp = { viewModel.navigateTo(Screen.SignUp) },\n                    onNavigateToForgotPassword = { viewModel.navigateTo(Screen.ForgotPassword) },"
)

forgot_password_routing = """
        Screen.ForgotPassword -> {
            Box(modifier = Modifier.fillMaxSize()) {
                ForgotPasswordScreen(
                    onNavigateToLogin = { viewModel.navigateTo(Screen.Login) }
                )
            }
        }
"""

content = content.replace(
    "        Screen.SignUp -> {",
    forgot_password_routing + "\n        Screen.SignUp -> {"
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
