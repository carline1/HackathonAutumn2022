package com.example.hackathonautumn2022.data.api

import com.example.hackathonautumn2022.data.models.*
import retrofit2.http.*

interface AppService {

    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): UserResponse

    @GET("users/{userId}/tasks")
    suspend fun getTasksOfUser(@Path("userId") userId: Int): List<TaskResponse>

    @GET("users/{userId}/tasks/{taskId}")
    suspend fun getTaskOfUser(
        @Path("userId") userId: Int,
        @Path("taskId") taskId: Int
    ): TaskResponse

    @PUT("users/{userId}/tasks/{taskId}")
    suspend fun updateTaskStatus(
        @Path("userId") userId: Int,
        @Path("taskId") taskId: Int,
        @Body taskBody: UpdateTaskStatusRequest
    )

    @GET("users/{userId}/tasks/{taskId}/comments")
    suspend fun getCommentsOfTask(
        @Path("userId") userId: Int,
        @Path("taskId") taskId: Int
    ): List<MessasgesResponse>

    @POST("users/{userId}/tasks/{taskId}/comments")
    suspend fun sendMessageToComments(
        @Path("userId") userId: Int,
        @Path("taskId") taskId: Int,
        @Body message: MessageRequest
    ): List<MessasgesResponse>

    @GET("users/{userId}/tasks/{taskId}/marks")
    suspend fun getMarksOfTask(
        @Path("userId") userId: Int,
        @Path("taskId") taskId: Int
    ): List<MessasgesResponse>

    @POST("users/{userId}/tasks/{taskId}/comments")
    suspend fun sendMessageToMarks(
        @Path("userId") userId: Int,
        @Path("taskId") taskId: Int,
        @Body message: MessageRequest
    ): List<MessasgesResponse>

    @GET("users/{userId}/tasks/{taskId}/comments/{commentId}")
    suspend fun getCommentById(
        @Path("userId") userId: Int,
        @Path("taskId") taskId: Int,
        @Path("commentId") commentId: Int,
    ): MessasgesResponse

}