import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

# Replace sorting and ID logic
old_logic = """state.remoteOrders.sortedByDescending { it.id }.take(5).forEach { order ->
            notifications.add(
                NotificationItemModel(
                    icon = androidx.compose.material.icons.Icons.Default.LocalLaundryService,
                    title = "Order Status Update",
                    message = "Order #${order.id} is now ${(order.status ?: "PROCESSING").replace("_", " ")}.",
                    time = order.date ?: "Recently","""

new_logic = """state.remoteOrders.sortedByDescending { it.order_id ?: it.id ?: "" }.take(5).forEach { order ->
            val displayId = order.order_id ?: order.orderId ?: order.id ?: "Unknown"
            notifications.add(
                NotificationItemModel(
                    icon = androidx.compose.material.icons.Icons.Default.LocalLaundryService,
                    title = "Order Status Update",
                    message = "Order #${displayId} is now ${(order.status ?: "PROCESSING").replace("_", " ")}.",
                    time = order.date ?: "Recently","""

content = content.replace(old_logic, new_logic)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
