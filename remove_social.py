import re
with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = re.sub(
    r"// Social Proof / Rating Card\s*item\s*\{\s*SocialProofRatingCard\(\s*onReviewsClick = onReviewsClick\s*\)\s*\}",
    "",
    content
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
