import re

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

content = content.replace(
"""    @POST("routes.php")
    @POST("routes.php")
    suspend fun markMessagesAsRead(""",
"""    @POST("routes.php")
    suspend fun markMessagesAsRead("""
)

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)
print("Fixed API Service")
