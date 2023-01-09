package com.ops.opside.common.views

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {
    protected val mBaseActivity by lazy {
        requireActivity() as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun toast(message: String) = Toast.makeText(mBaseActivity, message, Toast.LENGTH_LONG).show()
}