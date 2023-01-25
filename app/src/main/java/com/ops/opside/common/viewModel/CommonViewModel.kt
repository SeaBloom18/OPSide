package com.ops.opside.common.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ops.opside.common.utils.Manager
import com.ops.opside.common.entities.firestore.Moduls
import com.ops.opside.common.entities.firestore.Permission
import com.ops.opside.common.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

abstract class CommonViewModel : ViewModel() {
    protected val disposable = CompositeDisposable()
    protected val showProgress = SingleLiveEvent<Boolean>()

    fun getShowProgress(): LiveData<Boolean> = showProgress

    override fun onCleared() {
        disposable.clear()
    }

    fun verify(modul: Moduls, permission: Permission): Boolean{
        return Manager.getInstance().hasPermission(modul,permission)
    }

}