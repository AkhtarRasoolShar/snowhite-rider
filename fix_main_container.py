with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                SplashScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    appBanners = uiState.appBanners,
                    logoUrl = uiState.appSettings.logo_url,""",
"""                SplashScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    logoUrl = uiState.appSettings.logo_url,"""
)

content = content.replace(
"""                LoginScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    appBanners = uiState.appBanners,
                    logoUrl = uiState.appSettings.logo_url,""",
"""                LoginScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    logoUrl = uiState.appSettings.logo_url,"""
)

content = content.replace(
"""                SignUpScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    appBanners = uiState.appBanners,
                    logoUrl = uiState.appSettings.logo_url,""",
"""                SignUpScreen(
                    appName = uiState.appSettings.app_name ?: "SnowWhite",
                    logoUrl = uiState.appSettings.logo_url,"""
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
