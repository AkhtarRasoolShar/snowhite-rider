import re

# 1. Update AuthScreens.kt
with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    auth_content = f.read()

# Replace fun SignUpScreen( with fun RegisterScreen(
auth_content = auth_content.replace("fun SignUpScreen(", "fun RegisterScreen(")

# Remove the old RegisterScreen wrapper
old_register_wrapper = """@Composable
fun RegisterScreen(
    isLoading: Boolean,
    onRegisterClick: (name: String, phone: String, email: String, pass: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    SignUpScreen(
        isLoading = isLoading,
        onSignUpClick = onRegisterClick,
        onNavigateToLogin = onNavigateToLogin,
        onBackClick = onBackClick
    )
}"""
auth_content = auth_content.replace(old_register_wrapper, "")

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(auth_content)

# 2. Update MainContainer.kt
with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    main_content = f.read()

main_content = main_content.replace("SignUpScreen(", "RegisterScreen(")

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(main_content)
