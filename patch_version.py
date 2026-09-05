import re

with open("app/build.gradle.kts", "r") as f:
    content = f.read()

content = content.replace(
    "versionCode = 14",
    "versionCode = 15"
).replace(
    "versionName = \"14.0\"",
    "versionName = \"15.0\""
)

with open("app/build.gradle.kts", "w") as f:
    f.write(content)
print("Patched version")
