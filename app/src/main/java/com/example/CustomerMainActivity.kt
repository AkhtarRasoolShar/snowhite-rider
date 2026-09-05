package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainContainer
import com.example.ui.theme.SnowWhiteTheme
import com.example.ui.viewmodel.SnowWhiteViewModel
import kotlinx.coroutines.delay

/**
 * Opens WhatsApp chat directly with SnoWhite Customer Support (+92 301 8637011).
 */
fun openSupportWhatsApp(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/923018637011"))
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Unable to open WhatsApp. Please contact +92 301 8637011", Toast.LENGTH_LONG).show()
    }
}

class CustomerMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SnowWhiteTheme {
                val viewModel: SnowWhiteViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()

                // REAL-TIME STATUS SYNC: Polite background poll if needed
                LaunchedEffect(Unit) {
                    val initialId = uiState.currentCustomerId
                    if (initialId > 0) {
                        viewModel.fetchOrders(initialId, isSilent = true)
                    }
                    while (true) {
                        delay(30000L) // Poll every 30 seconds
                        val customerId = uiState.currentCustomerId
                        if (customerId > 0) {
                            viewModel.fetchOrders(customerId, isSilent = true)
                        }
                    }
                }

                MainContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CustomerMainScreen(viewModel: SnowWhiteViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // REAL-TIME STATUS SYNC: Polite background poll if needed
    LaunchedEffect(Unit) {
        val initialId = uiState.currentCustomerId
        if (initialId > 0) {
            viewModel.fetchOrders(initialId, isSilent = true)
        }
        while (true) {
            delay(30000L) // Poll every 30 seconds
            val customerId = uiState.currentCustomerId
            if (customerId > 0) {
                viewModel.fetchOrders(customerId, isSilent = true)
            }
        }
    }

    MainContainer(viewModel = viewModel)
}
