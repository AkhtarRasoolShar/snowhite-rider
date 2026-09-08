import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                HomeScreen(
                    activeOrder = uiState.currentActiveOrder,""",
"""                HomeScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    activeOrder = uiState.currentActiveOrder,"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)

print("Patched Main Home Screen")
