package com.ops.opside.flows.sign_on.dashboardModule.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.getTime
import com.ops.opside.common.utils.hoursBetween
import com.ops.opside.common.utils.setTime
import com.ops.opside.common.utils.toTimeText
import com.ops.opside.common.adapters.SwipeToDeleteCallback
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.databinding.ActivityControlPanelBinding
import com.ops.opside.flows.sign_on.dashboardModule.adapter.ControlPanelAdapter
import java.time.LocalTime

class ControlPanelActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityControlPanelBinding
    private lateinit var controlPanelAdapter: ControlPanelAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityControlPanelBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.ibCPClose.setOnClickListener { finish() }
        mBinding.btnSaveChanges.setOnClickListener { confirmChanges() }

        setUpRecyclerView()

        setUpStartTime()

        setUpEndTime()

        updateDuration()

    }

    private fun updateDuration() {
        val start = mBinding.tvDesde.getTime()
        val end = mBinding.tvHasta.getTime()

        val hours = start hoursBetween end

        //duration.text = "%.1f Horas".format(hours)
    }

    private fun setUpEndTime() {
        mBinding.tvHasta.text = LocalTime.now().toTimeText()
        mBinding.tvHasta.setOnClickListener {
            showEndTimePicker()
        }
    }

    private fun setUpStartTime() {
        mBinding.tvDesde.text = LocalTime.now().toTimeText()
        mBinding.tvDesde.setOnClickListener {
            showStartTimePicker()
        }
    }

    private fun showStartTimePicker() {
        val time = mBinding.tvDesde.getTime()
        showDialog(time.hour, time.minute) { _, hour, minute ->
            val currentTime = LocalTime.of(hour, minute)
            if (isValidStartTime(currentTime)) {
                mBinding.tvDesde.setTime(currentTime)
                updateDuration()
            }
        }
    }

    private fun isValidStartTime(time: LocalTime): Boolean {
        return time < mBinding.tvHasta.getTime()
    }

    private fun showDialog(initialHour: Int, initialMinute: Int, observer: TimePickerDialog.OnTimeSetListener) {
        com.ops.opside.common.utils.TimePickerDialog.newInstance(initialHour, initialMinute, observer)
            .show(supportFragmentManager, "time-picker")
    }

    private fun showEndTimePicker() {
        val time = mBinding.tvHasta.getTime()
        showDialog(
            time.hour,
            time.minute
        ) { _, hour, minute ->
            val currentTime = LocalTime.of(hour, minute)

            if (isValidEndTime(currentTime)) {
                mBinding.tvHasta.setTime(currentTime)
                updateDuration()
            }
        }
    }

    private fun isValidEndTime(time: LocalTime): Boolean {
        return time > mBinding.tvDesde.getTime()
    }

    private fun confirmChanges() {
        val dialog = BaseDialog(
            this,
            getString(R.string.cp_alertdialog_title),
            getString(R.string.cp_alertdialog_message),
            getString(R.string.common_accept),
            "",
            { Toast.makeText(this, R.string.cp_toast_success, Toast.LENGTH_SHORT).show() },
            { Toast.makeText(this, "onCancel()", Toast.LENGTH_SHORT).show() },
        )

        dialog.show()
    }

    private fun setUpRecyclerView() {
        controlPanelAdapter = ControlPanelAdapter(getConcessionaires())
        linearLayoutManager = LinearLayoutManager(this)

        val recycler = mBinding.recycler.apply {
            layoutManager = linearLayoutManager
            adapter = controlPanelAdapter
        }

        //Swipe to Delete
        val swipeHandler = object : SwipeToDeleteCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                controlPanelAdapter.concessionaireRE.removeAt(viewHolder.adapterPosition)
                controlPanelAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycler)
    }

    private fun getConcessionaires(): MutableList<ConcessionaireSE> {
        val concessionaireRES = mutableListOf<ConcessionaireSE>()

        val concessionaireRE1 = ConcessionaireSE(id = (1).toLong(), idFirebase = "", name = "David Gonzalez")
        val concessionaireRE2 = ConcessionaireSE(id = (1).toLong(), idFirebase = "", name = "Mario Razo")

        concessionaireRES.add(concessionaireRE1)
        concessionaireRES.add(concessionaireRE2)
        concessionaireRES.add(concessionaireRE1)
        concessionaireRES.add(concessionaireRE2)

        return concessionaireRES
    }
}