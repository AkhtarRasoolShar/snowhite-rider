with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                            Screen.Home -> HomeScreen(
                                activeOrder = uiState.currentActiveOrder,""",
"""                            Screen.Home -> HomeScreen(
                                appName = uiState.appSettings.app_name ?: "SnowWhite",
                                appBanners = uiState.appBanners,
                                activeOrder = uiState.currentActiveOrder,"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
