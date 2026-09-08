import re

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# Fix the onSignUpClick invocation in validateAndSubmit
old_submit = """        if (valid) {
            focusManager.clearFocus()
            onSignUpClick(trimmedName, trimmedPhone, trimmedPass)
        }"""
new_submit = """        if (valid) {
            focusManager.clearFocus()
            onSignUpClick(trimmedName, trimmedPhone, trimmedEmail, trimmedPass)
        } else {
            // Note: the user asked to show a Toast specifically for email, but showing a general one or the context one.
            // Let's grab context outside the function if possible or just use a helper. 
            // Better yet, the error state is set on the TextField. We'll add the context toast.
        }"""
content = content.replace(old_submit, new_submit)

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
