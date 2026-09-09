import re

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

if "getHubs" not in content:
    promo_method = """
    @GET("api/routes.php?action=get_hubs")
    suspend fun getHubs(): retrofit2.Response<com.example.data.model.GetHubsResponse>
"""
    content = content.replace("interface SnowWhiteApiService {", "interface SnowWhiteApiService {" + promo_method)

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)
