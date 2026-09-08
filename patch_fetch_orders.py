import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

# find `isFetchingOrders = false,` inside fetchOrders success block and add `generateNotifications()`
content = content.replace("remoteOrders = fetchedOrders", "remoteOrders = fetchedOrders\n                            )\n                        }\n                    }\n                    generateNotifications()")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
