import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

if "appName: String = \"SnowWhite\"," not in content.split("fun HomeScreen(")[1].split(")")[0]:
    content = content.replace("fun HomeScreen(\n", "fun HomeScreen(\n    appName: String = \"SnowWhite\",\n")

content = content.replace(
"""                HeroPromoBanner(
                    onBookNowClick = onBookNowClick
                )""",
"""                HeroPromoBanner(
                    appName = appName,
                    onBookNowClick = onBookNowClick
                )"""
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)

print("Patched HomeScreen")
