import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

# Update the TopAppBarHeader callback
# It should map to NotificationsList
content = content.replace("viewModel.navigateTo(Screen.NotificationSettings)", "viewModel.navigateTo(Screen.NotificationsList)")

# Update the composable mapping
content = content.replace("Screen.Notifications -> NotificationsScreen(", "Screen.NotificationsList -> NotificationsListScreen(")

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
