package com.ops.opside.common.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.ops.opside.R
import com.ops.opside.databinding.DialogBaseBinding

class BaseDialog(
    context: Context,
    private val imageResource: Int = 0,
    //private val typeDialog: String? = DIALOG_WARNING,
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
            ivDialog.setImageResource(imageResource)
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

            /*when(typeDialog){
                DIALOG_WARNING -> ivDialog.setColorFilter(R.color.warning)
                DIALOG_ERROR -> ivDialog.setColorFilter(R.color.error)
                DIALOG_INFO -> ivDialog.setColorFilter(R.color.info)
                DIALOG_SUCCESS -> ivDialog.setColorFilter(R.color.secondaryColor)
            }*/
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

const val DIALOG_WARNING = "warning"
const val DIALOG_ERROR = "error"
const val DIALOG_INFO = "info"
const val DIALOG_SUCCESS = "success"