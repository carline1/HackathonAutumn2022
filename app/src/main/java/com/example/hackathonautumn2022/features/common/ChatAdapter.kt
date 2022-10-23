package com.example.hackathonautumn2022.features.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathonautumn2022.databinding.DateViewHolderBinding
import com.example.hackathonautumn2022.databinding.StrangerMsgViewHolderBinding
import com.example.hackathonautumn2022.databinding.YourselfMsgViewHolderBinding

class ChatAdapter :
    ListAdapter<ChatAdapter.ChatViewHolderModel, ChatAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (ViewHolderType.values()[viewType]) {
            ViewHolderType.DATE -> ViewHolder.Date(
                DateViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ViewHolderType.YOURSELF_MSG -> ViewHolder.YourselfMsg(
                YourselfMsgViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ViewHolderType.STRANGER_MSG -> ViewHolder.StrangerMsg(
                StrangerMsgViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(currentList[position])

    override fun getItemCount() = currentList.size

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is ChatViewHolderModel.Date -> ViewHolderType.DATE.ordinal
            is ChatViewHolderModel.YourselfMsg -> ViewHolderType.YOURSELF_MSG.ordinal
            is ChatViewHolderModel.StrangerMsg -> ViewHolderType.STRANGER_MSG.ordinal
        }
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(model: ChatViewHolderModel)

        class Date(private val binding: DateViewHolderBinding) : ViewHolder(binding.root) {
            override fun bind(model: ChatViewHolderModel) {
                model as ChatViewHolderModel.Date

                binding.root.text = model.date
            }
        }

        class YourselfMsg(private val binding: YourselfMsgViewHolderBinding) :
            ViewHolder(binding.root) {
            override fun bind(model: ChatViewHolderModel) {
                model as ChatViewHolderModel.YourselfMsg

                binding.msgText.text = model.msg
                binding.time.text = model.time
            }
        }

        class StrangerMsg(private val binding: StrangerMsgViewHolderBinding) :
            ViewHolder(binding.root) {
            override fun bind(model: ChatViewHolderModel) {
                model as ChatViewHolderModel.StrangerMsg

                binding.userName.text = model.userName
                binding.msgText.text = model.msg
                binding.time.text = model.time
            }
        }
    }

    sealed class ChatViewHolderModel {
        data class Date(val date: String) : ChatViewHolderModel()
        data class YourselfMsg(val msg: String, val time: String) : ChatViewHolderModel()
        data class StrangerMsg(val userName: String, val msg: String, val time: String) :
            ChatViewHolderModel()
    }

    enum class ViewHolderType {
        DATE, YOURSELF_MSG, STRANGER_MSG
    }

    object DiffCallback : DiffUtil.ItemCallback<ChatViewHolderModel>() {
        override fun areItemsTheSame(
            oldItem: ChatViewHolderModel,
            newItem: ChatViewHolderModel
        ) =
            if (oldItem is ChatViewHolderModel.Date && newItem is ChatViewHolderModel.Date) {
                oldItem.date == newItem.date
            } else if (oldItem is ChatViewHolderModel.YourselfMsg && newItem is ChatViewHolderModel.YourselfMsg) {
                oldItem.msg == newItem.msg && oldItem.time == newItem.time
            } else if (oldItem is ChatViewHolderModel.StrangerMsg && newItem is ChatViewHolderModel.StrangerMsg) {
                oldItem.userName == newItem.userName && oldItem.msg == newItem.msg && oldItem.time == newItem.time
            } else false

        override fun areContentsTheSame(
            oldItem: ChatViewHolderModel,
            newItem: ChatViewHolderModel
        ) = oldItem == newItem
    }

}