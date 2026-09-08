import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("""                                remoteOrders = fetchedOrders
                            )
                        }
                    }
                    generateNotifications()
                            )
                        }
                    }""", """                                remoteOrders = fetchedOrders
                            )
                        }
                    }
                    generateNotifications()""")

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
