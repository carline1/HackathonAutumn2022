package com.example.hackathonautumn2022.data.models

data class TaskResponse(
    val id: Int?,
    val name: String?,
    val description: String?,
    val status: String?,
    val security: Boolean?,
    val start: String?,
    val end: String?,
    val user: String?
)