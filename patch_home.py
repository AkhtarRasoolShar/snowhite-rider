with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
"""            // Dynamic Services Grid
            item {
                DualServiceGrid(
                    services = services,
                    onLaundryClick = onLaundryClick,
                    onProductsClick = onProductsClick
                )
            }""",
"""            // Dynamic Services Grid
            item {
                DualServiceGrid(
                    services = services,
                    retailCategories = categories.filter { it.type == "retail" },
                    onLaundryClick = onLaundryClick,
                    onProductsClick = onProductsClick
                )
            }"""
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
