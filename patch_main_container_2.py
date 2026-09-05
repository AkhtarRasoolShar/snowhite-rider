import re

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                OrderChatScreen(
                    orderId = screen.orderId,
                    mySenderType = screen.mySenderType,
                    mySenderId = uiState.currentCustomerId,
                    orderCode = screen.orderCode,
                    otherPartyName = screen.otherPartyName,
                    viewModel = viewModel,
                    onBackClick = { viewModel.navigateTo(Screen.OrderHistory) }
                )""",
"""                OrderChatScreen(
                    orderId = screen.orderId,
                    mySenderType = screen.mySenderType,
                    mySenderId = uiState.currentCustomerId,
                    orderCode = screen.orderCode,
                    otherPartyName = screen.otherPartyName,
                    chatMessages = uiState.chatMessages,
                    viewModel = viewModel,
                    onBackClick = { viewModel.navigateTo(Screen.OrderHistory) }
                )"""
)

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)
print("Patched MainContainer 2")
