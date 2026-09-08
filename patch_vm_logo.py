with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

content = content.replace(
    "currency = data[\"currency\"] ?: \"PKR\"",
    "currency = data[\"currency\"] ?: \"PKR\",\n                            logo_url = data[\"logo_url\"]"
)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
