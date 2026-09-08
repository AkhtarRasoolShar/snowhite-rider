with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    """        if (targetId <= 0) {
            Log.w("ORDERS_DEBUG", "fetchOrders aborted: invalid customerId=$targetId")
            return
        }""",
    """        if (targetId <= 0) {
            // User not logged in, silent return
            return
        }"""
)

content = content.replace(
    """    fun fetchCustomerOrders(isSilent: Boolean = false) {
        val targetId = if (_uiState.value.currentCustomerId > 0) _uiState.value.currentCustomerId else sessionManager.getUserId()
        fetchOrders(targetId, isSilent = isSilent)
    }""",
    """    fun fetchCustomerOrders(isSilent: Boolean = false) {
        val targetId = if (_uiState.value.currentCustomerId > 0) _uiState.value.currentCustomerId else sessionManager.getUserId()
        if (targetId > 0) {
            fetchOrders(targetId, isSilent = isSilent)
        }
    }"""
)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
