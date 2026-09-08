import re

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# Fix unresolved references by adding imports
imports_to_add = """
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
"""
if "import androidx.compose.material.icons.filled.Email" not in content:
    content = content.replace("import androidx.compose.material.icons.filled.Phone", "import androidx.compose.material.icons.filled.Phone\n" + imports_to_add)

# In LoginScreen, I injected trimmedEmail validation accidentally!
# Let's remove the email block from LoginScreen (around line 196)
login_email_block = """        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        if (trimmedEmail.isEmpty()) {
            emailError = "Email is required"
            valid = false
        } else if (!emailRegex.matches(trimmedEmail)) {
            emailError = "Enter a valid email address"
            valid = false
        } else {
            emailError = null
        }"""
# In LoginScreen there is no trimmedEmail or emailError.
if login_email_block in content:
    content = content.replace(login_email_block, "", 1) # Only remove first occurrence, second is for SignUp! But wait, is it in SignUp?

# Let's just fix LoginScreen's validateAndSubmit
login_validate = """    fun validateAndSubmit() {
        val trimmedPhone = phone.trim()
        val trimmedPass = password.trim()
        var valid = true

        if (trimmedPhone.isEmpty()) {
            phoneError = "Phone number is required"
            valid = false
        } else if (trimmedPhone.length < 7) {
            phoneError = "Enter a valid phone number (at least 7 digits)"
            valid = false
        } else {
            phoneError = null
        }

        if (trimmedPass.isEmpty()) {
            passwordError = "Password is required"
            valid = false
        }"""
        
# Find LoginScreen validateAndSubmit
match_login = re.search(r"    fun validateAndSubmit\(\) \{\n        val trimmedPhone = phone\.trim\(\)[\s\S]*?passwordError = null\n        \}", content)
if match_login:
    # Replace the whole login validateAndSubmit block
    content = content[:match_login.start()] + login_validate + content[match_login.end():]

# Now for SignUpScreen validateAndSubmit
# User asked for Toast in validateAndSubmit
signup_validate_match = re.search(r"        if \(valid\) \{\n            focusManager\.clearFocus\(\)\n            onSignUpClick\(trimmedName, trimmedPhone, trimmedPass\)\n        \}", content)
if signup_validate_match:
    new_signup = """        if (valid) {
            focusManager.clearFocus()
            onSignUpClick(trimmedName, trimmedPhone, trimmedEmail, trimmedPass)
        } else {
            if (emailError != null) {
                Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            }
        }"""
    content = content[:signup_validate_match.start()] + new_signup + content[signup_validate_match.end():]

# Need `val context = LocalContext.current` in SignUpScreen
if "val context = LocalContext.current" not in content:
    content = content.replace("val focusManager = LocalFocusManager.current", "val focusManager = LocalFocusManager.current\n    val context = LocalContext.current")

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
