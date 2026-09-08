import re

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "r") as f:
    content = f.read()

old_polling = """    // Polling effect every 3 seconds to fetch new messages in real-time
    LaunchedEffect(orderId) {
        while(true) {
            viewModel.fetchMessages(orderId)
            kotlinx.coroutines.delay(3000) // Poll every 3 seconds
        }
    }"""

new_polling = """    // Polling effect every 3 seconds to fetch new messages in real-time
    LaunchedEffect(orderId) {
        // Mark as read immediately when opened
        viewModel.markMessagesAsRead(orderId)
        while(true) {
            viewModel.fetchMessages(orderId)
            kotlinx.coroutines.delay(3000) // Poll every 3 seconds
        }
    }"""

content = content.replace(old_polling, new_polling)

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "w") as f:
    f.write(content)
print("Added markMessagesAsRead to OrderChatScreen")
