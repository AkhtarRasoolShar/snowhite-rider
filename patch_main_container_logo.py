with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
    "                LoginScreen(\n                    isLoading = uiState.isAuthLoading,",
    "                LoginScreen(\n                    logoUrl = uiState.appSettings.logo_url,\n                    isLoading = uiState.isAuthLoading,"
)

content = content.replace(
    "                SignUpScreen(\n                    isLoading = uiState.isAuthLoading,",
    "                SignUpScreen(\n                    logoUrl = uiState.appSettings.logo_url,\n                    isLoading = uiState.isAuthLoading,"
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
