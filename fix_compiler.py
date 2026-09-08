import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

content = content.replace('message = "Order #${order.id} is now ${order.status.replace("_", " ")}.",', 'message = "Order #${order.id} is now ${(order.status ?: "PROCESSING").replace("_", " ")}.",')
content = content.replace('time = order.date,', 'time = order.date ?: "Recently",')

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
