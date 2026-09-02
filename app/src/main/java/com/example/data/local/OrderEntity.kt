package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laundry_orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val trackingCode: String,
    val serviceTier: String,
    val area: String,
    val address: String,
    val date: String,
    val timeSlot: String,
    val totalAmountPKR: Int,
    val itemCount: Int,
    val itemsSummaryJson: String,
    val statusStepIndex: Int,
    val riderName: String,
    val riderPhone: String,
    val estimatedDelivery: String,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)
