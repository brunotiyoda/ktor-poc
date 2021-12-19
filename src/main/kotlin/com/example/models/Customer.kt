package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String
)

val customerStorage = mutableListOf<Customer>()