package com.ops.opside.common.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ops.opside.R
import java.time.LocalTime

class TimePickerDialog : DialogFragment() {
    private var timeObserver: TimePickerDialog.OnTimeSetListener? = null

    private var hour: Int
    private var minute: Int

    init {
        val now = LocalTime.now()
        hour = now.hour
        minute = now.minute
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            hour = args.getInt(HOUR_ARG)
            minute = args.getInt(MINUTE_ARG)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //DateFormat.is24HourFormat()
        val dialog = TimePickerDialog(
            requireActivity(),
            R.style.ThemeOverlay_App_TimePicker,
            timeObserver,
            hour,
            minute,
            false
        )
        return dialog
    }

    companion object {
        private const val MINUTE_ARG: String = "args.minute"
        private const val HOUR_ARG: String = "args.hour"

        fun newInstance(
            hour: Int, minute: Int, observer: TimePickerDialog.OnTimeSetListener
        ): com.ops.opside.common.utils.TimePickerDialog {

            val args = Bundle().apply {
                putInt(HOUR_ARG, hour)
                putInt(MINUTE_ARG, minute)
            }

            return TimePickerDialog().apply {
                timeObserver = observer
                arguments = args
            }
        }
    }
}