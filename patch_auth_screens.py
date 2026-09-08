with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "r") as f:
    content = f.read()

# Update LoginScreen signature
content = content.replace(
    "onNavigateToSignUp: () -> Unit,",
    "onNavigateToSignUp: () -> Unit,\n    onNavigateToForgotPassword: () -> Unit,"
)

# Add "Forgot Password?" text
forgot_text = """                        }

                        // Forgot Password Text
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = "Forgot Password?",
                                fontSize = 12.sp,
                                color = BrightBlue,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clickable { onNavigateToForgotPassword() }
                                    .padding(vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))"""

content = content.replace("                        }\n\n                        Spacer(modifier = Modifier.height(4.dp))\n\n                        // Primary Login Button using Bright Blue", forgot_text + "\n\n                        // Primary Login Button using Bright Blue")

with open("app/src/main/java/com/example/ui/screens/AuthScreens.kt", "w") as f:
    f.write(content)
