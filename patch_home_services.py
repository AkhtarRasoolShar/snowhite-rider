with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

if "import com.example.data.model.ServiceItem" not in content:
    content = content.replace("import com.example.data.model.Product", "import com.example.data.model.Product\nimport com.example.data.model.ServiceItem")

if "services: List<ServiceItem> = emptyList()," not in content:
    content = content.replace(
        "activeOrder: OrderEntity?,",
        "activeOrder: OrderEntity?,\n    services: List<ServiceItem> = emptyList(),"
    )

content = content.replace(
"""            // "Get Started" Dual Service Grid (Laundry/Dry Cleaning & Products)
            item {
                DualServiceGrid(
                    onLaundryClick = onLaundryClick,
                    onProductsClick = onProductsClick
                )
            }""",
"""            // Dynamic Services Grid
            item {
                DualServiceGrid(
                    services = services,
                    onLaundryClick = onLaundryClick,
                    onProductsClick = onProductsClick
                )
            }"""
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
