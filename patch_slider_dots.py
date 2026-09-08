with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

slider_logic_with_dots = """
            // Hero Promo Banner or Dynamic Slider
            item {
                if (appBanners.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { appBanners.size })
                    
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
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(appBanners.size) { iteration ->
                                val color = if (pagerState.currentPage == iteration) androidx.compose.ui.graphics.Color.DarkGray else androidx.compose.ui.graphics.Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(8.dp)
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

content = content.replace(
"""            // Hero Promo Banner or Dynamic Slider
            item {
                if (appBanners.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { appBanners.size })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth().height(200.dp).padding(vertical = 8.dp),
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
                } else {
                    HeroPromoBanner(
                        appName = appName,
                        onBookNowClick = onBookNowClick
                    )
                }
            }""",
slider_logic_with_dots.strip()
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
