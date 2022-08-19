package com.ops.opside.common.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup

class ProgressDialog(
    context: Context,
) : Dialog(context) {

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(width, height)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}