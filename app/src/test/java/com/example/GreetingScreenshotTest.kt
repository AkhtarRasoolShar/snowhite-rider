package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.SnowWhiteTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      SnowWhiteTheme {
        HomeScreen(
          activeOrder = null,
          categories = emptyList(),
          products = emptyList(),
          selectedCategoryId = null,
          onCategorySelected = {},
          getProductQuantity = { 0 },
          onAddProduct = {},
          onRemoveProduct = {},
          totalCartPricePKR = 0,
          totalCartCount = 0,
          onBookNowClick = {},
          onLaundryClick = {},
          onProductsClick = {},
          onReviewsClick = {},
          onTrackActiveOrderClick = {},
          onProceedToSchedule = {}
        )
      }
    }
  }
}
