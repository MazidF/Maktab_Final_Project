package com.example.onlineshop.data.model.customer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Customer(
    val id: Long,
    val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val username: String,
    val password: String,
) : Serializable
