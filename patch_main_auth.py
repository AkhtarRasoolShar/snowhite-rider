import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                SplashScreen(
                    onSplashFinished = { viewModel.handleSplashFinished() }""",
"""                SplashScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    onSplashFinished = { viewModel.handleSplashFinished() }"""
)

content = content.replace(
"""                LoginScreen(
                    onLoginSuccess = { customerId, name, phone ->""",
"""                LoginScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    onLoginSuccess = { customerId, name, phone ->"""
)

content = content.replace(
"""                SignUpScreen(
                    onRegisterSuccess = { customerId, name, phone ->""",
"""                SignUpScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    onRegisterSuccess = { customerId, name, phone ->"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
print("Patched Main Auth Screens")
