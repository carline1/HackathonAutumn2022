package com.example.hackathonautumn2022.data.models

data class UserResponse(
    val id: Int?,
    val user: User?,
    val number_phone: String?,
)

data class User(
    val username: String?,
    val first_name: String?,
    val last_name: String?,
    val email: String?
)
