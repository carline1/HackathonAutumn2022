package com.example.hackathonautumn2022.features.comments

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathonautumn2022.R
import com.example.hackathonautumn2022.databinding.CommentsFragmentBinding
import com.example.hackathonautumn2022.di.AppComponent
import com.example.hackathonautumn2022.di.appComponent
import com.example.hackathonautumn2022.features.common.ChatAdapter
import com.example.hackathonautumn2022.features.common.ChatAdapter.*
import com.example.hackathonautumn2022.features.main.MainScreenViewModel
import com.example.hackathonautumn2022.features.task.TaskFragment
import com.example.hackathonautumn2022.features.task.TaskViewModel
import com.example.hackathonautumn2022.utils.collectOnStart
import com.example.hackathonautumn2022.utils.dp
import com.example.hackathonautumn2022.utils.lazyViewModel
import com.example.hackathonautumn2022.utils.navigateTo
import kotlinx.coroutines.flow.onEach

class CommentsFragment : Fragment() {

    private var _binding: CommentsFragmentBinding? = null
    private val binding get() = _binding!!

    private val component: AppComponent by lazy {
        requireContext().appComponent
    }
    private val viewModel: CommentsViewModel by lazyViewModel {
        val userId = arguments?.getInt(TaskFragment.USER_ID)
        checkNotNull(userId) { "userId must be not null!" }
        val taskId = arguments?.getInt(TaskFragment.TASK_ID)
        checkNotNull(taskId) { "taskId must be not null!" }
        val user = arguments?.getString(TaskFragment.USER)
        checkNotNull(user) { "user must be not null!" }
        val task = arguments?.getString(TaskFragment.TASK)
        checkNotNull(task) { "task must be not null!" }

        component.commentsViewModel.create(userId, taskId, user, task)
    }

    private val chatAdapter = ChatAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommentsFragmentBinding.inflate(inflater, container, false)

        initToolbar()
        initAdapter()
        initSendButton()
//        test()

        return binding.root
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_24px)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initAdapter() {
        binding.commentsList.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    rect: Rect,
                    view: View,
                    parent: RecyclerView,
                    s: RecyclerView.State
                ) {
                    parent.adapter?.let { adapter ->
                        val childAdapterPosition = parent.getChildAdapterPosition(view)
                            .let { if (it == RecyclerView.NO_POSITION) return else it }
//                        val viewType = adapter.getItemViewType(childAdapterPosition)

                        rect.right = 20.dp
                        rect.left = 20.dp
                        rect.top = if (childAdapterPosition == 0) 16.dp else 0
                        rect.bottom = 16.dp
                    }
                }
            })
        }
    }

    private fun initSendButton() {
        binding.apply {
            sendCommentButton.setOnClickListener {
                viewModel.sendMessage(messageTextInput.text.toString())
                messageTextInput.setText("")
            }
        }
    }

    private fun test() {
        chatAdapter.submitList(
            listOf(
                ChatViewHolderModel.Date("20 мая"),
                ChatViewHolderModel.YourselfMsg("АААААА", "16:00"),
                ChatViewHolderModel.StrangerMsg("Димас", "АААААА", "16:00"),
                ChatViewHolderModel.YourselfMsg("АААААА", "16:00"),
                ChatViewHolderModel.YourselfMsg("АААААА", "16:00"),
                ChatViewHolderModel.StrangerMsg("Димас", "АААААА", "16:00"),
                ChatViewHolderModel.YourselfMsg("АААААА", "16:00"),
                ChatViewHolderModel.YourselfMsg("АААААА", "16:00"),
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        observeActions()
    }

    private fun observeState() {
        viewModel.state.onEach {
            updateScreenData(it.screenData)
            updateProgress(it.isLoading)
        }.collectOnStart(viewLifecycleOwner)
    }

    private fun updateScreenData(screenData: List<ChatViewHolderModel>) {
        chatAdapter.submitList(screenData)
    }

    private fun updateProgress(isLoading: Boolean) {
        binding.progress.root.isVisible = isLoading
    }

    private fun observeActions() {
        viewModel.action.onEach {
            when(it) {
                CommentsViewModel.Actions.ScrollListToBottom -> scrollListToBottom()
            }
        }.collectOnStart(viewLifecycleOwner)
    }

    private fun scrollListToBottom() {
        binding.commentsList.scrollToPosition(chatAdapter.currentList.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_ID = "USER_ID"
        const val TASK_ID = "TASK_ID"
        const val USER = "USER"
        const val TASK = "TASK"

        fun prepareBundle(userId: Int, taskId: Int, user: String, task: String) = Bundle().apply {
            putInt(USER_ID, userId)
            putInt(TASK_ID, taskId)
            putString(USER, user)
            putString(TASK, task)
        }
    }

}