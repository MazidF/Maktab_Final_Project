package com.example.onlineshop.ui.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.data.model.customer.Customer
import com.example.onlineshop.data.model.order.OrderStatus
import com.example.onlineshop.databinding.FragmentProfileBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.utils.launchOnState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentProfile : FragmentConnectionObserver(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    private val viewModel: ViewModelProfile by viewModels()

    @Inject
    lateinit var mainDataStore: MainDataStore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        observe()
    }

    private fun initView() = with(binding) {
        val customer = viewModel.customer
        fragmentProfileUsername.text = customer.username
        fragmentProfileEmail.text = customer.email

        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        fragmentProfileCurrent.setOnClickListener {
            navigateToOrderHistory(OrderStatus.ON_HOLD)
        }
        fragmentProfileCompleted.setOnClickListener {
            navigateToOrderHistory(OrderStatus.COMPLETED)
        }
        fragmentProfileRefunded.setOnClickListener {
            navigateToOrderHistory(OrderStatus.REFUNDED)
        }
    }

    private fun navigateToOrderHistory(orderStatus: OrderStatus) {
        navController.navigate(
            FragmentProfileDirections.actionFragmentProfileToFragmentOrderHistory(
                orderStatus
            )
        )
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            mainDataStore.preferences.collect {
                if (it.hasBeenLoggedIn) {
                    initView()
                } else {
                    navigateToLoginPage()
                }
            }
        }
    }

    private fun navigateToLoginPage() {
        navController.navigate(
            FragmentProfileDirections.actionFragmentProfileToFragmentLogin()
        )
    }

    private fun startLoading() = with(binding) {

    }

    private fun onSuccess(customer: Customer) {

        stopLoading()
    }

    private fun stopLoading() = with(binding) {

    }

    private fun handleError(error: Throwable) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentProfileDirections.actionFragmentProfileToFragmentNetworkConnectionFailed())
    }
}