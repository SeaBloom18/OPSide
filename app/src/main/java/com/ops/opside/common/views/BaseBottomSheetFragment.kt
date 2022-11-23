package com.ops.opside.common.views

import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
}