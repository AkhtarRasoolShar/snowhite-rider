with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                    DrawerMenuContent(
                        appName = uiState.appSettings.app_name ?: "SnowWhite",
                    appBanners = uiState.appBanners,
                        whatsappNumber = uiState.appSettings.whatsapp_number ?: "",""",
"""                    DrawerMenuContent(
                        appName = uiState.appSettings.app_name ?: "SnowWhite",
                        whatsappNumber = uiState.appSettings.whatsapp_number ?: "","""
)

content = content.replace(
"""                        TopAppBarHeader(
                            appName = uiState.appSettings.app_name ?: "SnowWhite",
                    appBanners = uiState.appBanners,
                            logoUrl = uiState.appSettings.logo_url,""",
"""                        TopAppBarHeader(
                            appName = uiState.appSettings.app_name ?: "SnowWhite",
                            logoUrl = uiState.appSettings.logo_url,"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
