with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

if "appBanners = uiState.appBanners," not in content:
    content = content.replace(
        "appName = uiState.appSettings.app_name ?: \"SnowWhite\",",
        "appName = uiState.appSettings.app_name ?: \"SnowWhite\",\n                    appBanners = uiState.appBanners,"
    )
    with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
        f.write(content)
