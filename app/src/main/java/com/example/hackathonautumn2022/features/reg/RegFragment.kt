package com.example.hackathonautumn2022.features.reg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackathonautumn2022.MainActivity
import com.example.hackathonautumn2022.R
import com.example.hackathonautumn2022.databinding.AuthFragmentBinding
import com.example.hackathonautumn2022.databinding.RegFragmentBinding
import com.example.hackathonautumn2022.utils.collectOnStart
import kotlinx.coroutines.flow.onEach

class RegFragment : Fragment() {

    private var _binding: RegFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegFragmentBinding.inflate(inflater, container, false)

        initToolbar()

        return binding.root
    }

    private fun initToolbar() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back_24px)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        observeActions()
    }

    private fun observeState() {
        viewModel.state.onEach {

        }.collectOnStart(viewLifecycleOwner)
    }

    private fun observeActions() {
        viewModel.action.onEach {
            when(it) {
                RegViewModel.Actions.OnSettingsClicked -> {}
            }
        }.collectOnStart(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}