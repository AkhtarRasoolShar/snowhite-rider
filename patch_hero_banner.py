import re

with open("app/src/main/java/com/example/ui/components/HeroPromoBanner.kt", "r") as f:
    content = f.read()

if "appName: String = \"SnowWhite\"," not in content:
    content = content.replace("fun HeroPromoBanner(\n", "fun HeroPromoBanner(\n    appName: String = \"SnowWhite\",\n")

content = content.replace(
"""                    Text(
                        text = "SNOWHITE DRYCLEANERS • SINCE 1949",
                        color = Color.White,""",
"""                    Text(
                        text = "${appName.uppercase()} • SINCE 1949",
                        color = Color.White,"""
)

with open("app/src/main/java/com/example/ui/components/HeroPromoBanner.kt", "w") as f:
    f.write(content)

print("Patched HeroPromoBanner")
