import re

with open("app/src/main/java/com/example/ui/screens/OrderHistoryScreen.kt", "r") as f:
    content = f.read()

old_launched_effect = """    // Fetch on initial screen entry if empty, and gently sync every 30s only when active orders exist
    LaunchedEffect(userId, hasActiveOrders) {
        Log.d("ORDERS_DEBUG", "OrderHistoryScreen loaded for ID: $userId (hasActiveOrders=$hasActiveOrders)")
        if (remoteOrders.isEmpty()) {
            if (onFetchOrders != null && userId > 0) {
                onFetchOrders(userId)
            } else {
                onRefreshOrders()
            }
        }
        if (hasActiveOrders && userId > 0) {
            while (true) {
                delay(30000L) // Poll every 30s instead of slamming the DB
                onFetchOrders?.invoke(userId)
            }
        }
    }"""

new_launched_effect = """    // ALWAYS fetch on initial screen entry to restore missing items
    LaunchedEffect(Unit) {
        if (onFetchOrders != null && userId > 0) {
            onFetchOrders(userId)
        } else {
            onRefreshOrders()
        }
    }

    // Gently sync every 30s only when active orders exist
    LaunchedEffect(userId, hasActiveOrders) {
        if (hasActiveOrders && userId > 0) {
            while (true) {
                kotlinx.coroutines.delay(30000L) // Poll every 30s instead of slamming the DB
                onFetchOrders?.invoke(userId)
            }
        }
    }"""

content = content.replace(old_launched_effect, new_launched_effect)

with open("app/src/main/java/com/example/ui/screens/OrderHistoryScreen.kt", "w") as f:
    f.write(content)
print("Patched OrderHistoryScreen.kt")
