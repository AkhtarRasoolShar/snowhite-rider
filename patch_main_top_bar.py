import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                        TopAppBarHeader(
                            onOpenDrawer = {""",
"""                        TopAppBarHeader(
                            appName = uiState.appSettings.app_name ?: "SnowWhite",
                            onOpenDrawer = {"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
print("Patched Main Top Bar")
