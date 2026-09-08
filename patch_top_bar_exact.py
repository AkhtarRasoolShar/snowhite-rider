import re

with open("app/src/main/java/com/example/ui/components/TopAppBarHeader.kt", "r") as f:
    content = f.read()

old_text = """                    Text(
                        text = appName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF03045E),
                        modifier = Modifier.testTag("app_title_text")
                    )"""

new_text = """                    Text(
                        text = appName.ifBlank { "Laundry App" },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.testTag("app_title_text")
                    )"""

content = content.replace(old_text, new_text)

with open("app/src/main/java/com/example/ui/components/TopAppBarHeader.kt", "w") as f:
    f.write(content)
print("Patched Exact TopAppBarHeader")
