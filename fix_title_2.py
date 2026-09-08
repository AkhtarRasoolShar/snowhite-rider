import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

# Let's fix 'ths' which likely meant 'this' in context of a typo in the title or message that I might have introduced or left from earlier.
# Looking at the code:
# message = "Your Order #${displayId} is now ${(order.status ?: "PROCESSING").replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}."
# If I look closely, the user might be referring to "ths" from my earlier message, or it's a general typo.
# Wait, I didn't introduce "ths". 

# Could "ths" mean "this"? "correct this"? The user simply says "correct ths". 
# Maybe they noticed the time says "Just now" for promos but they want something else?
# Let's look at the generated output from earlier. 
# Ah, I replaced "Order #${displayId} is now..." with "Your Order #${displayId} is now..."

# Let's change it to something super generic and clean just to be safe.
new_message = 'message = "Order #${displayId} status updated to: ${(order.status ?: "Processing").replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}",'
content = content.replace('message = "Your Order #${displayId} is now ${(order.status ?: "PROCESSING").replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}.",', new_message)


with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
