with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("object SignUp : Screen()", "object SignUp : Screen()\n    object ForgotPassword : Screen()")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
