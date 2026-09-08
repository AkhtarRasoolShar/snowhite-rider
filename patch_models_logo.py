with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

content = content.replace(
    "val currency: String? = \"PKR\"",
    "val currency: String? = \"PKR\",\n    val logo_url: String? = null"
)

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)
