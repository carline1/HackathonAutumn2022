package com.example.hackathonautumn2022.features.comments.usecases

import com.example.hackathonautumn2022.consts.Constants.userName
import com.example.hackathonautumn2022.data.models.MessasgesResponse
import com.example.hackathonautumn2022.features.common.ChatAdapter.ChatViewHolderModel

class PrepareMessagesScreenData(
    private val comments: List<MessasgesResponse>
) {

    fun prepare(): List<ChatViewHolderModel> = comments.map {
        if (it.user == userName) {
            ChatViewHolderModel.YourselfMsg(it.description ?: "", "")
        } else {
            ChatViewHolderModel.StrangerMsg(it.user ?: "", it.description ?: "", "")
        }
    }

}