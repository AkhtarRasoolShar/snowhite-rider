with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "r") as f:
    content = f.read()

content = content.replace("product.name ?: \"\"", "product.name.orEmpty()")
content = content.replace("product.description ?: \"\"", "product.description.orEmpty()")

with open("app/src/main/java/com/example/ui/screens/ProductsShopScreen.kt", "w") as f:
    f.write(content)
