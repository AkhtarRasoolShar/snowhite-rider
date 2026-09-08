with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

imports_to_add = """
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.example.data.model.Banner
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
"""
if "HorizontalPager" not in content:
    content = content.replace("import androidx.compose.foundation.lazy.items", "import androidx.compose.foundation.lazy.items" + imports_to_add)

if "appBanners: List<Banner> = emptyList()," not in content:
    content = content.replace("    appName: String = \"SnowWhite\",\n", "    appName: String = \"SnowWhite\",\n    appBanners: List<Banner> = emptyList(),\n")

slider_logic = """
            // Hero Promo Banner or Dynamic Slider
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
            }
"""

content = content.replace(
"""            // Hero Promo Banner
            item {
                HeroPromoBanner(
                    appName = appName,
                    onBookNowClick = onBookNowClick
                )
            }""",
slider_logic.strip()
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
