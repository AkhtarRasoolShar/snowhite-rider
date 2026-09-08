with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

if "import kotlinx.coroutines.delay" not in content:
    content = content.replace(
        "import androidx.compose.runtime.LaunchedEffect",
        "import androidx.compose.runtime.LaunchedEffect\nimport kotlinx.coroutines.delay"
    )

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
