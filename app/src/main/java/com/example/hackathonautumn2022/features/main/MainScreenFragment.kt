package com.example.hackathonautumn2022.features.main

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.hackathonautumn2022.MainActivity
import com.example.hackathonautumn2022.R
import com.example.hackathonautumn2022.data.models.TaskResponse
import com.example.hackathonautumn2022.databinding.MainScreenFragmentBinding
import com.example.hackathonautumn2022.di.AppComponent
import com.example.hackathonautumn2022.di.appComponent
import com.example.hackathonautumn2022.features.reg.RegViewModel
import com.example.hackathonautumn2022.features.task.TaskFragment
import com.example.hackathonautumn2022.utils.collectOnStart
import com.example.hackathonautumn2022.utils.dp
import com.example.hackathonautumn2022.utils.navigateTo
import kotlinx.coroutines.flow.onEach

class MainScreenFragment : Fragment() {

    private var _binding: MainScreenFragmentBinding? = null
    private val binding get() = _binding!!

    private val component: AppComponent by lazy {
        requireContext().appComponent
    }
    private val viewModel: MainScreenViewModel by lazy {
        component.mainScreenViewModel
    }

    private val taskListAdapter = MainScreenAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainScreenFragmentBinding.inflate(inflater, container, false)

        initToolbar()
        initAdapter()
//        test()
//        viewModel.test()

        return binding.root
    }

    private fun initToolbar() {
        val title = arguments?.getString(USER_NAME) ?: getString(R.string.tasks)
        binding.toolbar.apply {
            this.title = title
        }
    }

    private fun initAdapter() {
        binding.taskList.apply {
            this.adapter = taskListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    rect: Rect,
                    view: View,
                    parent: RecyclerView,
                    s: RecyclerView.State
                ) {
                    parent.adapter?.let {
                        val childAdapterPosition = parent.getChildAdapterPosition(view)
                            .let { if (it == RecyclerView.NO_POSITION) return else it }

                        rect.right = 10.dp
                        rect.left = 10.dp
                        rect.top = if (childAdapterPosition == 0) 16.dp else 0
                        rect.bottom = if (childAdapterPosition != 0) 16.dp else 0
                    }
                }
            })
        }
    }

//    private fun test() {
//        taskListAdapter.submitList(
//            listOf(
//                MainScreenAdapter.TaskViewHolderModel.Title("Test"),
//                MainScreenAdapter.TaskViewHolderModel.Task(
//                    TaskResponse(
//                        null,
//                        null,
//                        null,
//                        "Name",
//                        null,
//                        null,
//                        "01.01.2001"
//                    )
//                ) { navigateTo(R.id.action_mainScreenFragment_to_taskFragment) },
//                MainScreenAdapter.TaskViewHolderModel.Task(
//                    TaskResponse(
//                        null,
//                        null,
//                        null,
//                        "Name",
//                        null,
//                        null,
//                        "01.01.2001"
//                    )
//                ) {},
//                MainScreenAdapter.TaskViewHolderModel.Task(
//                    TaskResponse(
//                        null,
//                        null,
//                        null,
//                        "Name",
//                        null,
//                        null,
//                        "01.01.2001"
//                    )
//                ) {},
//                MainScreenAdapter.TaskViewHolderModel.Task(
//                    TaskResponse(
//                        null,
//                        null,
//                        null,
//                        "Name",
//                        null,
//                        null,
//                        "01.01.2001"
//                    )
//                ) {},
//            )
//        )
//    }

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

    private fun updateProgress(isLoading: Boolean) {
        binding.progress.root.isVisible = isLoading
    }

    private fun updateScreenData(screenData: List<MainScreenAdapter.TaskViewHolderModel>) {
        taskListAdapter.submitList(screenData)
    }

    private fun observeActions() {
        viewModel.action.onEach {
            when(it) {
                is MainScreenViewModel.Actions.OnTaskClicked -> {
                    val bundle = TaskFragment.prepareBundle(it.userId, it.taskId, it.user, it.task)
                    navigateTo(R.id.action_mainScreenFragment_to_taskFragment, bundle)
                }
            }
        }.collectOnStart(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_NAME = "USER_NAME"

        fun prepareBundle(userName: String) = Bundle().apply {
            putString(USER_NAME, userName)
        }
    }

}