package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.example.data.model.Banner
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.ChevronRight
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import com.example.data.local.OrderEntity
import com.example.data.model.Category
import com.example.data.model.Product
import com.example.data.model.ServiceItem
import com.example.ui.components.DualServiceGrid
import com.example.ui.components.HeroPromoBanner
import com.example.ui.components.SocialProofRatingCard
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.GradientAccentBlue
import com.example.ui.theme.OffWhiteBg
import com.example.ui.theme.SoftLightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    appName: String = "SnowWhite",
    appBanners: List<Banner> = emptyList(),
    activeOrder: OrderEntity?,
    services: List<ServiceItem> = emptyList(),
    categories: List<Category>,
    products: List<Product>,
    selectedCategoryId: Int?,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onCategorySelected: (Int) -> Unit,
    getProductQuantity: (Int?) -> Int,
    onAddProduct: (Product) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    totalCartPricePKR: Int,
    totalCartCount: Int,
    onBookNowClick: () -> Unit,
    onLaundryClick: () -> Unit,
    onProductsClick: () -> Unit,
    onReviewsClick: () -> Unit,
    onTrackActiveOrderClick: () -> Unit,
    onProceedToSchedule: () -> Unit
) {
    var selectedCategoryTab by remember { mutableStateOf("Dry Cleaning") }
    val categoryTabs = listOf("Dry Cleaning", "Wash & Fold", "Steam Ironing", "Premium Care")

    val allProducts = remember(products) {
        val list = mutableListOf<Product>()

        // 1. Dry Cleaning items
        list.add(Product(id = 101, name = "2-Piece Suit", category_name = "Dry Cleaning", description = "Professional organic solvent dry cleaning & steam crease press", rawPrice = 350.0, image_url = "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 102, name = "Gentlemen Shirt", category_name = "Dry Cleaning", description = "Crisp hanger washing & dry steam finish", rawPrice = 150.0, image_url = "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 103, name = "Trousers / Pants", category_name = "Dry Cleaning", description = "Stain treatment & sharp crease press", rawPrice = 180.0, image_url = "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 104, name = "Shalwar Kameez", category_name = "Dry Cleaning", description = "Traditional 2-piece gentle fabric dry care", rawPrice = 300.0, image_url = "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 105, name = "Designer Dress / Gown", category_name = "Dry Cleaning", description = "Special organic solvent cleaning for delicate embroidery", rawPrice = 800.0, image_url = "https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 106, name = "Silk Dupatta", category_name = "Dry Cleaning", description = "Eco silk care & gentle solvent hand wash finish", rawPrice = 160.0, image_url = "https://images.unsplash.com/photo-1609357605129-26f69add5d6e?w=400&auto=format&fit=crop&q=60"))

        // 2. Wash & Fold items
        list.add(Product(id = 201, name = "Daily Casual T-Shirts", category_name = "Wash & Fold", description = "Hygiene antibacterial wash & compact flat fold", rawPrice = 120.0, image_url = "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 202, name = "Denim Jeans / Chinos", category_name = "Wash & Fold", description = "Deep rotary tumble wash, fabric softener & neat fold", rawPrice = 160.0, image_url = "https://images.unsplash.com/photo-1542272604-780c96856592?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 203, name = "Cotton Shalwar Kameez", category_name = "Wash & Fold", description = "Sanitizing hot wash, conditioner & crisp fold", rawPrice = 200.0, image_url = "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 204, name = "Double Bed Sheet Set", category_name = "Wash & Fold", description = "High temperature anti-allergen wash & compact roll", rawPrice = 350.0, image_url = "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 205, name = "Bath Towel Set (2x)", category_name = "Wash & Fold", description = "Fluff rinse, ultra-soft dryer finish & tidy fold", rawPrice = 180.0, image_url = "https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=400&auto=format&fit=crop&q=60"))

        // 3. Steam Ironing items
        list.add(Product(id = 301, name = "Formal Dress Shirt", category_name = "Steam Ironing", description = "Industrial steam press, collar stiffening & wrinkle-free hanger", rawPrice = 80.0, image_url = "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 302, name = "Formal Trousers / Pants", category_name = "Steam Ironing", description = "Sharp front and back crease press", rawPrice = 90.0, image_url = "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 303, name = "Shalwar Suit Press", category_name = "Steam Ironing", description = "Complete 2-piece high-temperature steam press", rawPrice = 120.0, image_url = "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 304, name = "Lawn Kurti / Shirt", category_name = "Steam Ironing", description = "Delicate steam polish for ladies lawn & linen", rawPrice = 80.0, image_url = "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 305, name = "Bed Sheet & Pillowcases", category_name = "Steam Ironing", description = "Flat roller iron press for crisp hotel finish", rawPrice = 180.0, image_url = "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=400&auto=format&fit=crop&q=60"))

        // 4. Premium Care items
        list.add(Product(id = 401, name = "Leather Jacket", category_name = "Premium Care", description = "Specialized leather conditioning, supple oil treatment & polish", rawPrice = 1500.0, image_url = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 402, name = "Sherwani / Groom Wear", category_name = "Premium Care", description = "Handcrafted stain removal, metallic thread & velvet protection", rawPrice = 1800.0, image_url = "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 403, name = "Bridal Lehenga", category_name = "Premium Care", description = "Intricate zardozi embroidery protection & steam finishing", rawPrice = 2500.0, image_url = "https://images.unsplash.com/photo-1583391733956-3750e0ff4e8b?w=400&auto=format&fit=crop&q=60"))
        list.add(Product(id = 404, name = "Heavy Wool Winter Overcoat", category_name = "Premium Care", description = "Pure wool de-linting, fabric revitalization & moth-proofing", rawPrice = 1200.0, image_url = "https://images.unsplash.com/photo-1544923246-77307dd654cb?w=400&auto=format&fit=crop&q=60"))

        // Also if backend passed custom products with category_name, include them
        if (products.isNotEmpty()) {
            products.forEach { prod ->
                if (!prod.category_name.isNullOrBlank()) {
                    list.add(prod)
                }
            }
        }

        list
    }

    val filteredProducts = remember(selectedCategoryTab, allProducts) {
        allProducts.filter { it.category_name?.equals(selectedCategoryTab, ignoreCase = true) == true }
    }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhiteBg)
            .testTag("home_screen")
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            // Active Order Banner if an order is currently in progress
            if (activeOrder != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { onTrackActiveOrderClick() }
                            .testTag("active_order_banner_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .border(1.5.dp, GradientAccentBlue, RoundedCornerShape(20.dp))
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(DeepBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrackChanges,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Active Order #${activeOrder.orderId}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = DeepBlue
                                    )
                                    Text(
                                        text = "Est. Delivery: ${activeOrder.estimatedDelivery}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Track Live",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepBlue
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = DeepBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

// Hero Promo Banner or Dynamic Slider
            item {
                if (appBanners.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { appBanners.size })
                    
                    LaunchedEffect(pagerState.currentPage) {
                        delay(3000)
                        var newPosition = pagerState.currentPage + 1
                        if (newPosition > appBanners.size - 1) newPosition = 0
                        pagerState.animateScrollToPage(newPosition)
                    }
                    
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth().height(200.dp).padding(top = 8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            pageSpacing = 8.dp
                        ) { page ->
                            val banner = appBanners[page]
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            ) {
                                AsyncImage(
                                    model = banner.image_url,
                                    contentDescription = banner.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )
                            }
                        }
                        
                        // Dots Indicator
                        Row(
                            Modifier
                                .height(24.dp)
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            repeat(appBanners.size) { iteration ->
                                val color = if (pagerState.currentPage == iteration) androidx.compose.ui.graphics.Color.DarkGray else androidx.compose.ui.graphics.Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(if (pagerState.currentPage == iteration) 8.dp else 6.dp)
                                )
                            }
                        }
                    }
                } else {
                    HeroPromoBanner(
                        appName = appName,
                        onBookNowClick = onBookNowClick
                    )
                }
            }



            // Dynamic Services Grid
            item {
                DualServiceGrid(
                    services = services,
                    retailCategories = categories.filter { it.type == "retail" },
                    onLaundryClick = onLaundryClick,
                    onProductsClick = onProductsClick
                )
            }

            

            // Categories Section Title
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Garment Services & Products",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "Select garments or care products for doorstep cleaning",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Dynamic ScrollableTabRow for Service Categories (Dry Cleaning, Wash & Fold, Steam Ironing, Premium Care)
            item {
                val primaryBrandColor = Color(0xFF00B4D8)
                val selectedIndex = categoryTabs.indexOf(selectedCategoryTab).coerceAtLeast(0)

                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = Color.White,
                    contentColor = primaryBrandColor,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        if (selectedIndex in tabPositions.indices) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                                height = 3.dp,
                                color = primaryBrandColor
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("home_category_tabs")
                ) {
                    categoryTabs.forEach { tabName ->
                        val isSelected = tabName.equals(selectedCategoryTab, ignoreCase = true)
                        Tab(
                            selected = isSelected,
                            onClick = {
                                selectedCategoryTab = tabName
                            },
                            text = {
                                Text(
                                    text = tabName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) primaryBrandColor else Color(0xFF64748B),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        )
                    }
                }
            }

            // Products List filtered strictly by selected category tab
            if (filteredProducts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No products found in $selectedCategoryTab.",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                items(filteredProducts, key = { it.id ?: 0 }) { product ->
                    val qty = getProductQuantity(product.id)
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        if (selectedCategoryTab == "Premium Care") {
                            PremiumProductCardItem(
                                product = product,
                                quantity = qty,
                                onAdd = { onAddProduct(product) },
                                onRemove = { onRemoveProduct(product) }
                            )
                        } else {
                            ProductCardItem(
                                product = product,
                                quantity = qty,
                                selectedService = selectedCategoryTab,
                                onAdd = { onAddProduct(product) },
                                onRemove = { onRemoveProduct(product) }
                            )
                        }
                    }
                }
            }
        }
    }

        // Sticky Bottom Checkout Bar (Outside the LazyColumn)
        if (totalCartCount > 0) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$totalCartCount items in cart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Rs. $totalCartPricePKR PKR",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = DeepBlue
                        )
                    }

                    Button(
                        onClick = onProceedToSchedule,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("home_sticky_checkout_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Schedule Pickup", fontWeight = FontWeight.Bold)
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * CustomerHomeScreen:
 * Alias providing the exact same robust single LazyColumn layout with weight(1f)
 * and sticky bottom button outside LazyColumn for customer home usage.
 */
@Composable
fun CustomerHomeScreen(
    appName: String = "SnowWhite",
    appBanners: List<Banner> = emptyList(),
    activeOrder: OrderEntity? = null,
    services: List<ServiceItem> = emptyList(),
    categories: List<Category> = emptyList(),
    products: List<Product> = emptyList(),
    selectedCategoryId: Int? = null,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onCategorySelected: (Int) -> Unit = {},
    getProductQuantity: (Int?) -> Int = { 0 },
    onAddProduct: (Product) -> Unit = {},
    onRemoveProduct: (Product) -> Unit = {},
    totalCartPricePKR: Int = 0,
    totalCartCount: Int = 0,
    onBookNowClick: () -> Unit = {},
    onLaundryClick: () -> Unit = {},
    onProductsClick: () -> Unit = {},
    onReviewsClick: () -> Unit = {},
    onTrackActiveOrderClick: () -> Unit = {},
    onProceedToSchedule: () -> Unit = {}
) {
    HomeScreen(
        appName = appName,
        appBanners = appBanners,
        activeOrder = activeOrder,
        services = services,
        categories = categories,
        products = products,
        selectedCategoryId = selectedCategoryId,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        onCategorySelected = onCategorySelected,
        getProductQuantity = getProductQuantity,
        onAddProduct = onAddProduct,
        onRemoveProduct = onRemoveProduct,
        totalCartPricePKR = totalCartPricePKR,
        totalCartCount = totalCartCount,
        onBookNowClick = onBookNowClick,
        onLaundryClick = onLaundryClick,
        onProductsClick = onProductsClick,
        onReviewsClick = onReviewsClick,
        onTrackActiveOrderClick = onTrackActiveOrderClick,
        onProceedToSchedule = onProceedToSchedule
    )
}

@Composable
fun ProductCardItem(
    product: Product,
    quantity: Int,
    selectedService: String,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val primaryBrandColor = Color(0xFF00B4D8)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val defaultImg = when {
                product.name?.contains("Suit", ignoreCase = true) == true -> "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=400&auto=format&fit=crop&q=60"
                product.name?.contains("Shirt", ignoreCase = true) == true -> "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=400&auto=format&fit=crop&q=60"
                product.name?.contains("Pants", ignoreCase = true) == true || product.name?.contains("Trousers", ignoreCase = true) == true -> "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=400&auto=format&fit=crop&q=60"
                product.name?.contains("Dress", ignoreCase = true) == true || product.name?.contains("Gown", ignoreCase = true) == true || product.name?.contains("Lawn", ignoreCase = true) == true -> "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400&auto=format&fit=crop&q=60"
                product.name?.contains("Bed", ignoreCase = true) == true || product.name?.contains("Sheet", ignoreCase = true) == true || product.name?.contains("Blanket", ignoreCase = true) == true -> "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=400&auto=format&fit=crop&q=60"
                product.name?.contains("Curtain", ignoreCase = true) == true -> "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=400&auto=format&fit=crop&q=60"
                else -> "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=400&auto=format&fit=crop&q=60"
            }
            val imgUrl = if (product.image_url.isNullOrBlank()) defaultImg else product.image_url

            coil.compose.AsyncImage(
                model = imgUrl,
                contentDescription = product.name,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = androidx.compose.ui.Modifier
                    .size(60.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .background(androidx.compose.ui.graphics.Color.LightGray)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.name ?: "Product",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!product.description.isNullOrBlank()) {
                    Text(
                        text = product.description,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp
                    )
                }
                Text(
                    text = "Rs. ${product.price.toInt()} PKR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBrandColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            
            // +/- Quantity Selector with Brand Colors
            if (quantity > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(primaryBrandColor.copy(alpha = 0.1f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "$quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBrandColor,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    IconButton(
                        onClick = onAdd,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onAdd,
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBrandColor.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp).testTag("add_product_${product.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "ADD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBrandColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumProductCardItem(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val primaryBrandColor = Color(0xFF00B4D8)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("premium_product_card_${product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imgUrl = product.image_url.takeIf { !it.isNullOrBlank() } ?: "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&auto=format&fit=crop&q=60"

            coil.compose.AsyncImage(
                model = imgUrl,
                contentDescription = product.name,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = androidx.compose.ui.Modifier
                    .size(60.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .background(androidx.compose.ui.graphics.Color.LightGray)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = product.name ?: "Premium Item",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "PREMIUM",
                            color = Color(0xFFD97706),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                if (!product.description.isNullOrBlank()) {
                    Text(
                        text = product.description,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp
                    )
                }
                Text(
                    text = "Rs. ${product.price.toInt()} PKR",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBrandColor
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            
            // +/- Quantity Selector with Brand Colors
            if (quantity > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(primaryBrandColor.copy(alpha = 0.1f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "$quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBrandColor,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    IconButton(
                        onClick = onAdd,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onAdd,
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBrandColor.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp).testTag("add_premium_${product.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = primaryBrandColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "ADD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBrandColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BentoCategoryTile(
    modifier: Modifier = Modifier,
    title: String,
    tag: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(18.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SoftLightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = DeepBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = tag,
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
fun FeatureHighlightCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(18.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SoftLightBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 12.sp
                )
            }
        }
    }
}
