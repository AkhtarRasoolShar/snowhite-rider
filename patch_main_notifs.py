import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

# Replace NotificationsListScreen call
call_old = "Screen.NotificationsList -> NotificationsListScreen(\n                                onBackClick = { viewModel.navigateTo(Screen.Home) }\n                            )"
call_new = """Screen.NotificationsList -> {
                                LaunchedEffect(Unit) {
                                    viewModel.fetchCustomerOrders(isSilent = true)
                                    viewModel.fetchPromos()
                                }
                                NotificationsListScreen(
                                    notifications = uiState.notifications,
                                    onBackClick = { viewModel.navigateTo(Screen.Home) }
                                )
                            }"""

content = content.replace(call_old, call_new)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
