import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

# Add selectHub and fetchHubs
if "fun selectHub" not in content:
    logic = """
    fun selectHub(hub: com.example.data.model.Hub) {
        _uiState.update { it.copy(selectedHub = hub) }
    }

    fun fetchHubs() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getHubs()
                if (response.isSuccessful) {
                    val hubs = response.body()?.data ?: response.body()?.hubs ?: emptyList()
                    _uiState.update { it.copy(availableHubs = hubs) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
"""
    content = re.sub(r"}\s*$", logic + "\n}", content)

# update createAndSubmitOrder
if "hub_name = state.selectedHub?.name" not in content:
    content = content.replace('service_tier = state.selectedServiceTier.title,', 'service_tier = state.selectedServiceTier.title,\n                hub_name = state.selectedHub?.name,')

# Add validation check to createAndSubmitOrder
validation_check = """        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        if (state.selectedHub == null) {
            _uiState.update { it.copy(snackbarMessage = "Please select a hub before checkout.") }
            return
        }"""
content = content.replace("""        val state = _uiState.value
        if (state.cartItems.isEmpty()) return""", validation_check)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
