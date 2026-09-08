with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

content = content.replace(
    "Response<com.example.data.model.ApiResponse<Any>>",
    "Response<AuthResponse>"
)

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)
