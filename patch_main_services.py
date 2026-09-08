with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
    "activeOrder = uiState.currentActiveOrder,",
    "activeOrder = uiState.currentActiveOrder,\n                                services = uiState.servicesList,"
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
