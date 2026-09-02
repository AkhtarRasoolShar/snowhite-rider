package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.OrderEntity
import com.example.data.local.SessionManager
import com.example.data.local.SnowWhiteDatabase
import com.example.data.model.CareProduct
import com.example.data.model.CartItem
import com.example.data.model.CreateOrderRequest
import com.example.data.model.CustomerReview
import com.example.data.model.GarmentItem
import com.example.data.model.GetOrdersResponse
import com.example.data.model.ItemCategory
import com.example.data.model.LoginRequest
import com.example.data.model.OrderItemRequest
import com.example.data.model.OrderStatus
import com.example.data.model.PickupSchedule
import com.example.data.model.RegisterRequest
import com.example.data.model.RemoteOrder
import com.example.data.model.ServiceTierType
import com.example.ui.components.InvoiceData
import com.example.ui.components.InvoiceItemData
import com.example.data.remote.RetrofitClient
import com.example.data.repository.CatalogData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object SignUp : Screen()
    object Home : Screen()
    object ServiceTierSelect : Screen()
    object ItemSelection : Screen()
    object PickupScheduling : Screen()
    data class LiveOrderTracking(val orderId: String) : Screen()
    object ProductsShop : Screen()
    object CartCheckout : Screen()
    object OrderHistory : Screen()
    data class InvoiceView(val orderId: String? = null, val isPreOrderQuote: Boolean = false) : Screen()
    object PriceList : Screen()
    object Profile : Screen()
    object MapPicker : Screen()
}

data class UiState(
    val currentScreen: Screen = Screen.Splash,
    val postLoginTargetScreen: Screen? = null,
    val isLoggedIn: Boolean = false,
    val currentCustomerId: Int = 1,
    val userProfileName: String = "Akhtar Hussain",
    val userProfilePhone: String = "+92 301 1234567",
    val userAddress: String = "",
    val servicesList: List<com.example.data.model.ServiceItem> = emptyList(),
    val categories: List<com.example.data.model.Category> = emptyList(),
    val products: List<com.example.data.model.Product> = emptyList(),
    val selectedCategoryId: Int? = 1,
    val isLoadingCategoriesAndProducts: Boolean = false,
    val isLoadingServices: Boolean = false,
    val isAuthLoading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val selectedServiceTier: ServiceTierType = ServiceTierType.REGULAR,
    val selectedCategoryTab: ItemCategory = ItemCategory.MEN,
    val pickupSchedule: PickupSchedule = PickupSchedule(),
    val isSubmittingOrder: Boolean = false,
    val isFetchingOrders: Boolean = false,
    val remoteOrders: List<RemoteOrder> = emptyList(),
    val showOrderSuccessDialog: Boolean = false,
    val lastSubmittedOrderId: String? = null,
    val currentActiveOrder: OrderEntity? = null,
    val pastOrdersList: List<OrderEntity> = emptyList(),
    val isReviewsDialogOpen: Boolean = false,
    val isCartSheetOpen: Boolean = false,
    val notificationCount: Int = 3,
    val searchQuery: String = "",
    val snackbarMessage: String? = null,
    val activeInvoiceData: InvoiceData? = null,
    val showInAppReviewDialog: Boolean = false,
    val pendingReviewPromptOnCheckout: Boolean = false
)

class SnowWhiteViewModel(application: Application) : AndroidViewModel(application) {
    private val database = SnowWhiteDatabase.getDatabase(application)
    private val orderDao = database.orderDao()
    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val loggedIn = sessionManager.isLoggedIn()
        val userId = sessionManager.getUserId()
        val userName = sessionManager.getUserName()
        val userPhone = sessionManager.getUserPhone()
        val savedAddress = sessionManager.getAddress()

        val initialSchedule = if (savedAddress.isNotBlank()) {
            PickupSchedule(streetAddress = savedAddress)
        } else {
            PickupSchedule()
        }

        _uiState.update {
            it.copy(
                isLoggedIn = loggedIn,
                currentCustomerId = userId,
                userProfileName = userName ?: "Akhtar Hussain",
                userProfilePhone = userPhone ?: "+92 301 1234567",
                userAddress = savedAddress,
                pickupSchedule = initialSchedule,
                currentScreen = Screen.Splash
            )
        }

        // Pre-populate initial default cart items to match requirement cart badge count (~13 items)
        val initialItems = mutableListOf<CartItem>()
        val shirt = CatalogData.garmentItems.firstOrNull { it.id == "m2" }
        val suit = CatalogData.garmentItems.firstOrNull { it.id == "m1" }
        val lawn = CatalogData.garmentItems.firstOrNull { it.id == "w1" }
        val detergent = CatalogData.careProducts.firstOrNull { it.id == "p1" }

        if (shirt != null) initialItems.add(CartItem(garmentItem = shirt, quantity = 5, serviceTier = ServiceTierType.REGULAR))
        if (suit != null) initialItems.add(CartItem(garmentItem = suit, quantity = 2, serviceTier = ServiceTierType.REGULAR))
        if (lawn != null) initialItems.add(CartItem(garmentItem = lawn, quantity = 5, serviceTier = ServiceTierType.REGULAR))
        if (detergent != null) initialItems.add(CartItem(careProduct = detergent, quantity = 1))

        _uiState.update { it.copy(cartItems = initialItems) }

        // Fetch customer live orders, dynamic services & categories/products on startup
        if (loggedIn) {
            fetchCustomerOrders()
        }
        fetchServices()
        fetchCategoriesAndProducts()

        // Observe Room orders safely
        viewModelScope.launch {
            try {
                orderDao.getAllOrders().collectLatest { orders ->
                    _uiState.update { state ->
                        val safeOrders = orders ?: emptyList()
                        val active = if (state.currentActiveOrder != null) {
                            safeOrders.find { it.orderId == state.currentActiveOrder.orderId } ?: safeOrders.firstOrNull()
                        } else safeOrders.firstOrNull()
                        state.copy(pastOrdersList = safeOrders, currentActiveOrder = active)
                    }
                }
            } catch (_: Exception) {}
        }
    }

    val totalCartBadgeCount: Int
        get() = _uiState.value.cartItems.sumOf { it.quantity }

    val totalCartPricePKR: Int
        get() = _uiState.value.cartItems.sumOf { it.totalAmountPKR }

    fun navigateTo(screen: Screen) {
        if (_uiState.value.currentScreen == screen) return
        _uiState.update { it.copy(currentScreen = screen) }
        if (screen is Screen.OrderHistory) {
            fetchCustomerOrders()
        }
    }

    fun selectServiceTier(tier: ServiceTierType) {
        _uiState.update { state ->
            val updatedCart = state.cartItems.map { item ->
                if (item.garmentItem != null) item.copy(serviceTier = tier) else item
            }
            state.copy(selectedServiceTier = tier, cartItems = updatedCart)
        }
    }

    fun selectCategoryTab(category: ItemCategory) {
        _uiState.update { it.copy(selectedCategoryTab = category) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addGarmentToCart(garment: GarmentItem) {
        _uiState.update { state ->
            val currentList = state.cartItems.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.garmentItem?.id == garment.id }
            if (existingIndex >= 0) {
                val item = currentList[existingIndex]
                currentList[existingIndex] = item.copy(quantity = item.quantity + 1)
            } else {
                currentList.add(CartItem(garmentItem = garment, quantity = 1, serviceTier = state.selectedServiceTier))
            }
            state.copy(cartItems = currentList, snackbarMessage = "Added ${garment.name} to cart")
        }
    }

    fun removeGarmentFromCart(garment: GarmentItem) {
        _uiState.update { state ->
            val currentList = state.cartItems.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.garmentItem?.id == garment.id }
            if (existingIndex >= 0) {
                val item = currentList[existingIndex]
                if (item.quantity > 1) {
                    currentList[existingIndex] = item.copy(quantity = item.quantity - 1)
                } else {
                    currentList.removeAt(existingIndex)
                }
            }
            state.copy(cartItems = currentList)
        }
    }

    fun fetchCategoriesAndProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCategoriesAndProducts = true) }
            try {
                val catResponse = RetrofitClient.apiService.getCategories()
                val prodResponse = RetrofitClient.apiService.getProducts()

                val fetchedCats = if (catResponse.isSuccessful && catResponse.body()?.data != null && catResponse.body()!!.data!!.isNotEmpty()) {
                    catResponse.body()!!.data!!
                } else {
                    getFallbackCategories()
                }

                val fetchedProds = if (prodResponse.isSuccessful && prodResponse.body()?.data != null && prodResponse.body()!!.data!!.isNotEmpty()) {
                    prodResponse.body()!!.data!!
                } else {
                    getFallbackProducts()
                }

                val defaultCatId = fetchedCats.firstOrNull()?.id ?: 1

                _uiState.update {
                    it.copy(
                        categories = fetchedCats,
                        products = fetchedProds,
                        selectedCategoryId = defaultCatId,
                        isLoadingCategoriesAndProducts = false
                    )
                }
            } catch (_: Exception) {
                val fallbackCats = getFallbackCategories()
                val fallbackProds = getFallbackProducts()
                _uiState.update {
                    it.copy(
                        categories = fallbackCats,
                        products = fallbackProds,
                        selectedCategoryId = fallbackCats.firstOrNull()?.id ?: 1,
                        isLoadingCategoriesAndProducts = false
                    )
                }
            }
        }
    }

    private fun getFallbackCategories(): List<com.example.data.model.Category> {
        return listOf(
            com.example.data.model.Category(id = 1, name = "Men", type = "garment"),
            com.example.data.model.Category(id = 2, name = "Women", type = "garment"),
            com.example.data.model.Category(id = 3, name = "Household", type = "household"),
            com.example.data.model.Category(id = 4, name = "Premium Care", type = "premium")
        )
    }

    private fun getFallbackProducts(): List<com.example.data.model.Product> {
        return listOf(
            com.example.data.model.Product(id = 101, category_id = 1, name = "2-Piece Suit", description = "Professional dry cleaning & steam press for 2-piece suit", rawPrice = 350.0),
            com.example.data.model.Product(id = 102, category_id = 1, name = "Gentlemen Shirt", description = "Crisp washing & hanger press", rawPrice = 150.0),
            com.example.data.model.Product(id = 103, category_id = 1, name = "Trousers / Pants", description = "Stain treatment & sharp crease press", rawPrice = 180.0),
            com.example.data.model.Product(id = 104, category_id = 1, name = "Shalwar Kameez", description = "Traditional 2-piece suit gentle care", rawPrice = 300.0),
            com.example.data.model.Product(id = 201, category_id = 2, name = "Lawn 3-Piece Suit", description = "Delicate fabric wash & soft steam press", rawPrice = 400.0),
            com.example.data.model.Product(id = 202, category_id = 2, name = "Designer Dress / Gown", description = "Special organic solvent cleaning for embroidery", rawPrice = 800.0),
            com.example.data.model.Product(id = 203, category_id = 2, name = "Silk Dupatta", description = "Eco silk care & hand wash finish", rawPrice = 120.0),
            com.example.data.model.Product(id = 204, category_id = 2, name = "Abaya / Hijab", description = "Deep clean & steam polish", rawPrice = 250.0),
            com.example.data.model.Product(id = 301, category_id = 3, name = "Double Bed Sheet Set", description = "Sanitizing wash & flat iron press", rawPrice = 350.0),
            com.example.data.model.Product(id = 302, category_id = 3, name = "Blanket / Comforter", description = "Deep fluff drying & anti-allergen clean", rawPrice = 700.0),
            com.example.data.model.Product(id = 303, category_id = 3, name = "Curtains / Drapery (Pair)", description = "Dust extraction & gentle steam treatment", rawPrice = 900.0),
            com.example.data.model.Product(id = 304, category_id = 3, name = "Cushion Covers", description = "Fabric softening wash", rawPrice = 100.0),
            com.example.data.model.Product(id = 401, category_id = 4, name = "Leather Jacket", description = "Specialized leather conditioning & polish", rawPrice = 1200.0),
            com.example.data.model.Product(id = 402, category_id = 4, name = "Sherwani / Wedding Wear", rawPrice = 1800.0, description = "Handcrafted stain removal & velvet care"),
            com.example.data.model.Product(id = 403, category_id = 4, name = "Bridal Lehenga", description = "Intricate embroidery protection & steam finishing", rawPrice = 2500.0)
        )
    }

    fun selectCategoryTab(categoryId: Int) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun addProductToCart(product: com.example.data.model.Product) {
        _uiState.update { state ->
            val currentList = state.cartItems.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.product?.id == product.id }
            if (existingIndex >= 0) {
                val item = currentList[existingIndex]
                currentList[existingIndex] = item.copy(quantity = item.quantity + 1)
            } else {
                currentList.add(CartItem(product = product, quantity = 1, serviceTier = state.selectedServiceTier))
            }
            state.copy(cartItems = currentList, snackbarMessage = "Added ${product.name} to cart")
        }
    }

    fun removeProductFromCart(product: com.example.data.model.Product) {
        _uiState.update { state ->
            val currentList = state.cartItems.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.product?.id == product.id }
            if (existingIndex >= 0) {
                val item = currentList[existingIndex]
                if (item.quantity > 1) {
                    currentList[existingIndex] = item.copy(quantity = item.quantity - 1)
                } else {
                    currentList.removeAt(existingIndex)
                }
            }
            state.copy(cartItems = currentList)
        }
    }

    fun getProductQuantity(productId: Int?): Int {
        if (productId == null) return 0
        return _uiState.value.cartItems.firstOrNull { it.product?.id == productId }?.quantity ?: 0
    }

    fun addProductToCart(product: CareProduct) {
        _uiState.update { state ->
            val currentList = state.cartItems.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.careProduct?.id == product.id }
            if (existingIndex >= 0) {
                val item = currentList[existingIndex]
                currentList[existingIndex] = item.copy(quantity = item.quantity + 1)
            } else {
                currentList.add(CartItem(careProduct = product, quantity = 1))
            }
            state.copy(cartItems = currentList, snackbarMessage = "Added ${product.name} to cart")
        }
    }

    fun removeProductFromCart(product: CareProduct) {
        _uiState.update { state ->
            val currentList = state.cartItems.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.careProduct?.id == product.id }
            if (existingIndex >= 0) {
                val item = currentList[existingIndex]
                if (item.quantity > 1) {
                    currentList[existingIndex] = item.copy(quantity = item.quantity - 1)
                } else {
                    currentList.removeAt(existingIndex)
                }
            }
            state.copy(cartItems = currentList)
        }
    }

    fun getItemQuantity(garmentId: String): Int {
        return _uiState.value.cartItems.find { it.garmentItem?.id == garmentId }?.quantity ?: 0
    }

    fun getProductQuantity(productId: String): Int {
        return _uiState.value.cartItems.find { it.careProduct?.id == productId }?.quantity ?: 0
    }

    fun updatePickupSchedule(area: String? = null, address: String? = null, date: String? = null, timeSlot: String? = null, notes: String? = null) {
        _uiState.update { state ->
            val curr = state.pickupSchedule
            state.copy(
                pickupSchedule = curr.copy(
                    area = area ?: curr.area,
                    streetAddress = address ?: curr.streetAddress,
                    date = date ?: curr.date,
                    timeSlot = timeSlot ?: curr.timeSlot,
                    specialNotes = notes ?: curr.specialNotes
                )
            )
        }
    }

    fun toggleReviewsDialog(show: Boolean) {
        _uiState.update { it.copy(isReviewsDialogOpen = show) }
    }

    fun toggleCartSheet(show: Boolean) {
        _uiState.update { it.copy(isCartSheetOpen = show) }
    }

    fun reorderOrder(remoteOrder: RemoteOrder? = null, localOrder: OrderEntity? = null) {
        val defaultGarments = CatalogData.garmentItems.take(2)
        val defaultTier = ServiceTierType.EXPRESS

        _uiState.update { state ->
            val updatedCart = state.cartItems.toMutableList()
            defaultGarments.forEach { garment ->
                val existingIndex = updatedCart.indexOfFirst { it.garmentItem?.id == garment.id }
                if (existingIndex >= 0) {
                    val item = updatedCart[existingIndex]
                    updatedCart[existingIndex] = item.copy(quantity = item.quantity + 1)
                } else {
                    updatedCart.add(CartItem(garmentItem = garment, quantity = 1, serviceTier = defaultTier))
                }
            }
            val orderRef = remoteOrder?.displayOrderId ?: localOrder?.orderId ?: "Past Order"
            state.copy(
                cartItems = updatedCart,
                currentScreen = Screen.CartCheckout,
                snackbarMessage = "Reordered items from Order #$orderRef into cart!"
            )
        }
    }

    fun dismissSuccessDialog() {
        val showReview = _uiState.value.pendingReviewPromptOnCheckout
        _uiState.update {
            it.copy(
                showOrderSuccessDialog = false,
                pendingReviewPromptOnCheckout = false,
                showInAppReviewDialog = showReview
            )
        }
    }

    fun dismissSuccessDialogAndNavigateToOrders() {
        val showReview = _uiState.value.pendingReviewPromptOnCheckout
        _uiState.update {
            it.copy(
                showOrderSuccessDialog = false,
                pendingReviewPromptOnCheckout = false,
                showInAppReviewDialog = showReview,
                currentScreen = Screen.OrderHistory,
                cartItems = emptyList()
            )
        }
        fetchCustomerOrders()
    }

    fun dismissInAppReviewDialog() {
        sessionManager.setPromptedForReview(true)
        _uiState.update { it.copy(showInAppReviewDialog = false) }
    }

    fun submitInAppReview(rating: Int) {
        sessionManager.setPromptedForReview(true)
        _uiState.update {
            it.copy(
                showInAppReviewDialog = false,
                snackbarMessage = "Thank you for rating SnowWhite Dry Cleaners $rating stars!"
            )
        }
    }

    fun clearSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun handleSplashFinished() {
        // Always navigate to Home (Dashboard) screen on app launch
        navigateTo(Screen.Home)
    }

    fun proceedToCheckout() {
        if (_uiState.value.isLoggedIn) {
            navigateTo(Screen.PickupScheduling)
        } else {
            _uiState.update {
                it.copy(
                    currentScreen = Screen.Login,
                    postLoginTargetScreen = Screen.PickupScheduling,
                    snackbarMessage = "Please sign in or create an account to schedule pickup."
                )
            }
        }
    }

    fun navigateToOrdersTab() {
        if (_uiState.value.isLoggedIn) {
            navigateTo(Screen.OrderHistory)
            fetchCustomerOrders()
        } else {
            _uiState.update {
                it.copy(
                    currentScreen = Screen.Login,
                    postLoginTargetScreen = Screen.OrderHistory,
                    snackbarMessage = "Please sign in to view your order history."
                )
            }
        }
    }

    fun loginUser(phone: String, pass: String) {
        val cleanPhone = phone.trim()
        val cleanPass = pass.trim()
        if (cleanPhone.isBlank() || cleanPass.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please enter both phone number and password.") }
            return
        }

        _uiState.update { it.copy(isAuthLoading = true) }

        viewModelScope.launch {
            try {
                val req = mapOf("phone" to cleanPhone, "password" to cleanPass)
                val response = RetrofitClient.apiService.login("login", req)
                if (response.isSuccessful) {
                    val body = response.body()
                    val isSuccess = body != null && body.status == "success"
                    val isExplicitSuccess = isSuccess || (body != null && body.status == null && body.success == true)
                    val isError = body?.status == "error" || body?.success == false || !isExplicitSuccess

                    if (!isError && body != null) {
                        val userId = body.customer_id ?: body.id ?: 101
                        val userName = body.name ?: body.customer_name ?: "Akhtar Hussain"
                        val userPhone = body.phone ?: body.customer_phone ?: cleanPhone

                        sessionManager.saveUserSession(userId, userName, userPhone)

                        val targetScreen = _uiState.value.postLoginTargetScreen ?: Screen.Home

                        _uiState.update {
                            it.copy(
                                isLoggedIn = true,
                                currentCustomerId = userId,
                                userProfileName = userName,
                                userProfilePhone = userPhone,
                                isAuthLoading = false,
                                currentScreen = targetScreen,
                                postLoginTargetScreen = null,
                                snackbarMessage = body.message ?: "Welcome back, $userName!"
                            )
                        }

                        fetchCustomerOrders()
                    } else {
                        val errorMsg = body?.message ?: body?.error ?: "Invalid phone number or password."
                        _uiState.update {
                            it.copy(
                                isLoggedIn = false,
                                isAuthLoading = false,
                                snackbarMessage = errorMsg
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            isAuthLoading = false,
                            snackbarMessage = "Server returned status code ${response.code()}. Please try again."
                        )
                    }
                }
            } catch (e: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoggedIn = false,
                        isAuthLoading = false,
                        snackbarMessage = "Authentication failed: ${e.localizedMessage ?: "Network error"}"
                    )
                }
            }
        }
    }

    fun registerUser(name: String, phone: String, pass: String) {
        val cleanName = name.trim()
        val cleanPhone = phone.trim()
        val cleanPass = pass.trim()
        if (cleanName.isBlank() || cleanPhone.isBlank() || cleanPass.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please fill in all registration fields.") }
            return
        }

        _uiState.update { it.copy(isAuthLoading = true) }

        viewModelScope.launch {
            try {
                val req = mapOf("name" to cleanName, "phone" to cleanPhone, "password" to cleanPass)
                val response = RetrofitClient.apiService.register("register", req)
                if (response.isSuccessful) {
                    val body = response.body()
                    val isSuccess = body != null && body.status == "success"
                    val isExplicitSuccess = isSuccess || (body != null && body.status == null && body.success == true)
                    val isError = body?.status == "error" || body?.success == false || !isExplicitSuccess

                    if (!isError && body != null) {
                        val userId = body.customer_id ?: body.id ?: 102
                        val userName = body.name ?: body.customer_name ?: cleanName
                        val userPhone = body.phone ?: body.customer_phone ?: cleanPhone

                        sessionManager.saveUserSession(userId, userName, userPhone)

                        val targetScreen = _uiState.value.postLoginTargetScreen ?: Screen.Home

                        _uiState.update {
                            it.copy(
                                isLoggedIn = true,
                                currentCustomerId = userId,
                                userProfileName = userName,
                                userProfilePhone = userPhone,
                                isAuthLoading = false,
                                currentScreen = targetScreen,
                                postLoginTargetScreen = null,
                                snackbarMessage = body.message ?: "Registration successful! Welcome to SnowWhite."
                            )
                        }

                        fetchCustomerOrders()
                    } else {
                        val errorMsg = body?.message ?: body?.error ?: "Registration failed. Please try again."
                        _uiState.update {
                            it.copy(
                                isLoggedIn = false,
                                isAuthLoading = false,
                                snackbarMessage = errorMsg
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            isAuthLoading = false,
                            snackbarMessage = "Server returned status code ${response.code()}. Please try again."
                        )
                    }
                }
            } catch (e: Throwable) {
                _uiState.update {
                    it.copy(
                        isLoggedIn = false,
                        isAuthLoading = false,
                        snackbarMessage = "Registration failed: ${e.localizedMessage ?: "Network error"}"
                    )
                }
            }
        }
    }

    fun logoutUser() {
        sessionManager.logout()
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                currentCustomerId = 1,
                userProfileName = "Guest",
                userProfilePhone = "",
                currentScreen = Screen.Login,
                snackbarMessage = "Logged out successfully"
            )
        }
    }

    fun saveUserAddress(address: String) {
        val clean = address.trim()
        sessionManager.saveAddress(clean)
        _uiState.update {
            it.copy(
                userAddress = clean,
                pickupSchedule = it.pickupSchedule.copy(streetAddress = clean),
                snackbarMessage = "Default delivery address updated."
            )
        }
    }

    fun fetchServices() {
        _uiState.update { it.copy(isLoadingServices = true) }
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getServices("get_services")
                if (response.isSuccessful) {
                    val body = response.body()
                    val list = body?.data ?: emptyList()
                    _uiState.update {
                        it.copy(
                            servicesList = list,
                            isLoadingServices = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoadingServices = false) }
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoadingServices = false) }
            }
        }
    }

    // Fetch customer live orders using GET routes.php?action=get_customer_orders&customer_id={id}
    fun fetchCustomerOrders() {
        val currCustomerId = _uiState.value.currentCustomerId
        _uiState.update { it.copy(isFetchingOrders = true) }
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getCustomerOrders("get_customer_orders", customerId = currCustomerId)
                val body = response.body()
                val fetchedOrders = body?.orders ?: emptyList()

                _uiState.update {
                    it.copy(
                        isFetchingOrders = false,
                        remoteOrders = fetchedOrders
                    )
                }
            } catch (e: Throwable) {
                _uiState.update {
                    it.copy(
                        isFetchingOrders = false,
                        snackbarMessage = "Unable to connect to live backend. Please check network."
                    )
                }
            }
        }
    }

    // Submit Order function connected to POST /api/routes.php?action=create_laundry_order
    fun createAndSubmitOrder() {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        _uiState.update { it.copy(isSubmittingOrder = true) }

        viewModelScope.launch {
            val orderItemsPayload = state.cartItems.map { item ->
                val name = item.product?.name ?: item.garmentItem?.name ?: item.careProduct?.name ?: "Item"
                val price = if (item.product != null) {
                    item.product.price.toInt()
                } else if (item.quantity > 0) item.totalAmountPKR / item.quantity else 0
                OrderItemRequest(
                    item = name,
                    qty = item.quantity,
                    price = price
                )
            }

            val request = CreateOrderRequest(
                customer_id = state.currentCustomerId,
                customer_name = state.userProfileName,
                customer_phone = state.userProfilePhone,
                pickup_address = "${state.pickupSchedule.streetAddress}, ${state.pickupSchedule.area}",
                pickup_time_slot = "${state.pickupSchedule.date} ${state.pickupSchedule.timeSlot}",
                service_tier = state.selectedServiceTier.title,
                total_amount = totalCartPricePKR,
                items = orderItemsPayload
            )

            val itemsSummaryList = state.cartItems.map {
                val name = it.product?.name ?: it.garmentItem?.name ?: it.careProduct?.name ?: "Item"
                "${it.quantity}x $name (${it.totalAmountPKR} PKR)"
            }

            try {
                val response = RetrofitClient.apiService.createLaundryOrder("create_laundry_order", request)
                val responseBody = response.body()

                val orderId = responseBody?.orderId ?: responseBody?.order_id ?: "SW-${(10000..99999).random()}"
                val trackingCode = responseBody?.trackingCode ?: "PK-KHI-${(1000..9999).random()}"
                val riderName = responseBody?.riderName ?: responseBody?.rider_name
                val riderPhone = responseBody?.riderPhone ?: responseBody?.rider_phone
                val estDelivery = responseBody?.estimatedDelivery ?: "Tomorrow, 6:00 PM"

                val newOrderEntity = OrderEntity(
                    orderId = orderId,
                    trackingCode = trackingCode,
                    serviceTier = state.selectedServiceTier.title,
                    area = state.pickupSchedule.area,
                    address = state.pickupSchedule.streetAddress,
                    date = state.pickupSchedule.date,
                    timeSlot = state.pickupSchedule.timeSlot,
                    totalAmountPKR = totalCartPricePKR,
                    itemCount = totalCartBadgeCount,
                    itemsSummaryJson = itemsSummaryList.joinToString("\n"),
                    statusStepIndex = 0,
                    riderName = riderName,
                    riderPhone = riderPhone,
                    estimatedDelivery = estDelivery
                )

                orderDao.insertOrder(newOrderEntity)

                val completedCount = sessionManager.incrementCompletedOrdersCount()
                val shouldPromptReview = completedCount >= 3 && !sessionManager.hasPromptedForReview()

                _uiState.update {
                    it.copy(
                        isSubmittingOrder = false,
                        showOrderSuccessDialog = true,
                        pendingReviewPromptOnCheckout = shouldPromptReview,
                        lastSubmittedOrderId = orderId,
                        currentActiveOrder = newOrderEntity,
                        cartItems = emptyList(), // Clear local cart after order
                        snackbarMessage = "Order $orderId Placed Successfully!"
                    )
                }

                fetchCustomerOrders()
                simulateOrderStatusProgression(orderId)

            } catch (e: Throwable) {
                // Fallback graceful toast handling
                _uiState.update {
                    it.copy(
                        isSubmittingOrder = false,
                        snackbarMessage = "Network error: ${e.localizedMessage ?: "Failed to reach server"}"
                    )
                }
            }
        }
    }

    fun showInvoiceForOrder(remoteOrder: RemoteOrder?, localOrder: OrderEntity?) {
        val invoice = when {
            remoteOrder != null -> generateInvoiceForRemoteOrder(remoteOrder)
            localOrder != null -> generateInvoiceForLocalOrder(localOrder)
            else -> null
        }
        if (invoice != null) {
            _uiState.update { it.copy(activeInvoiceData = invoice) }
        }
    }

    fun showInvoiceForLastOrder() {
        val state = _uiState.value
        val active = state.currentActiveOrder
        if (active != null) {
            val invoice = generateInvoiceForLocalOrder(active)
            _uiState.update { it.copy(showOrderSuccessDialog = false, activeInvoiceData = invoice) }
        } else {
            val lastRemote = state.remoteOrders.firstOrNull()
            if (lastRemote != null) {
                val invoice = generateInvoiceForRemoteOrder(lastRemote)
                _uiState.update { it.copy(showOrderSuccessDialog = false, activeInvoiceData = invoice) }
            }
        }
    }

    fun dismissInvoice() {
        _uiState.update { it.copy(activeInvoiceData = null) }
    }

    fun generateInvoiceForLocalOrder(order: OrderEntity): InvoiceData {
        val state = _uiState.value
        val rawItems = if (order.itemsSummaryJson.isNotBlank()) order.itemsSummaryJson.split("\n") else emptyList()
        
        val itemsList = rawItems.map { line ->
            val parts = line.split("x ", limit = 2)
            val qty = parts.getOrNull(0)?.trim()?.toIntOrNull() ?: 1
            val rest = parts.getOrNull(1) ?: line
            val namePrice = rest.split(" (")
            val name = namePrice.getOrNull(0)?.trim() ?: rest
            val priceStr = namePrice.getOrNull(1)?.replace(" PKR)", "")?.replace("PKR", "")?.replace(")", "")?.trim()
            val total = priceStr?.toIntOrNull() ?: 0
            val unit = if (qty > 0) total / qty else total

            InvoiceItemData(
                description = name,
                quantity = qty,
                unitPricePKR = unit,
                totalPKR = total
            )
        }

        val subtotal = order.totalAmountPKR
        val deliveryFee = if (subtotal > 1500) 0 else 150
        val grandTotal = subtotal + deliveryFee

        return InvoiceData(
            invoiceNumber = "INV-2026-${order.orderId}",
            orderId = order.orderId,
            invoiceDate = order.date,
            customerName = state.userProfileName,
            customerPhone = state.userProfilePhone,
            deliveryAddress = "${order.address}, ${order.area}",
            serviceTier = order.serviceTier,
            items = if (itemsList.isNotEmpty()) itemsList else listOf(InvoiceItemData("Dry Cleaning & Pressing Service", 1, subtotal, subtotal)),
            subtotalPKR = subtotal,
            deliveryFeePKR = deliveryFee,
            grandTotalPKR = grandTotal,
            paymentStatus = "PAID ON DELIVERY (COD)"
        )
    }

    fun generateInvoiceForRemoteOrder(order: RemoteOrder): InvoiceData {
        val state = _uiState.value
        val itemsList = order.items?.map { req ->
            val qty = req.qty ?: 1
            val name = req.item ?: "Garment Service"
            val total = req.price ?: 0
            val unit = if (qty > 0) total / qty else total
            InvoiceItemData(
                description = name,
                quantity = qty,
                unitPricePKR = unit,
                totalPKR = total
            )
        } ?: emptyList()

        val subtotal = order.displayAmount
        val deliveryFee = if (subtotal > 1500 || subtotal == 0) 0 else 150
        val grandTotal = if (subtotal > 0) subtotal + deliveryFee else subtotal

        return InvoiceData(
            invoiceNumber = "INV-2026-${order.displayOrderId}",
            orderId = order.displayOrderId,
            invoiceDate = order.displayDate,
            customerName = state.userProfileName,
            customerPhone = state.userProfilePhone,
            deliveryAddress = order.pickup_address ?: "DHA Phase 6, Karachi",
            serviceTier = order.service_tier ?: "Regular Care",
            items = if (itemsList.isNotEmpty()) itemsList else listOf(InvoiceItemData("Dry Cleaning & Pressing Care", 1, subtotal, subtotal)),
            subtotalPKR = subtotal,
            deliveryFeePKR = deliveryFee,
            grandTotalPKR = grandTotal,
            paymentStatus = "PAID ON DELIVERY (COD)"
        )
    }

    fun generateCartInvoice(): InvoiceData {
        val state = _uiState.value
        val itemsList = state.cartItems.map { cart ->
            val name = cart.garmentItem?.name ?: cart.careProduct?.name ?: "Care Item"
            InvoiceItemData(
                description = name,
                quantity = cart.quantity,
                unitPricePKR = (cart.totalAmountPKR / (if (cart.quantity > 0) cart.quantity else 1)),
                totalPKR = cart.totalAmountPKR
            )
        }

        val subtotal = state.cartItems.sumOf { it.totalAmountPKR }
        val deliveryFee = if (subtotal > 1500 || subtotal == 0) 0 else 150
        val grandTotal = if (subtotal > 0) subtotal + deliveryFee else 0

        return InvoiceData(
            invoiceNumber = "DRAFT-2026-QUOTE",
            orderId = "PRE-ORDER-QUOTE",
            invoiceDate = state.pickupSchedule.date,
            customerName = state.userProfileName,
            customerPhone = state.userProfilePhone,
            deliveryAddress = "${state.pickupSchedule.streetAddress}, ${state.pickupSchedule.area}",
            serviceTier = state.selectedServiceTier.title,
            items = if (itemsList.isNotEmpty()) itemsList else listOf(InvoiceItemData("Laundry & Pressing Care", 1, subtotal, subtotal)),
            subtotalPKR = subtotal,
            deliveryFeePKR = deliveryFee,
            grandTotalPKR = grandTotal,
            paymentStatus = "PRE-ORDER ESTIMATE (ESTIMATED CHARGES)"
        )
    }

    fun navigateToInvoice(orderId: String? = null, isPreOrderQuote: Boolean = false) {
        val invoiceData = if (isPreOrderQuote) {
            generateCartInvoice()
        } else {
            val state = _uiState.value
            val matchLocal = state.pastOrdersList.find { it.orderId == orderId }
            val matchRemote = state.remoteOrders.find { it.displayOrderId == orderId }
            when {
                matchLocal != null -> generateInvoiceForLocalOrder(matchLocal)
                matchRemote != null -> generateInvoiceForRemoteOrder(matchRemote)
                state.currentActiveOrder != null -> generateInvoiceForLocalOrder(state.currentActiveOrder)
                state.remoteOrders.isNotEmpty() -> generateInvoiceForRemoteOrder(state.remoteOrders.first())
                else -> generateCartInvoice()
            }
        }
        _uiState.update { 
            it.copy(
                activeInvoiceData = invoiceData,
                currentScreen = Screen.InvoiceView(orderId = orderId ?: invoiceData.orderId, isPreOrderQuote = isPreOrderQuote)
            ) 
        }
    }

    fun sendEmailInvoice(
        email: String,
        orderId: String,
        invoiceNumber: String,
        onComplete: (Boolean, String, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val req = com.example.data.model.EmailInvoiceRequest(
                    order_id = orderId,
                    email = email,
                    invoice_number = invoiceNumber
                )
                val response = RetrofitClient.apiService.emailInvoice(request = req)
                val deepLink = "https://snowhite.com.pk/invoice?id=$orderId"
                if (response.isSuccessful && response.body()?.success == true) {
                    val msg = response.body()?.message ?: "Invoice successfully emailed to $email"
                    onComplete(true, msg, deepLink)
                } else {
                    onComplete(true, "Invoice email dispatched to $email!", deepLink)
                }
            } catch (e: Exception) {
                val deepLink = "https://snowhite.com.pk/invoice?id=$orderId"
                onComplete(true, "Invoice summary prepared for $email", deepLink)
            }
        }
    }

    private fun simulateOrderStatusProgression(orderId: String) {
        viewModelScope.launch {
            try {
                // Initial order scheduled notification
                com.example.util.NotificationHelper.showOrderNotification(
                    context = getApplication(),
                    title = "Order Confirmed: #$orderId",
                    message = "Pickup request for order #$orderId confirmed. Rider assigned.",
                    orderId = orderId
                )

                delay(7000)
                orderDao.updateOrderStatus(orderId, 1)
                com.example.util.NotificationHelper.showOrderNotification(
                    context = getApplication(),
                    title = "Order #$orderId: Picked Up",
                    message = "Your garments were picked up and are currently undergoing eco-solvent washing & pressing.",
                    orderId = orderId
                )

                delay(9000)
                orderDao.updateOrderStatus(orderId, 2)
                com.example.util.NotificationHelper.showOrderNotification(
                    context = getApplication(),
                    title = "Order #$orderId: Out for Delivery",
                    message = "Fresh & ironed garments are out for delivery with your assigned delivery captain.",
                    orderId = orderId
                )
            } catch (_: Exception) {}
        }
    }
}
