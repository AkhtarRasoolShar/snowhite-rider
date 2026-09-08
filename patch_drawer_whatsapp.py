import re

with open("app/src/main/java/com/example/ui/components/DrawerMenuContent.kt", "r") as f:
    content = f.read()

if "whatsappNumber: String = \"\"," not in content:
    content = content.replace(
        "appName: String = \"SnowWhite\",",
        "appName: String = \"SnowWhite\",\n    whatsappNumber: String = \"\","
    )

old_wa = """                DrawerMenuItem(
                    icon = Icons.Default.Call,
                    label = "WhatsApp Support (+92 301 8637011)",
                    isSelected = false,"""

new_wa = """                DrawerMenuItem(
                    icon = Icons.Default.Call,
                    label = if (whatsappNumber.isNotBlank()) "WhatsApp Support ($whatsappNumber)" else "WhatsApp Support",
                    isSelected = false,"""

content = content.replace(old_wa, new_wa)

with open("app/src/main/java/com/example/ui/components/DrawerMenuContent.kt", "w") as f:
    f.write(content)
print("Patched WhatsApp")
