package com.ops.opside.common.views

import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.utils.Manager
import com.ops.opside.common.entities.firestore.Moduls
import com.ops.opside.common.entities.firestore.Permission

open class BaseBottomSheetFragment: BottomSheetDialogFragment() {
    protected val mBaseActivity by lazy {
        requireActivity() as BaseActivity
    }

    fun showLoading(show: Boolean) {
        mBaseActivity.showLoading(show)
    }

    fun showMessage(message: String) {
        mBaseActivity.showMessage(message)
    }

    fun showError(message: String) {
        mBaseActivity.showError(message)
    }

    fun toast(message: String) = Toast.makeText(mBaseActivity,message, Toast.LENGTH_LONG).show()

    fun verify(modul: Moduls, permission: Permission): Boolean{
        return Manager.getInstance().hasPermission(modul,permission)
    }
}