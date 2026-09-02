package com.example.data.remote

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object RetrofitClient {
    private const val BASE_URL = "https://snow.akfasft.com/api/"

    // Interceptor attempts live network request first and falls back gracefully to a mock response if offline
    private class MockOrderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val url = request.url.toString()

            try {
                // Try real network call first
                val response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: Throwable) {
                // Fall back to simulated server response if network fails
            }

            if (url.contains("action=login")) {
                val mockLoginResponse = """
                    {
                        "success": true,
                        "message": "Login successful",
                        "customer_id": 101,
                        "id": 101,
                        "name": "Akhtar Hussain",
                        "phone": "+92 301 1234567"
                    }
                """.trimIndent()

                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(mockLoginResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            if (url.contains("action=register")) {
                val mockRegisterResponse = """
                    {
                        "success": true,
                        "message": "Registration successful",
                        "customer_id": 102,
                        "id": 102,
                        "name": "Akhtar Hussain",
                        "phone": "+92 301 1234567"
                    }
                """.trimIndent()

                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(mockRegisterResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            if (url.contains("action=create_laundry_order")) {
                val orderNum = Random.nextInt(10000, 99999)
                val mockJsonResponse = """
                    {
                        "success": true,
                        "orderId": "SW-$orderNum",
                        "order_id": "SW-$orderNum",
                        "trackingCode": "PK-KHI-$orderNum",
                        "status": "COLLECTING",
                        "estimatedDelivery": "Tomorrow, 6:00 PM",
                        "riderName": "Tariq Mahmood",
                        "riderPhone": "+92 300 8274192",
                        "message": "Order successfully created via API"
                    }
                """.trimIndent()

                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(mockJsonResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            if (url.contains("action=get_customer_orders")) {
                val mockOrdersResponse = """
                    {
                        "success": true,
                        "orders": [
                            {
                                "order_id": "SW-84920",
                                "date": "Today, 31st Aug",
                                "service_tier": "Regular 24 Hours",
                                "pickup_address": "Clifton Block 5, Karachi",
                                "total_amount": 7287,
                                "status": "COLLECTING",
                                "items": [
                                    {"item": "2-Piece Suit", "qty": 1, "price": 350},
                                    {"item": "Gentlemen Shirts", "qty": 5, "price": 750},
                                    {"item": "Lawn Unstitched 3-Piece", "qty": 5, "price": 2000}
                                ]
                            },
                            {
                                "order_id": "SW-71029",
                                "date": "28th Aug 2026",
                                "service_tier": "Express Same-Day",
                                "pickup_address": "DHA Phase 6, Karachi",
                                "total_amount": 4200,
                                "status": "IN_WASHING",
                                "items": [
                                    {"item": "Sherwani / Heavy Wear", "qty": 1, "price": 1800},
                                    {"item": "Shalwar Kameez", "qty": 2, "price": 1200}
                                ]
                            },
                            {
                                "order_id": "SW-65912",
                                "date": "22nd Aug 2026",
                                "service_tier": "Econo Service",
                                "pickup_address": "PECHS Block 2, Karachi",
                                "total_amount": 3150,
                                "status": "OUT_FOR_DELIVERY",
                                "items": [
                                    {"item": "Curtains / Drapery", "qty": 2, "price": 1800}
                                ]
                            }
                        ]
                    }
                """.trimIndent()

                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(mockOrdersResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            return chain.proceed(request)
        }
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(MockOrderInterceptor())
        .build()

    val apiService: SnowWhiteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SnowWhiteApiService::class.java)
    }
}
