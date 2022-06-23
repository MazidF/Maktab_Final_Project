package com.example.onlineshop.ui.fragments.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentLoginBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.data.result.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentLogin : FragmentConnectionObserver(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    private val viewModel: ViewModelLogin by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        fragmentLoginBtn.setOnClickListener {
            if (checkInputs()) {
                val username = fragmentLoginEmail.text
                val password = fragmentLoginPassword.text
                if (fragmentLoginRetryPassword.isGone) {
                    viewModel.login(username, password)
                } else {
                    viewModel.signIn(username, password)
                }
            }
        }
        fragmentLoginClose.setOnClickListener {
            (requireActivity() as? AppCompatActivity)?.onBackPressed()
        }
        fragmentLoginChange.apply {
            setOnClickListener {
                val isVisible = fragmentLoginRetryPassword.isVisible
                text = context.getString(
                    if (isVisible.not()) R.string.change_to_log_in else R.string.change_to_sign_in
                )
                fragmentLoginRetryPassword.isVisible = isVisible.not()
            }
        }
    }

    private fun checkInputs(): Boolean = with(binding) {
        var result = fragmentLoginPassword.check()
        result = fragmentLoginEmail.check {
            Patterns.EMAIL_ADDRESS.matcher(it).matches()
            /*Regex(EMAIL_PATTERN).matches(it)*/
        } && result
        if (fragmentLoginRetryPassword.isGone.not()) {
            result = fragmentLoginRetryPassword.check {
                it == fragmentLoginPassword.text
            } && result
        }
        result
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.customerStateFlow.collectLatest {
                when (it) {
                    is Resource.Fail -> {
                        fragmentLoginError.isVisible = true
                    }
                    is Resource.Success -> {
                        viewModel.login(it.body().id).join()
                        navController.navigate(
                            FragmentLoginDirections.actionFragmentLoginToFragmentProfile()
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(
            FragmentLoginDirections.actionFragmentLoginToFragmentNetworkConnectionFailed()
        )
    }
}