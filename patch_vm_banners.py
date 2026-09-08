import re
with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

if "val appBanners: List<com.example.data.model.Banner>" not in content:
    content = content.replace(
        "val appSettings: com.example.data.model.AppSettings = com.example.data.model.AppSettings(),",
        "val appSettings: com.example.data.model.AppSettings = com.example.data.model.AppSettings(),\n    val appBanners: List<com.example.data.model.Banner> = emptyList(),"
    )

if "getBanners" not in content:
    fetch_banners_logic = """                        val bannersResponse = RetrofitClient.apiService.getBanners()
                        if (bannersResponse.isSuccessful) {
                            val bannersData = bannersResponse.body()?.data ?: emptyList()
                            _uiState.update { it.copy(appBanners = bannersData) }
                        }
"""
    content = content.replace(
        "_uiState.update { it.copy(appSettings = newSettings) }",
        "_uiState.update { it.copy(appSettings = newSettings) }\n" + fetch_banners_logic
    )

    with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
        f.write(content)
