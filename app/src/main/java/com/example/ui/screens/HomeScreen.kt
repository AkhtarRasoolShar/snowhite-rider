package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.ui.components.DualServiceGrid
import com.example.ui.components.HeroPromoBanner
import com.example.ui.components.SocialProofRatingCard
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.GradientAccentBlue
import com.example.ui.theme.LightBlueBorder
import com.example.ui.theme.OffWhiteBg
import com.example.ui.theme.SoftLightBlue

@Composable
fun HomeScreen(
    activeOrder: OrderEntity?,
    onBookNowClick: () -> Unit,
    onLaundryClick: () -> Unit,
    onProductsClick: () -> Unit,
    onReviewsClick: () -> Unit,
    onTrackActiveOrderClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhiteBg)
            .verticalScroll(rememberScrollState())
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Active Order Banner if an order is currently in progress
        if (activeOrder != null) {
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Active Order #${activeOrder.orderId}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DeepBlue
                                )
                            }
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

        // Hero Promo Banner
        HeroPromoBanner(
            onBookNowClick = onBookNowClick
        )

        // "Get Started" Dual Service Grid (Laundry/Dry Cleaning & Products)
        DualServiceGrid(
            onLaundryClick = onLaundryClick,
            onProductsClick = onProductsClick
        )

        // Social Proof / Rating Card
        SocialProofRatingCard(
            onReviewsClick = onReviewsClick
        )

        // Special Care Bento Grid Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Specialty Garment Care",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Handcrafted organic cleaning for delicate fabrics",
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(bottom = 10.dp, top = 2.dp)
            )

            // 2x2 Bento Category Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BentoCategoryTile(
                    modifier = Modifier.weight(1f),
                    title = "Suits & Formals",
                    tag = "Press & Polish",
                    icon = Icons.Default.Style,
                    onClick = onLaundryClick
                )
                BentoCategoryTile(
                    modifier = Modifier.weight(1f),
                    title = "Eastern Wear",
                    tag = "Shalwar Kameez",
                    icon = Icons.Default.DryCleaning,
                    onClick = onLaundryClick
                )
            }
        }

        // Why Choose SnowWhite Bento Feature Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Why SnowWhite Pakistan?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeatureHighlightCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Speed,
                    title = "Express 8Hr",
                    subtitle = "Same-day doorstep pickup"
                )

                FeatureHighlightCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Shield,
                    title = "German Solvents",
                    subtitle = "Eco fabric preservation"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BentoCategoryTile(
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
private fun FeatureHighlightCard(
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

