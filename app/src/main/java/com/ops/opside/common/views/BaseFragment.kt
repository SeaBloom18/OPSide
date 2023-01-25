package com.ops.opside.common.views

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ops.opside.common.utils.Manager
import com.ops.opside.common.entities.firestore.Moduls
import com.ops.opside.common.entities.firestore.Permission

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

    fun verify(modul: Moduls, permission: Permission): Boolean{
        return Manager.getInstance().hasPermission(modul,permission)
    }
}