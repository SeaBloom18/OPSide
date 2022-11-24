package com.ops.opside.common.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ops.opside.databinding.DialogLoadingBinding

class ProgressDialog(
    context: Context,
) : Dialog(context) {

    private lateinit var mBinding: DialogLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(width, height)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}