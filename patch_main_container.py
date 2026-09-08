import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

# Add NotificationsScreen to MainContainer
notifications_case = """
                            Screen.Notifications -> NotificationsScreen(
                                onBackClick = { viewModel.navigateTo(Screen.Home) }
                            )

                            Screen.Profile -> CustomerProfileScreen("""

content = content.replace("Screen.Profile -> ProfileScreen(", notifications_case)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
