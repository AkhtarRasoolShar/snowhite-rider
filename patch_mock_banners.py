with open("app/src/main/java/com/example/data/remote/RetrofitClient.kt", "r") as f:
    content = f.read()

if "action=get_banners" not in content:
    mock_logic = """            if (url.contains("action=get_banners")) {
                val mockBannersResponse = \"\"\"
                    {
                        "status": "success",
                        "success": true,
                        "data": [
                            {"id": 1, "title": "50% Off Laundry", "image_url": "https://img.freepik.com/free-vector/realistic-laundry-service-sale-banner-template_23-2150337855.jpg"},
                            {"id": 2, "title": "Premium Dry Cleaning", "image_url": "https://img.freepik.com/free-vector/dry-cleaning-service-banner-template_23-2149866185.jpg"}
                        ]
                    }
                \"\"\".trimIndent()
                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(mockBannersResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            }
"""
    content = content.replace("            if (url.contains(\"action=get_categories\")) {", mock_logic + "            if (url.contains(\"action=get_categories\")) {")
    with open("app/src/main/java/com/example/data/remote/RetrofitClient.kt", "w") as f:
        f.write(content)
