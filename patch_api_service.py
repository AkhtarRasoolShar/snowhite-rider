with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

endpoints = """
    @POST("routes.php?action=forgot_password_request")
    suspend fun forgotPasswordRequest(
        @Body request: Map<String, String>
    ): Response<com.example.data.model.ApiResponse<Any>>

    @POST("routes.php?action=reset_password_with_otp")
    suspend fun resetPasswordWithOtp(
        @Body request: Map<String, String>
    ): Response<com.example.data.model.ApiResponse<Any>>
"""

content = content.replace("interface SnowWhiteApiService {", "interface SnowWhiteApiService {\n" + endpoints)

with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
    f.write(content)
