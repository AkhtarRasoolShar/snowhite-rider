package com.example.data.remote

import com.example.data.model.ApiResponse
import com.example.data.model.AuthResponse
import com.example.data.model.Category
import com.example.data.model.ChatMessage
import com.example.data.model.ChatMessagesResponse
import com.example.data.model.CreateOrderRequest
import com.example.data.model.CreateOrderResponse
import com.example.data.model.EmailInvoiceRequest
import com.example.data.model.EmailInvoiceResponse
import com.example.data.model.GetOrdersResponse
import com.example.data.model.LoginRequest
import com.example.data.model.Product
import com.example.data.model.RegisterRequest
import com.example.data.model.SendChatMessageRequest
import com.example.data.model.ServiceItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SnowWhiteApiService {
    @POST("routes.php")
    suspend fun login(
        @Query("action") action: String = "login",
        @Body request: Map<String, String>
    ): Response<AuthResponse>

    @POST("routes.php")
    suspend fun register(
        @Query("action") action: String = "register",
        @Body request: Map<String, String>
    ): Response<AuthResponse>

    @POST("routes.php")
    suspend fun createLaundryOrder(
        @Query("action") action: String = "create_laundry_order",
        @Body request: CreateOrderRequest
    ): Response<CreateOrderResponse>

    @GET("routes.php")
    suspend fun getCustomerOrders(
        @Query("action") action: String = "get_customer_orders",
        @Query("customer_id") customerId: Int = 1,
        @Query("user_id") userId: Int = customerId
    ): Response<GetOrdersResponse>

    @POST("routes.php")
    suspend fun emailInvoice(
        @Query("action") action: String = "email_invoice",
        @Body request: EmailInvoiceRequest
    ): Response<EmailInvoiceResponse>

    @GET("routes.php")
    suspend fun getServices(
        @Query("action") action: String = "get_services"
    ): Response<ApiResponse<List<ServiceItem>>>

    @GET("routes.php?action=get_categories")
    suspend fun getCategories(): Response<ApiResponse<List<Category>>>

    @GET("routes.php?action=get_products&is_app=true")
    suspend fun getProducts(): Response<ApiResponse<List<Product>>>

    @GET("routes.php?action=get_products&is_app=true")
    suspend fun getActiveProducts(): Response<ApiResponse<List<Product>>>

    @GET("routes.php")
    suspend fun getChatMessages(
        @Query("action") action: String = "get_chat_messages",
        @Query("order_id") orderId: Int
    ): Response<ChatMessagesResponse>

    @POST("routes.php")
    suspend fun sendChatMessage(
        @Query("action") action: String = "send_chat_message",
        @Body request: SendChatMessageRequest
    ): Response<ApiResponse<ChatMessage>>
}
