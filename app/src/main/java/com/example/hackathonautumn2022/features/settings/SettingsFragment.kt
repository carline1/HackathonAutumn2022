package com.example.hackathonautumn2022.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hackathonautumn2022.MainActivity
import com.example.hackathonautumn2022.R
import com.example.hackathonautumn2022.databinding.SettingsFragmentBinding
import com.example.hackathonautumn2022.features.reg.RegViewModel
import com.example.hackathonautumn2022.utils.collectOnStart
import kotlinx.coroutines.flow.onEach

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)

        initToolbar()
//        initBackgrounds()
        initLogout()

        return binding.root
    }

    private fun initToolbar() {
        binding.toolbar.title = getString(R.string.settings)
    }

//    private fun initBackgrounds() {
//        binding.apply {
//            background1.setOnClickListener {
//                (requireActivity() as MainActivity).changeTheme(R.style.AppTheme1)
//            }
//            background2.setOnClickListener {
//                (requireActivity() as MainActivity).changeTheme(R.style.AppTheme2)
//            }
//            background3.setOnClickListener {
//                (requireActivity() as MainActivity).changeTheme(R.style.AppTheme3)
//            }
//        }
//    }

    private fun initLogout() {
        binding.logout.setOnClickListener {
            requireActivity().finish()
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
//            when(it) {
//
//            }
        }.collectOnStart(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}