import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

old_drawer = """                    DrawerMenuContent(
                        currentRoute = when (uiState.currentScreen) {"""

new_drawer = """                    DrawerMenuContent(
                        appName = uiState.appSettings.app_name ?: "SnowWhite",
                        currentRoute = when (uiState.currentScreen) {"""

content = content.replace(old_drawer, new_drawer)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
print("Patched MainContainer Drawer")
