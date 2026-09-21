package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CareProduct
import com.example.data.model.CartItem
import com.example.data.model.GarmentItem
import com.example.data.model.Product
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.GradientAccentBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.SoftLightBlue

@Composable
fun CartCheckoutScreen(
    cartItems: List<CartItem>,
    totalBadgeCount: Int,
    totalPricePKR: Int,
    onAddGarment: (GarmentItem) -> Unit,
    onRemoveGarment: (GarmentItem) -> Unit,
    onAddProduct: (CareProduct) -> Unit,
    onRemoveProduct: (CareProduct) -> Unit,
    onAddCatalogProduct: ((Product) -> Unit)? = null,
    onRemoveCatalogProduct: ((Product) -> Unit)? = null,
    onProceedToSchedule: () -> Unit,
    onContinueShopping: () -> Unit,
    onViewPreOrderInvoice: (() -> Unit)? = null,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
            .testTag("cart_checkout_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("cart_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Column {
                    Text(
                        text = "My Laundry Cart",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "$totalBadgeCount items selected",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(DeepBlue.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Rs. $totalPricePKR PKR",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = DeepBlue
                )
            }
        }

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(160.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(SoftLightBlue)
                        )
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 6.dp,
                            modifier = Modifier.size(105.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Empty Laundry Cart Illustration",
                                    tint = DeepBlue,
                                    modifier = Modifier.size(52.dp)
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = GradientAccentBlue,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Your Laundry Cart is Empty",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You haven't added any dry cleaning garments or care products yet. Select from Econo, Executive, or Express services to get started with free Karachi pickup!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = onContinueShopping,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(52.dp)
                            .testTag("browse_services_empty_cart_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalLaundryService,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Browse Laundry Services",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        } else {
            // LazyColumn with weight(1f) for Cart Items
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems) { item ->
                    val productName = item.product?.name ?: item.garmentItem?.name ?: item.careProduct?.name ?: "Unknown Item"
                    val subtitle = when {
                        item.product != null -> item.product.category_name ?: "Care Product"
                        item.garmentItem != null -> "Tier: ${item.serviceTier.title}"
                        item.careProduct != null -> item.careProduct.volumeOrQty
                        else -> ""
                    }
                    val fallbackImg = when {
                        productName.contains("Suit", ignoreCase = true) -> "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=400&auto=format&fit=crop&q=60"
                        productName.contains("Shirt", ignoreCase = true) -> "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=400&auto=format&fit=crop&q=60"
                        productName.contains("Pants", ignoreCase = true) || productName.contains("Trousers", ignoreCase = true) -> "https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=400&auto=format&fit=crop&q=60"
                        productName.contains("Dress", ignoreCase = true) || productName.contains("Gown", ignoreCase = true) -> "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400&auto=format&fit=crop&q=60"
                        productName.contains("Detergent", ignoreCase = true) -> "https://images.unsplash.com/photo-1583947215259-38e31be8751f?w=400&auto=format&fit=crop&q=60"
                        productName.contains("Conditioner", ignoreCase = true) || productName.contains("Softener", ignoreCase = true) -> "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=400&auto=format&fit=crop&q=60"
                        else -> "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=400&auto=format&fit=crop&q=60"
                    }
                    val itemImgUrl = item.product?.image_url?.takeIf { it.isNotBlank() } ?: fallbackImg

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, LightBlueBorder, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                coil.compose.AsyncImage(
                                    model = itemImgUrl,
                                    contentDescription = productName,
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = androidx.compose.ui.Modifier
                                        .size(60.dp)
                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                                        .background(androidx.compose.ui.graphics.Color.LightGray)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.product?.name ?: item.garmentItem?.name ?: item.careProduct?.name ?: "Unknown",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (subtitle.isNotBlank()) {
                                        Text(
                                            text = subtitle,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "Rs. ${item.totalAmountPKR} PKR",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepBlue,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            // (+ / -) Counter
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (item.product != null) {
                                            onRemoveCatalogProduct?.invoke(item.product)
                                        } else if (item.garmentItem != null) {
                                            onRemoveGarment(item.garmentItem)
                                        } else if (item.careProduct != null) {
                                            onRemoveProduct(item.careProduct)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SoftLightBlue)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = DeepBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = item.quantity.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                IconButton(
                                    onClick = {
                                        if (item.product != null) {
                                            onAddCatalogProduct?.invoke(item.product)
                                        } else if (item.garmentItem != null) {
                                            onAddGarment(item.garmentItem)
                                        } else if (item.careProduct != null) {
                                            onAddProduct(item.careProduct)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(DeepBlue)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }

            // Fixed Bottom Section: Totals, Invoice Button, and Proceed Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Summary Breakdown Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftLightBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", fontSize = 13.sp)
                            Text("Rs. $totalPricePKR PKR", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Doorstep Pickup & Delivery", fontSize = 13.sp)
                            Text("FREE (Karachi Promo)", fontWeight = FontWeight.Bold, color = DeepBlue, fontSize = 13.sp)
                        }
                        HorizontalDivider(color = LightBlueBorder, modifier = Modifier.padding(vertical = 3.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Rs. $totalPricePKR PKR", fontWeight = FontWeight.Black, fontSize = 16.sp, color = DeepBlue)
                        }
                    }
                }

                if (onViewPreOrderInvoice != null) {
                    OutlinedButton(
                        onClick = onViewPreOrderInvoice,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("view_preorder_invoice_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(18.dp))
                            Text("Review Itemized Charge Invoice", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepBlue)
                        }
                    }
                }

                Button(
                    onClick = onProceedToSchedule,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepBlue, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("checkout_schedule_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Proceed to Schedule Pickup", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    totalBadgeCount: Int,
    totalPricePKR: Int,
    onAddGarment: (GarmentItem) -> Unit,
    onRemoveGarment: (GarmentItem) -> Unit,
    onAddProduct: (CareProduct) -> Unit,
    onRemoveProduct: (CareProduct) -> Unit,
    onAddCatalogProduct: ((Product) -> Unit)? = null,
    onRemoveCatalogProduct: ((Product) -> Unit)? = null,
    onProceedToSchedule: () -> Unit,
    onContinueShopping: () -> Unit,
    onViewPreOrderInvoice: (() -> Unit)? = null,
    onBackClick: () -> Unit = {}
) {
    CartCheckoutScreen(
        cartItems = cartItems,
        totalBadgeCount = totalBadgeCount,
        totalPricePKR = totalPricePKR,
        onAddGarment = onAddGarment,
        onRemoveGarment = onRemoveGarment,
        onAddProduct = onAddProduct,
        onRemoveProduct = onRemoveProduct,
        onAddCatalogProduct = onAddCatalogProduct,
        onRemoveCatalogProduct = onRemoveCatalogProduct,
        onProceedToSchedule = onProceedToSchedule,
        onContinueShopping = onContinueShopping,
        onViewPreOrderInvoice = onViewPreOrderInvoice,
        onBackClick = onBackClick
    )
}
