package com.example.hackathonautumn2022.features.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackathonautumn2022.MainActivity
import com.example.hackathonautumn2022.R
import com.example.hackathonautumn2022.databinding.AuthFragmentBinding
import com.example.hackathonautumn2022.utils.collectOnStart
import com.example.hackathonautumn2022.utils.navigateTo
import kotlinx.coroutines.flow.onEach

class AuthFragment : Fragment() {

    private var _binding: AuthFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentBinding.inflate(inflater, container, false)

        initRegButton()
        initAuthButton()

        return binding.root
    }

    private fun initRegButton() {
        binding.regButton.setOnClickListener {
            navigateTo(R.id.action_authFragment_to_regFragment)
        }
    }

    private fun initAuthButton() {
        binding.authButton.setOnClickListener {
            viewModel.onAuthButtonClicked("", "")
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
            when (it) {
                is AuthViewModel.Actions.OnAuthButtonClicked -> onAuthButtonClicked(
                    it.login,
                    it.pwd
                )
            }
        }.collectOnStart(viewLifecycleOwner)
    }

    private fun onAuthButtonClicked(login: String, pwd: String) {
        navigateTo(R.id.action_authFragment_to_mainScreenFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}