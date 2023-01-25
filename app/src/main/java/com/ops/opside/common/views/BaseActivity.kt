package com.ops.opside.common.views

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ops.opside.common.utils.Manager
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.firestore.Moduls
import com.ops.opside.common.entities.firestore.Permission
import com.ops.opside.common.utils.Formaters.orFalse
import com.ops.opside.common.utils.tryOrPrintException

open class BaseActivity : AppCompatActivity() {
    private var progressDialog: Dialog? = null
    private var baseDialog: BaseDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = Dialog(this, R.style.dialogTransparent)
        progressDialog?.setContentView(R.layout.dialog_loading)
        progressDialog?.setCancelable(false)
    }

    protected fun getFragmentByTag(fragmentTag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(fragmentTag)
    }

    protected fun getCurrentVisibleFragment(): Fragment? {
        return supportFragmentManager.fragments.lastOrNull { it.isVisible }
    }

    protected fun removeAllFragments() {
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    fun launchFragment(
        fragment: Fragment,
        fragmentManager: FragmentManager,
        containerId: Int,
        bundle: Bundle? = null
    ) {
        tryOrPrintException {
            bundle?.let { fragment.arguments = it }

            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.add(containerId, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    fun showLoading(show: Boolean) {
        if (show) {
            progressDialog?.show()
        } else {
            progressDialog?.dismiss()
        }
    }

    fun isProgressDialogVisible() = progressDialog?.isShowing.orFalse()

    fun showError(message: String) {
        val dialog = BaseDialog(
            this,
            R.drawable.ic_ops_error,
            getString(R.string.common_atention),
            message,
            getString(R.string.common_cancel)
        )

        dialog.show()
    }

    fun showMessage(message: String) {
        if (message.trim().isNotEmpty()) {
            showDialog(getString(R.string.common_atention), message)
        }
    }

    fun showDialog(
        title: String,
        message: String,
        imageResource: Int = 0,
        buttonYesTitle: String = "",
        buttonNoTitle: String = "",
        funButtonYes: () -> Unit = {},
        funButtonNo: () -> Unit = {}
    ) {

        baseDialog?.let {
            if (it.isShowing) return
        }

        baseDialog = BaseDialog(
            context = this,
            mTitle = title,
            mDescription = message,
            imageResource = imageResource,
            buttonYesText = buttonYesTitle,
            buttonNoText = buttonNoTitle,
            yesAction = funButtonYes,
            noAction = funButtonNo
        )

        baseDialog?.show()
    }

    fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    fun verify(modul: Moduls, permission: Permission): Boolean{
        return Manager.getInstance().hasPermission(modul,permission)
    }
}