with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

if "data class Banner" not in content:
    content += "\n\ndata class Banner(\n    val id: Int? = null,\n    val title: String? = null,\n    val image_url: String? = null,\n    val is_active: Int? = 1\n)"
    with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
        f.write(content)
