import re
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Remove Specialty Garment Care
content = re.sub(
    r"// Specialty Garment Care Bento Section\s*item\s*\{.*?(?=\s*// Why Choose SnoWhite Feature Cards)",
    "",
    content,
    flags=re.DOTALL
)

# Remove Why Choose SnoWhite Feature Cards
content = re.sub(
    r"// Why Choose SnoWhite Feature Cards\s*item\s*\{.*?(?=\s*\}\s*\n\})", # until the end of LazyColumn
    "",
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
