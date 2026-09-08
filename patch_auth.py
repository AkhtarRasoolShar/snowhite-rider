import re

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# 1. Update SignUpScreen signature
old_sig = """fun SignUpScreen(
    appName: String = "SnowWhite",
    logoUrl: String? = null,
    isLoading: Boolean,
    onSignUpClick: (name: String, phone: String, pass: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit = {}
) {"""
new_sig = """fun SignUpScreen(
    appName: String = "SnowWhite",
    logoUrl: String? = null,
    isLoading: Boolean,
    onSignUpClick: (name: String, phone: String, email: String, pass: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit = {}
) {"""
content = content.replace(old_sig, new_sig)

# 2. Add email state and validation to SignUpScreen
old_states = """    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }"""
new_states = """    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }"""
content = content.replace(old_states, new_states)

old_validate = """    fun validateAndSubmit() {
        val trimmedName = name.trim()
        val trimmedPhone = phone.trim()
        val trimmedPass = password.trim()
        var valid = true"""
new_validate = """    fun validateAndSubmit() {
        val trimmedName = name.trim()
        val trimmedPhone = phone.trim()
        val trimmedEmail = email.trim()
        val trimmedPass = password.trim()
        var valid = true"""
content = content.replace(old_validate, new_validate)

old_val2 = """        if (trimmedPhone.isEmpty()) {
            phoneError = "Phone number is required"
            valid = false
        } else if (trimmedPhone.length < 7) {
            phoneError = "Enter a valid phone number (at least 7 digits)"
            valid = false
        } else {
            phoneError = null
        }"""
new_val2 = """        if (trimmedPhone.isEmpty()) {
            phoneError = "Phone number is required"
            valid = false
        } else if (trimmedPhone.length < 7) {
            phoneError = "Enter a valid phone number (at least 7 digits)"
            valid = false
        } else {
            phoneError = null
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (trimmedEmail.isEmpty()) {
            emailError = "Email is required"
            valid = false
        } else if (!emailRegex.matches(trimmedEmail)) {
            emailError = "Enter a valid email address"
            valid = false
        } else {
            emailError = null
        }"""
content = content.replace(old_val2, new_val2)

old_submit = """        if (valid) {
            focusManager.clearFocus()
            onSignUpClick(trimmedName, trimmedPhone, trimmedPass)
        } else {
            // Error handling/Toast logic could go here if needed
        }"""
new_submit = """        if (valid) {
            focusManager.clearFocus()
            onSignUpClick(trimmedName, trimmedPhone, trimmedEmail, trimmedPass)
        } else {
            android.widget.Toast.makeText(androidx.compose.ui.platform.LocalContext.current, "Please fill all required fields correctly", android.widget.Toast.LENGTH_SHORT).show()
        }"""
content = content.replace(old_submit, new_submit)

# We need to insert the Email field into the UI
# Let's find the OutlinedTextField for phone to add email below it.
# It might be in the code:
# OutlinedTextField(
#     value = phone,
#     onValueChange = { phone = it },
#     ...

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
