with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

slider_logic_autoscroll = """
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
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AsyncImage(
                                    model = banner.image_url,
                                    contentDescription = banner.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
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
"""

import re
content = re.sub(
    r"// Hero Promo Banner or Dynamic Slider\s*item\s*\{.*?(?=\s*// \"Get Started\")",
    slider_logic_autoscroll.strip() + "\n\n",
    content,
    flags=re.DOTALL
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
