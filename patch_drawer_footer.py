import re

with open("app/src/main/java/com/example/ui/components/DrawerMenuContent.kt", "r") as f:
    content = f.read()

old_footer = """                Text(
                    text = "Snowhite DRYCLEANERS v7.0 • Karachi",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )"""

new_footer = """                Text(
                    text = "$appName v7.0",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )"""

content = content.replace(old_footer, new_footer)

with open("app/src/main/java/com/example/ui/components/DrawerMenuContent.kt", "w") as f:
    f.write(content)
print("Patched Footer")
