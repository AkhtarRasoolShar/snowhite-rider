import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

old_screen = "object NotificationSettings : Screen()"
new_screen = "object NotificationSettings : Screen()\n    object Notifications : Screen()"

content = content.replace(old_screen, new_screen)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)

