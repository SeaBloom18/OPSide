package com.ops.opside.common.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.utils.Formaters.orFalse

inline fun tryOrPrintException(f: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AppCompatActivity.launchFragment(
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

fun AppCompatActivity.launchActivity(
    activity: Activity,
    context: Context,
    bundle: Bundle? = null
) {
    tryOrPrintException {
        val intent = Intent(
            context,
            activity::class.java
        )

        bundle?.let { intent.putExtras(bundle) }
        startActivity(intent)
    }
}

fun AppCompatActivity.showError(message: String){
    val dialog = BaseDialog(
        this,
        getString(R.string.common_atention),
        message,
        getString(R.string.common_cancel)
    )

    dialog.show()
}

fun Fragment.showError(message: String){
    val dialog = BaseDialog(
        requireContext(),
        getString(R.string.common_atention),
        message,
        getString(R.string.common_cancel)
    )

    dialog.show()
}

fun AppCompatActivity.showLoading(show: Boolean) {
    ProgressDialogHelper.showLoading(show, this)
}

private object ProgressDialogHelper {
    private var progressDialog: Dialog? = null

    fun showLoading(show: Boolean, context: Context) {
        if (show) {
            progressDialog = Dialog(context, R.style.dialogTransparent)
            progressDialog?.setContentView(R.layout.dialog_loading)
            progressDialog?.setCancelable(false)

            progressDialog?.show()
        } else {
            progressDialog?.dismiss()
        }
    }

    fun isProgressDialogVisible() = progressDialog?.isShowing.orFalse()
}

fun Fragment.launchActivity(
    activity: Activity,
    context: Context,
    bundle: Bundle? = null
) {
    tryOrPrintException {
        val intent = Intent(
            context,
            activity::class.java
        )

        bundle?.let { intent.putExtras(bundle) }
        startActivity(intent)
    }
}

fun TextInputEditText.clear(){
    text?.clear()
}

