with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

content = content.replace(
    """    suspend fun forgotPasswordRequest(
        @Body request: Map<String, String>
    ): Response<AuthResponse>""",
    """    suspend fun forgotPasswordRequest(
        @Body request: Map<String, String>
    ): Response<com.example.data.model.ApiResponse<Any>>"""
)

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)
