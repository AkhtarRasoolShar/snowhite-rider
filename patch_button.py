import re

with open("app/src/main/java/com/example/ui/screens/LiveOrderTrackingScreen.kt", "r") as f:
    content = f.read()

bad_button_code = """                        onClick = {
                            val whatsappUrl = "https://wa.me/92$cleanPhone"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Opening WhatsApp for +92 $cleanPhone", Toast.LENGTH_SHORT).show()
                            }
                        },
                            shape = RoundedCornerShape(12.dp),"""

fixed_button_code = """                        Button(
                            onClick = {
                                val whatsappUrl = "https://wa.me/92$cleanPhone"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    Toast.makeText(context, "Opening WhatsApp for +92 $cleanPhone", Toast.LENGTH_SHORT).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),"""

content = content.replace(bad_button_code, fixed_button_code)

with open("app/src/main/java/com/example/ui/screens/LiveOrderTrackingScreen.kt", "w") as f:
    f.write(content)
print("Fixed button")
