package com.ops.opside.flows.sign_on.taxCollectionModule.viewModel

import androidx.lifecycle.LiveData
import com.ops.opside.common.utils.MD5
import com.ops.opside.common.utils.SingleLiveEvent
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.ConfirmPasswordAction
import com.ops.opside.flows.sign_on.taxCollectionModule.model.ConfirmPasswordInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetConfirmPasswordViewModel @Inject constructor(
    val mInteractor: ConfirmPasswordInteractor
) : CommonViewModel() {

    private val _action: SingleLiveEvent<ConfirmPasswordAction> = SingleLiveEvent()
    fun getAction(): LiveData<ConfirmPasswordAction> = _action

    fun confirmPassword(password: String) {
        disposable.add(
            mInteractor.confirmPassword(MD5.hashString(password)).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        if (it.first) {
                            _action.value = ConfirmPasswordAction.PasswordSuccess
                        } else {
                            _action.value = ConfirmPasswordAction.ShowMessageError(it.second)
                        }
                    },
                    {
                        showProgress.value = false
                        _action.value =
                            ConfirmPasswordAction.ShowMessageError(it.message.toString())
                    }
                )
        )
    }

}