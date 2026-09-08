with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "r") as f:
    content = f.read()

content = content.replace("it.category_name?.contains", "it.category_name?.toString()?.contains")

with open("app/src/main/java/com/example/ui/screens/MainContainer.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/data/model/Models.kt", "r") as f:
    content = f.read()

content = content.replace(
    "@SerializedName(\"description\") val description: String? = null",
    "@SerializedName(\"description\") val description: String? = null,\n    @SerializedName(\"category_name\") val category_name: String? = null"
)

with open("app/src/main/java/com/example/data/model/Models.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "r") as f:
    content = f.read()

content = content.replace("product.name", "product.name ?: \"\"")
content = content.replace("product.description", "product.description ?: \"\"")
content = content.replace("product.price?.toDoubleOrNull()?.toInt() ?: 0", "product.price?.toDoubleOrNull()?.toInt() ?: 0") # already correct? wait.
content = content.replace("${product.rating} • ${product.volumeOrQty}", "")
content = content.replace("product.price?.toDoubleOrNull", "product.price?.toString()?.toDoubleOrNull")

with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "w") as f:
    f.write(content)
