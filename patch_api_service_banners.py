with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "r") as f:
    content = f.read()

if "getBanners" not in content:
    content = content.replace("    @GET(\"routes.php?action=get_categories\")", "    @GET(\"routes.php?action=get_banners\")\n    suspend fun getBanners(): Response<ApiResponse<List<com.example.data.model.Banner>>>\n\n    @GET(\"routes.php?action=get_categories\")")
    with open("app/src/main/java/com/example/data/remote/SnowWhiteApiService.kt", "w") as f:
        f.write(content)
