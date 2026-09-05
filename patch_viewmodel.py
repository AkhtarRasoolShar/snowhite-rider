import re

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "r") as f:
    content = f.read()

bad_catch = """            } catch (e: Throwable) {
                Log.e("ORDERS_DEBUG", "fetchOrders network exception: ${e.message}", e)
                // CRITICAL: Preserve existing remoteOrders state on network exception
                _uiState.update {
                    it.copy(isFetchingOrders = false)
                }
            }"""

fixed_catch = """            } catch (e: Throwable) {
                Log.e("ORDERS_DEBUG", "fetchOrders network exception: ${e.message}", e)
                // Exposed error to UI to catch JSON formatting issues instantly
                _uiState.update {
                    it.copy(
                        isFetchingOrders = false,
                        snackbarMessage = "Fetch Error: ${e.message?.take(50)}"
                    )
                }
            }"""

content = content.replace(bad_catch, fixed_catch)

with open("app/src/main/java/com/example/ui/viewmodel/SnowWhiteViewModel.kt", "w") as f:
    f.write(content)
print("Patched catch block")
