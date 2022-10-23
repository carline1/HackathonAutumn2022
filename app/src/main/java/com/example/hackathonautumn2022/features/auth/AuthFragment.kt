package com.example.hackathonautumn2022.features.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.hackathonautumn2022.R
import com.example.hackathonautumn2022.consts.Constants
import com.example.hackathonautumn2022.databinding.AuthFragmentBinding
import com.example.hackathonautumn2022.di.AppComponent
import com.example.hackathonautumn2022.di.appComponent
import com.example.hackathonautumn2022.features.main.MainScreenFragment
import com.example.hackathonautumn2022.utils.collectOnStart
import com.example.hackathonautumn2022.utils.navigateTo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.onEach

class AuthFragment : Fragment() {

    private var _binding: AuthFragmentBinding? = null
    private val binding get() = _binding!!

    private val component: AppComponent by lazy {
        requireContext().appComponent
    }
    private val viewModel: AuthViewModel by lazy {
        component.authViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentBinding.inflate(inflater, container, false)

//        initRegButton()
        initAuthButton()

        return binding.root
    }

//    private fun initRegButton() {
//        binding.regButton.setOnClickListener {
//            navigateTo(R.id.action_authFragment_to_regFragment)
//        }
//    }

    private fun initAuthButton() {
        binding.authButton.setOnClickListener {
            viewModel.onAuthButtonClicked(
                login = binding.loginEditText.text.toString(),
                pwd = binding.pwdEditText.text.toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()
        observeActions()
    }

    private fun observeState() {
        viewModel.state.onEach {
            updateProgress(it.isLoading)
        }.collectOnStart(viewLifecycleOwner)
    }

    private fun updateProgress(isLoading: Boolean) {
        binding.progress.root.isVisible = isLoading
    }

    private fun observeActions() {
        viewModel.action.onEach {
            when (it) {
                is AuthViewModel.Actions.ToNextScreen -> onAuthButtonClicked(it.login)
                AuthViewModel.Actions.OnLoginError -> MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.error)
                    .setMessage(R.string.login_or_pwd_fail)
                    .setPositiveButton(R.string.Ok) { dialog, which -> }
                    .show()
            }
        }.collectOnStart(viewLifecycleOwner)
    }

    private fun onAuthButtonClicked(login: String) {
        Constants.userName = login
        navigateTo(R.id.action_authFragment_to_mainScreenFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}