import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                    DrawerMenuContent(
                        appName = uiState.appSettings.app_name ?: "SnowWhite",
                        currentRoute = when (uiState.currentScreen) {""",
"""                    DrawerMenuContent(
                        appName = uiState.appSettings.app_name ?: "SnowWhite",
                        whatsappNumber = uiState.appSettings.whatsapp_number ?: "",
                        currentRoute = when (uiState.currentScreen) {"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
print("Patched Main WhatsApp")
