package com.ops.opside.common.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ops.opside.databinding.DialogBaseBinding

class BaseDialog(
    context: Context,
    private val mTitle: String,
    private val mDescription: String,
    private val buttonYesText: String = "",
    private val buttonNoText: String = "",
    private val yesAction: () -> Unit = {},
    private val noAction: () -> Unit = {}
) : Dialog(context) {

    private lateinit var binding: DialogBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogBaseBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.apply {
            tvTitle.text = mTitle
            tvDescription.text = mDescription
            btnYes.text = buttonYesText
            btnNo.text = buttonNoText
            btnNo.isVisible = buttonNoText.isNotEmpty()
            btnYes.setOnClickListener {
                yesAction.invoke()
                dismiss()
            }
            btnNo.setOnClickListener {
                noAction.invoke()
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(width, height)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}