import re

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

old_register = """fun RegisterScreen(
    isLoading: Boolean,
    onRegisterClick: (name: String, phone: String, pass: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit = {}
) {"""

new_register = """fun RegisterScreen(
    isLoading: Boolean,
    onRegisterClick: (name: String, phone: String, email: String, pass: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit = {}
) {"""

content = content.replace(old_register, new_register)

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
