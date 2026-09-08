import re

with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

app_settings_code = """
data class AppSettings(
    val app_name: String? = "SnowWhite",
    val whatsapp_number: String? = "",
    val support_email: String? = "",
    val delivery_fee: String? = "0",
    val currency: String? = "PKR"
)
"""

if "data class AppSettings" not in content:
    content = content.replace(
        "data class ApiResponse",
        app_settings_code.strip() + "\n\ndata class ApiResponse"
    )

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

api_settings_code = """
    @GET("routes.php?action=get_settings")
    suspend fun getAppSettings(): Response<ApiResponse<Map<String, String>>>
"""

if "fun getAppSettings" not in content:
    # Use Response wrapper for Retrofit if other methods use it. Let's see later. I'll patch it.
    content = content.replace(
        "interface SnowWhiteApiService {",
        "interface SnowWhiteApiService {" + api_settings_code
    )

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)

print("Patched Models and API")
