package com.example.ui.screens

import com.example.openSupportWhatsApp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DrawerMenuContent
import com.example.ui.components.InAppReviewDialog
import com.example.ui.components.InvoiceDialog
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.ReviewsDialog
import com.example.ui.components.TopAppBarHeader
import com.example.ui.screens.InvoiceScreen
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.SoftLightBlue
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.SnowWhiteViewModel
import kotlinx.coroutines.launch

@Composable
fun MainContainer(
    viewModel: SnowWhiteViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val launchWhatsAppSupport = {
        openSupportWhatsApp(context)
    }

    LaunchedEffect(uiState.currentScreen) {
        focusManager.clearFocus()
    }

    LaunchedEffect(uiState.snackbarMessage) {
        val msg = uiState.snackbarMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbarMessage()
        }
    }

    if (uiState.isReviewsDialogOpen) {
        ReviewsDialog(
            onDismiss = { viewModel.toggleReviewsDialog(false) }
        )
    }

    if (uiState.showOrderSuccessDialog) {
        OrderSuccessDialog(
            orderId = uiState.lastSubmittedOrderId,
            onViewOrdersClick = { viewModel.dismissSuccessDialogAndNavigateToOrders() },
            onViewInvoiceClick = { viewModel.showInvoiceForLastOrder() },
            onEmailInvoiceClick = {
                val orderId = uiState.lastSubmittedOrderId ?: "SW-1001"
                viewModel.sendEmailInvoice(
                    email = "akhtarhussain1452@gmail.com",
                    orderId = orderId,
                    invoiceNumber = "INV-2026-$orderId"
                ) { success, msg, link ->
                    viewModel.showInvoiceForLastOrder()
                }
            }
        )
    }

    val activeInvoice = uiState.activeInvoiceData
    if (activeInvoice != null) {
        InvoiceDialog(
            invoiceData = activeInvoice,
            onDismiss = { viewModel.dismissInvoice() }
        )
    }

    if (uiState.showInAppReviewDialog) {
        InAppReviewDialog(
            onDismiss = { viewModel.dismissInAppReviewDialog() },
            onSubmitReview = { rating -> viewModel.submitInAppReview(rating) }
        )
    }

    when (val screen = uiState.currentScreen) {
        Screen.Splash -> {
            Box(modifier = Modifier.fillMaxSize()) {
                SplashScreen(
                    onSplashFinished = { viewModel.handleSplashFinished() }
                )
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Screen.Login -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LoginScreen(
                    isLoading = uiState.isAuthLoading,
                    onLoginClick = { phone, pass -> viewModel.loginUser(phone, pass) },
                    onNavigateToSignUp = { viewModel.navigateTo(Screen.SignUp) },
                    onBackClick = { viewModel.navigateTo(Screen.Home) }
                )
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Screen.SignUp -> {
            Box(modifier = Modifier.fillMaxSize()) {
                SignUpScreen(
                    isLoading = uiState.isAuthLoading,
                    onSignUpClick = { name, phone, pass -> viewModel.registerUser(name, phone, pass) },
                    onNavigateToLogin = { viewModel.navigateTo(Screen.Login) },
                    onBackClick = { viewModel.navigateTo(Screen.Home) }
                )
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        is Screen.OrderChat -> {
            Box(modifier = Modifier.fillMaxSize()) {
                OrderChatScreen(
                    orderId = screen.orderId,
                    mySenderType = screen.mySenderType,
                    mySenderId = uiState.currentCustomerId,
                    orderCode = screen.orderCode,
                    otherPartyName = screen.otherPartyName,
                    onBackClick = { viewModel.navigateTo(Screen.OrderHistory) }
                )
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        else -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DrawerMenuContent(
                        currentRoute = when (uiState.currentScreen) {
                            Screen.Home -> "home"
                            Screen.ServiceTierSelect, Screen.ItemSelection, Screen.PickupScheduling -> "book"
                            Screen.ProductsShop -> "products"
                            is Screen.LiveOrderTracking -> "tracking"
                            Screen.OrderHistory -> "history"
                            Screen.PriceList -> "pricelist"
                            Screen.Profile -> "profile"
                            Screen.NotificationSettings -> "notifications"
                            else -> "home"
                        },
                        isLoggedIn = uiState.isLoggedIn,
                        userName = uiState.userProfileName,
                        userPhone = uiState.userProfilePhone,
                        onNavigateHome = { viewModel.navigateTo(Screen.Home) },
                        onNavigateBookLaundry = { viewModel.navigateTo(Screen.ServiceTierSelect) },
                        onNavigateProducts = { viewModel.navigateTo(Screen.ProductsShop) },
                        onNavigateTracking = {
                            val activeId = uiState.currentActiveOrder?.orderId ?: "SW-DEMO"
                            viewModel.navigateTo(Screen.LiveOrderTracking(activeId))
                        },
                        onNavigateHistory = { viewModel.navigateTo(Screen.OrderHistory) },
                        onNavigatePriceList = { viewModel.navigateTo(Screen.PriceList) },
                        onNavigateProfile = { viewModel.navigateTo(Screen.Profile) },
                        onNavigateNotificationSettings = { viewModel.navigateTo(Screen.NotificationSettings) },
                        onNavigateSupportWhatsApp = { launchWhatsAppSupport() },
                        onLoginClick = { viewModel.navigateTo(Screen.Login) },
                        onLogout = { viewModel.logoutUser() },
                        onCloseDrawer = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars,
                    containerColor = Color(0xFFF7F9FC),
                    topBar = {
                        TopAppBarHeader(
                            onOpenDrawer = {
                                scope.launch { drawerState.open() }
                            },
                            onOpenCart = {
                                viewModel.navigateTo(Screen.CartCheckout)
                            },
                            onOpenNotifications = {
                                viewModel.navigateTo(Screen.NotificationSettings)
                            },
                            onOpenProfile = {
                                viewModel.navigateTo(Screen.Profile)
                            },
                            cartBadgeCount = viewModel.totalCartBadgeCount,
                            notificationCount = uiState.notificationCount
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.White,
                            tonalElevation = 8.dp,
                            modifier = Modifier.testTag("bottom_navigation_bar")
                        ) {
                            val currentScreen = uiState.currentScreen

                            NavigationBarItem(
                                selected = currentScreen is Screen.Home,
                                onClick = { viewModel.navigateTo(Screen.Home) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DeepBlue,
                                    selectedTextColor = DeepBlue,
                                    indicatorColor = SoftLightBlue
                                ),
                                modifier = Modifier.testTag("nav_item_home")
                            )

                            NavigationBarItem(
                                selected = currentScreen is Screen.ServiceTierSelect || currentScreen is Screen.ItemSelection || currentScreen is Screen.PickupScheduling,
                                onClick = { viewModel.navigateTo(Screen.ServiceTierSelect) },
                                icon = { Icon(Icons.Default.LocalLaundryService, contentDescription = "Book") },
                                label = { Text("Book", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DeepBlue,
                                    selectedTextColor = DeepBlue,
                                    indicatorColor = SoftLightBlue
                                ),
                                modifier = Modifier.testTag("nav_item_book")
                            )

                            NavigationBarItem(
                                selected = currentScreen is Screen.ProductsShop,
                                onClick = { viewModel.navigateTo(Screen.ProductsShop) },
                                icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Products") },
                                label = { Text("Products", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DeepBlue,
                                    selectedTextColor = DeepBlue,
                                    indicatorColor = SoftLightBlue
                                ),
                                modifier = Modifier.testTag("nav_item_products")
                            )

                            NavigationBarItem(
                                selected = currentScreen is Screen.LiveOrderTracking,
                                onClick = {
                                    val activeId = uiState.currentActiveOrder?.orderId ?: "SW-78249"
                                    viewModel.navigateTo(Screen.LiveOrderTracking(activeId))
                                },
                                icon = { Icon(Icons.Default.TrackChanges, contentDescription = "Tracking") },
                                label = { Text("Tracking", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DeepBlue,
                                    selectedTextColor = DeepBlue,
                                    indicatorColor = SoftLightBlue
                                ),
                                modifier = Modifier.testTag("nav_item_tracking")
                            )

                            NavigationBarItem(
                                selected = currentScreen is Screen.OrderHistory,
                                onClick = { viewModel.navigateToOrdersTab() },
                                icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = "Orders") },
                                label = { Text("Orders", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = DeepBlue,
                                    selectedTextColor = DeepBlue,
                                    indicatorColor = SoftLightBlue
                                ),
                                modifier = Modifier.testTag("nav_item_orders")
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val currentScreen = uiState.currentScreen) {
                            Screen.Home -> HomeScreen(
                                activeOrder = uiState.currentActiveOrder,
                                categories = uiState.categories,
                                products = uiState.products,
                                selectedCategoryId = uiState.selectedCategoryId,
                                isRefreshing = uiState.isLoadingCategoriesAndProducts || uiState.isFetchingOrders,
                                onRefresh = { viewModel.refreshCatalogAndOrders() },
                                onCategorySelected = { viewModel.selectCategoryTab(it) },
                                getProductQuantity = { viewModel.getProductQuantity(it) },
                                onAddProduct = { viewModel.addProductToCart(it) },
                                onRemoveProduct = { viewModel.removeProductFromCart(it) },
                                totalCartPricePKR = viewModel.totalCartPricePKR,
                                totalCartCount = viewModel.totalCartBadgeCount,
                                onBookNowClick = { viewModel.navigateTo(Screen.ServiceTierSelect) },
                                onLaundryClick = { viewModel.navigateTo(Screen.ServiceTierSelect) },
                                onProductsClick = { viewModel.navigateTo(Screen.ProductsShop) },
                                onReviewsClick = { viewModel.toggleReviewsDialog(true) },
                                onTrackActiveOrderClick = {
                                    val activeId = uiState.currentActiveOrder?.orderId ?: "SW-78249"
                                    viewModel.navigateTo(Screen.LiveOrderTracking(activeId))
                                },
                                onProceedToSchedule = { viewModel.navigateTo(Screen.CartCheckout) }
                            )

                            Screen.ServiceTierSelect -> ServiceTierSelectorScreen(
                                selectedTier = uiState.selectedServiceTier,
                                onTierSelected = { viewModel.selectServiceTier(it) },
                                onContinueClick = { viewModel.navigateTo(Screen.ItemSelection) },
                                onBackClick = { viewModel.navigateTo(Screen.Home) }
                            )

                            Screen.ItemSelection -> ItemSelectionScreen(
                                selectedCategory = uiState.selectedCategoryTab,
                                onCategorySelected = { viewModel.selectCategoryTab(it) },
                                searchQuery = uiState.searchQuery,
                                onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                                selectedServiceTier = uiState.selectedServiceTier,
                                getItemQuantity = { viewModel.getItemQuantity(it) },
                                onAddGarment = { viewModel.addGarmentToCart(it) },
                                onRemoveGarment = { viewModel.removeGarmentFromCart(it) },
                                totalCartCount = viewModel.totalCartBadgeCount,
                                totalCartPricePKR = viewModel.totalCartPricePKR,
                                onProceedToSchedule = { viewModel.proceedToCheckout() },
                                onBackClick = { viewModel.navigateTo(Screen.ServiceTierSelect) }
                            )

                            Screen.PickupScheduling -> PickupSchedulingScreen(
                                pickupSchedule = uiState.pickupSchedule,
                                onScheduleUpdated = { area, addr, date, slot, notes ->
                                    viewModel.updatePickupSchedule(area, addr, date, slot, notes)
                                },
                                totalCartCount = viewModel.totalCartBadgeCount,
                                totalPricePKR = viewModel.totalCartPricePKR,
                                isSubmitting = uiState.isSubmittingOrder,
                                onConfirmOrderClick = { viewModel.createAndSubmitOrder() },
                                onBackClick = { viewModel.navigateTo(Screen.CartCheckout) }
                            )

                            is Screen.LiveOrderTracking -> LiveOrderTrackingScreen(
                                order = uiState.currentActiveOrder,
                                remoteOrder = uiState.remoteOrders.find { it.displayOrderId == currentScreen.orderId } ?: uiState.remoteOrders.firstOrNull(),
                                onChatWithRiderClick = { id, code, rider ->
                                    viewModel.openOrderChat(id, code, rider)
                                },
                                onBackToHomeClick = { viewModel.navigateTo(Screen.Home) }
                            )

                            Screen.ProductsShop -> ProductsShopScreen(
                                getProductQuantity = { viewModel.getProductQuantity(it) },
                                onAddProduct = { viewModel.addProductToCart(it) },
                                onRemoveProduct = { viewModel.removeProductFromCart(it) },
                                totalCartCount = viewModel.totalCartBadgeCount,
                                totalCartPricePKR = viewModel.totalCartPricePKR,
                                onViewCartClick = { viewModel.navigateTo(Screen.CartCheckout) },
                                onBackClick = { viewModel.navigateTo(Screen.Home) }
                            )

                            Screen.CartCheckout -> CartCheckoutScreen(
                                cartItems = uiState.cartItems,
                                totalBadgeCount = viewModel.totalCartBadgeCount,
                                totalPricePKR = viewModel.totalCartPricePKR,
                                onAddGarment = { viewModel.addGarmentToCart(it) },
                                onRemoveGarment = { viewModel.removeGarmentFromCart(it) },
                                onAddProduct = { viewModel.addProductToCart(it) },
                                onRemoveProduct = { viewModel.removeProductFromCart(it) },
                                onProceedToSchedule = { viewModel.proceedToCheckout() },
                                onContinueShopping = { viewModel.navigateTo(Screen.ServiceTierSelect) },
                                onViewPreOrderInvoice = { viewModel.navigateToInvoice(isPreOrderQuote = true) },
                                onBackClick = { viewModel.navigateTo(Screen.Home) }
                            )

                            Screen.OrderHistory -> OrderHistoryScreen(
                                remoteOrders = uiState.remoteOrders,
                                localOrdersList = uiState.pastOrdersList,
                                isFetchingOrders = uiState.isFetchingOrders,
                                onRefreshOrders = { viewModel.fetchCustomerOrders(isSilent = false) },
                                onFetchOrders = { id -> viewModel.fetchOrders(id, isSilent = true) },
                                onSelectOrder = { orderId ->
                                    viewModel.navigateTo(Screen.LiveOrderTracking(orderId))
                                },
                                onBookNewOrderClick = { viewModel.navigateTo(Screen.ServiceTierSelect) },
                                onViewInvoice = { remote, local ->
                                    viewModel.showInvoiceForOrder(remote, local)
                                },
                                onReorder = { remote, local ->
                                    viewModel.reorderOrder(remote, local)
                                },
                                onChatWithRider = { id, code, rider ->
                                    viewModel.openOrderChat(id, code, rider)
                                },
                                onBackClick = { viewModel.navigateTo(Screen.Home) }
                            )

                            Screen.PriceList -> PriceListScreen(
                                servicesList = uiState.servicesList,
                                isLoading = uiState.isLoadingServices,
                                onRefresh = { viewModel.fetchServices() },
                                onBackClick = { viewModel.navigateTo(Screen.Home) },
                                onBookNowClick = { viewModel.navigateTo(Screen.ServiceTierSelect) }
                            )

                            Screen.Profile -> ProfileScreen(
                                userName = uiState.userProfileName,
                                userPhone = uiState.userProfilePhone,
                                savedAddress = uiState.userAddress,
                                savedDeliveryAddress = uiState.userDeliveryAddress,
                                onSaveAddress = { viewModel.saveUserAddress(it) },
                                onSaveDeliveryAddress = { viewModel.saveUserDeliveryAddress(it) },
                                onOpenMapPicker = { viewModel.navigateTo(Screen.MapPicker) },
                                onOpenNotificationSettings = { viewModel.navigateTo(Screen.NotificationSettings) },
                                onWhatsAppSupportClick = { launchWhatsAppSupport() },
                                onLogoutClick = { viewModel.logoutUser() },
                                onBackClick = { viewModel.navigateTo(Screen.Home) }
                            )

                            Screen.NotificationSettings -> NotificationSettingsScreen(
                                isPickupRemindersEnabled = uiState.isNotifPickupRemindersEnabled,
                                isStatusUpdatesEnabled = uiState.isNotifStatusUpdatesEnabled,
                                isDeliveryAlertsEnabled = uiState.isNotifDeliveryAlertsEnabled,
                                isPromosEnabled = uiState.isNotifPromosEnabled,
                                isWhatsappSyncEnabled = uiState.isNotifWhatsappSyncEnabled,
                                onToggleSetting = { type, enabled -> viewModel.updateNotificationSetting(type, enabled) },
                                onSendTestPushClick = { viewModel.sendTestNotification() },
                                onBackClick = { viewModel.navigateTo(Screen.Profile) }
                            )

                            Screen.MapPicker -> MapPickerScreen(
                                initialArea = uiState.pickupSchedule.area,
                                initialStreetAddress = uiState.pickupSchedule.streetAddress,
                                onLocationConfirmed = { area, street ->
                                    viewModel.updatePickupSchedule(area, street, null, null, null)
                                    viewModel.saveUserAddress(if (street.contains(area)) street else "$street, $area")
                                    viewModel.navigateTo(Screen.PickupScheduling)
                                },
                                onBackClick = { viewModel.navigateTo(Screen.PickupScheduling) }
                            )

                            is Screen.InvoiceView -> {
                                val invoiceData = uiState.activeInvoiceData ?: viewModel.generateCartInvoice()
                                InvoiceScreen(
                                    invoiceData = invoiceData,
                                    isPreOrderQuote = currentScreen.isPreOrderQuote,
                                    userEmail = "akhtarhussain1452@gmail.com",
                                    onBackClick = {
                                        if (currentScreen.isPreOrderQuote) viewModel.navigateTo(Screen.CartCheckout)
                                        else viewModel.navigateTo(Screen.OrderHistory)
                                    },
                                    onSendEmail = { email, orderId, invNum, callback ->
                                        viewModel.sendEmailInvoice(email, orderId, invNum, callback)
                                    },
                                    onPrimaryAction = {
                                        if (currentScreen.isPreOrderQuote) viewModel.navigateTo(Screen.PickupScheduling)
                                        else viewModel.navigateTo(Screen.OrderHistory)
                                    }
                                )
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
