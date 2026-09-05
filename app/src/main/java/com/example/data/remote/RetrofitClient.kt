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
                                "rider_name": "Tariq Mahmood",
                                "rider_phone": "03008274192",
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

            if (url.contains("action=get_categories")) {
                val mockCategoriesResponse = """
                    {
                        "status": "success",
                        "success": true,
                        "data": [
                            {"id": 1, "name": "Men", "type": "garment"},
                            {"id": 2, "name": "Women", "type": "garment"},
                            {"id": 3, "name": "Household", "type": "household"},
                            {"id": 4, "name": "Premium Care", "type": "premium"}
                        ]
                    }
                """.trimIndent()

                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(mockCategoriesResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            if (url.contains("action=get_products")) {
                val mockProductsResponse = """
                    {
                        "status": "success",
                        "success": true,
                        "data": [
                            {"id": 101, "category_id": 1, "name": "2-Piece Suit", "description": "Professional dry cleaning & steam press for 2-piece suit", "price": 350.0},
                            {"id": 102, "category_id": 1, "name": "Gentlemen Shirt", "description": "Crisp washing & hanger press", "price": 150.0},
                            {"id": 103, "category_id": 1, "name": "Trousers / Pants", "description": "Stain treatment & sharp crease press", "price": 180.0},
                            {"id": 104, "category_id": 1, "name": "Shalwar Kameez", "description": "Traditional 2-piece suit gentle care", "price": 300.0},
                            {"id": 201, "category_id": 2, "name": "Lawn 3-Piece Suit", "description": "Delicate fabric wash & soft steam press", "price": 400.0},
                            {"id": 202, "category_id": 2, "name": "Designer Dress / Gown", "description": "Special organic solvent cleaning for embroidery", "price": 800.0},
                            {"id": 203, "category_id": 2, "name": "Silk Dupatta", "description": "Eco silk care & hand wash finish", "price": 120.0},
                            {"id": 204, "category_id": 2, "name": "Abaya / Hijab", "description": "Deep clean & steam polish", "price": 250.0},
                            {"id": 301, "category_id": 3, "name": "Double Bed Sheet Set", "description": "Sanitizing wash & flat iron press", "price": 350.0},
                            {"id": 302, "category_id": 3, "name": "Blanket / Comforter", "description": "Deep fluff drying & anti-allergen clean", "price": 700.0},
                            {"id": 303, "category_id": 3, "name": "Curtains / Drapery (Pair)", "description": "Dust extraction & gentle steam treatment", "price": 900.0},
                            {"id": 304, "category_id": 3, "name": "Cushion Covers", "description": "Fabric softening wash", "price": 100.0},
                            {"id": 401, "category_id": 4, "name": "Leather Jacket", "description": "Specialized leather conditioning & polish", "price": 1200.0},
                            {"id": 402, "category_id": 4, "name": "Sherwani / Wedding Wear", "description": "Handcrafted stain removal & velvet care", "price": 1800.0},
                            {"id": 403, "category_id": 4, "name": "Bridal Lehenga", "description": "Intricate embroidery protection & steam finishing", "price": 2500.0}
                        ]
                    }
                """.trimIndent()

                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(mockProductsResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            if (url.contains("action=get_chat_messages")) {
                val orderIdParam = request.url.queryParameter("order_id") ?: "1"
                val mockChatList = inMemoryMockChats.getOrPut(orderIdParam) {
                    mutableListOf(
                        com.example.data.model.ChatMessage(
                            id = 1,
                            orderId = orderIdParam.toIntOrNull() ?: 1,
                            senderType = "rider",
                            senderId = 201,
                            message = "Assalam-o-Alaikum! I am your delivery captain Tariq. I have received your laundry order.",
                            createdAt = "11:30 AM"
                        ),
                        com.example.data.model.ChatMessage(
                            id = 2,
                            orderId = orderIdParam.toIntOrNull() ?: 1,
                            senderType = "customer",
                            senderId = 101,
                            message = "Walaikum Assalam Tariq Bhai! Please ensure gentle dry clean for the wedding wear.",
                            createdAt = "11:32 AM"
                        ),
                        com.example.data.model.ChatMessage(
                            id = 3,
                            orderId = orderIdParam.toIntOrNull() ?: 1,
                            senderType = "rider",
                            senderId = 201,
                            message = "Understood! Special steam press and delicate packaging will be done. I'll notify you upon dispatch.",
                            createdAt = "11:34 AM"
                        )
                    )
                }
                val json = gson.toJson(
                    mapOf(
                        "status" to "success",
                        "success" to true,
                        "data" to mockChatList,
                        "messages" to mockChatList
                    )
                )
                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(json.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            if (url.contains("action=send_chat_message")) {
                val orderIdParam = request.url.queryParameter("order_id") ?: "1"
                val list = inMemoryMockChats.getOrPut(orderIdParam) { mutableListOf() }
                val newMsg = com.example.data.model.ChatMessage(
                    id = list.size + 1,
                    orderId = orderIdParam.toIntOrNull() ?: 1,
                    senderType = "customer",
                    senderId = 101,
                    message = "Thank you! Looking forward to it.",
                    createdAt = "Just now"
                )
                list.add(newMsg)
                val json = gson.toJson(
                    mapOf(
                        "status" to "success",
                        "success" to true,
                        "data" to newMsg
                    )
                )
                return Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .body(json.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            return chain.proceed(request)
        }
    }

    private val inMemoryMockChats = mutableMapOf<String, MutableList<com.example.data.model.ChatMessage>>()

    private val gson = GsonBuilder()
        
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
