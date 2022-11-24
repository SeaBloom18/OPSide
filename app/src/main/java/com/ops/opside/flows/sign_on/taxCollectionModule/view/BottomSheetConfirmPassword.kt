package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetConfirmPasswordBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.ConfirmPasswordAction
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetConfirmPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetConfirmPassword(
    private val response: (Boolean) -> Unit = {}
) : BaseBottomSheetFragment() {

    private val mBinding: BottomSheetConfirmPasswordBinding by lazy {
        BottomSheetConfirmPasswordBinding.inflate(layoutInflater)
    }

    private val mViewModel: BottomSheetConfirmPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {
            btnConfirm.setOnClickListener { mViewModel.confirmPassword(tePassword.text.toString()) }
            btnClose.setOnClickListener { dismiss() }
        }

        bindViewModel()
    }

    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))
        mViewModel.getAction().observe(this, Observer(this::handleAction))
    }

    private fun handleAction(action: ConfirmPasswordAction) {
        when (action) {
            is ConfirmPasswordAction.ShowMessageError -> {
                showError(action.error)
                response.invoke(false)
            }
            is ConfirmPasswordAction.PasswordSuccess -> response.invoke(true)
        }
    }

}