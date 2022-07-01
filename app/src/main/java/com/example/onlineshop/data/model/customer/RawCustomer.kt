package com.example.onlineshop.data.model.customer

import com.google.gson.annotations.SerializedName

data class RawCustomer(
    val email: String,
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    val username: String = "",
    val password: String,
    @SerializedName("meta_data")
    val metaData: ArrayList<CustomerMetaData>,
) {
    companion object {
        fun from(customer: Customer, metaDates: ArrayList<CustomerMetaData>): RawCustomer {
            return RawCustomer(
                email = customer.email,
                firstName = customer.firstName,
                lastName = customer.lastName,
                username = customer.username,
                password = customer.password,
                metaData = metaDates,
            )
        }
    }
}
