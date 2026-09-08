import re

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

# Add getPromos
if "getPromos" not in content:
    promo_method = """
    @GET("routes.php?action=get_promos")
    suspend fun getPromos(): retrofit2.Response<com.example.data.model.GetPromosResponse>
"""
    content = content.replace("interface SnowWhiteApiService {", "interface SnowWhiteApiService {" + promo_method)

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)
