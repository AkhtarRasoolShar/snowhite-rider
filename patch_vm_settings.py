import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

# Add AppSettings to UiState
if "val appSettings: com.example.data.model.AppSettings = com.example.data.model.AppSettings()," not in content:
    content = content.replace(
        "val currentScreen: Screen = Screen.Splash,",
        "val currentScreen: Screen = Screen.Splash,\n    val appSettings: com.example.data.model.AppSettings = com.example.data.model.AppSettings(),"
    )

fetch_settings_code = """
    fun fetchSettings() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAppSettings()
                if (response.isSuccessful) {
                    val body = response.body()
                    val data = body?.data
                    if (data != null) {
                        val newSettings = com.example.data.model.AppSettings(
                            app_name = data["app_name"] ?: "SnowWhite",
                            whatsapp_number = data["whatsapp_number"] ?: "",
                            support_email = data["support_email"] ?: "",
                            delivery_fee = data["delivery_fee"] ?: "0",
                            currency = data["currency"] ?: "PKR"
                        )
                        _uiState.update { it.copy(appSettings = newSettings) }
                    }
                }
            } catch (e: Exception) {
                Log.e("SETTINGS_DEBUG", "Failed to fetch settings: ${e.message}")
            }
        }
    }
"""

if "fun fetchSettings()" not in content:
    content = content.replace(
        "init {",
        fetch_settings_code.strip() + "\n\n    init {"
    )
    
if "fetchSettings()" not in content.split("init {")[1].split("}")[0]:
    content = content.replace(
        "init {",
        "init {\n        fetchSettings()"
    )

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)

print("Patched ViewModel settings")
