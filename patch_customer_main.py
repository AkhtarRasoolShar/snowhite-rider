import re

with open("app/src/main/java/com/example/CustomerMainActivity.kt", "r") as f:
    content = f.read()

old_launched_effect = """                // REAL-TIME STATUS SYNC: Polite background poll if needed
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(30000L) // Poll every 30 seconds
                        val customerId = uiState.currentCustomerId
                        if (customerId > 0) {
                            viewModel.fetchOrders(customerId, isSilent = true)
                        }
                    }
                }"""

new_launched_effect = """                // REAL-TIME STATUS SYNC: Polite background poll if needed
                LaunchedEffect(Unit) {
                    val initialId = uiState.currentCustomerId
                    if (initialId > 0) {
                        viewModel.fetchOrders(initialId, isSilent = true)
                    }
                    while (true) {
                        delay(30000L) // Poll every 30 seconds
                        val customerId = uiState.currentCustomerId
                        if (customerId > 0) {
                            viewModel.fetchOrders(customerId, isSilent = true)
                        }
                    }
                }"""

content = content.replace(old_launched_effect, new_launched_effect)

old_launched_effect_2 = """    // REAL-TIME STATUS SYNC: Polite background poll if needed
    LaunchedEffect(Unit) {
        while (true) {
            delay(30000L) // Poll every 30 seconds
            val customerId = uiState.currentCustomerId
            if (customerId > 0) {
                viewModel.fetchOrders(customerId, isSilent = true)
            }
        }
    }"""

new_launched_effect_2 = """    // REAL-TIME STATUS SYNC: Polite background poll if needed
    LaunchedEffect(Unit) {
        val initialId = uiState.currentCustomerId
        if (initialId > 0) {
            viewModel.fetchOrders(initialId, isSilent = true)
        }
        while (true) {
            delay(30000L) // Poll every 30 seconds
            val customerId = uiState.currentCustomerId
            if (customerId > 0) {
                viewModel.fetchOrders(customerId, isSilent = true)
            }
        }
    }"""

content = content.replace(old_launched_effect_2, new_launched_effect_2)

with open("app/src/main/java/com/example/CustomerMainActivity.kt", "w") as f:
    f.write(content)

print("Patched CustomerMainActivity.kt")
