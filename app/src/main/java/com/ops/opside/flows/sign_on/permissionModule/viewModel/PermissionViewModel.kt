package com.ops.opside.flows.sign_on.permissionModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.entities.firestore.ModulsTree
import com.ops.opside.common.utils.applySchedulers
import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.permissionModule.model.PermissionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val mInteractor : PermissionInteractor
): CommonViewModel() {
    private val _getAllRoles = MutableLiveData<MutableMap<String,ModulsTree>>()
    private val _createNewRol = MutableLiveData<Boolean>()

    val getAllRoles: LiveData<MutableMap<String,ModulsTree>> = _getAllRoles
    val createNewRol: LiveData<Boolean> = _createNewRol

    fun getAllRoles(){
        disposable.add(
            mInteractor.getAllRoles().applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _getAllRoles.value = it
                    },
                    {
                        showProgress.value = false
                    }
                )
        )
    }

    fun createNewRol(tree: ModulsTree){
        disposable.add(
            mInteractor.createNewRol(tree).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        _createNewRol.value = it
                    },
                    {
                        showProgress.value = false
                    }
                )
        )
    }
}