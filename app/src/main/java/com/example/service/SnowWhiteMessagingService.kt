package com.example.service

import android.util.Log
import com.example.util.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SnowWhiteMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check payload notification data
        var title = remoteMessage.notification?.title
        var body = remoteMessage.notification?.body

        // Check custom data payload if provided
        if (remoteMessage.data.isNotEmpty()) {
            val dataTitle = remoteMessage.data["title"]
            val dataBody = remoteMessage.data["body"]
            val orderId = remoteMessage.data["order_id"]
            val status = remoteMessage.data["status"]

            if (!dataTitle.isNullOrBlank()) title = dataTitle
            if (!dataBody.isNullOrBlank()) body = dataBody

            if (title.isNullOrBlank() && !status.isNullOrBlank()) {
                title = "Order #$orderId Update"
                body = "Your order status is now: $status"
            }

            NotificationHelper.showOrderNotification(
                context = applicationContext,
                title = title ?: "SnowWhite Order Update",
                message = body ?: "Your laundry order status has been updated.",
                orderId = orderId
            )
            return
        }

        if (!title.isNullOrBlank() || !body.isNullOrBlank()) {
            NotificationHelper.showOrderNotification(
                context = applicationContext,
                title = title ?: "SnowWhite Order Update",
                message = body ?: "Your order status has been updated."
            )
        }
    }

    @Suppress("DEPRECATION")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed FCM registration token: $token")
        // Save FCM token if backend registration is required
    }

    companion object {
        private const val TAG = "SnowWhiteFCM"
    }
}
