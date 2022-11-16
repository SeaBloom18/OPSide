package com.ops.opside.flows.sign_on.dashboardModule.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.common.utils.TimePickerDialog.Companion.newInstance
import com.ops.opside.common.utils.toast
import com.ops.opside.databinding.ActivityControlPanelBinding
import com.ops.opside.flows.sign_on.dashboardModule.adapter.ControlPanelAdapter
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.ControlPanelViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class ControlPanelActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityControlPanelBinding
    private lateinit var controlPanelAdapter: ControlPanelAdapter
    private val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    private val mControlPanelViewModel: ControlPanelViewModel by viewModels()
    private lateinit var mCollectorList: MutableList<CollectorSE>
    private var mPriceLinearMeter: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityControlPanelBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnSaveChanges.setOnClickListener {
                mControlPanelViewModel.updateLinealPriceMeter("Ulmp4yMD4noSlOE6IwpX",
                    mBinding.teLinealPrice.text.toString().trim())
            }
        }

        setToolbar()
        bindViewModel()
        loadCollectorList()
        loadPriceLinearMeter()
        setUpStartTime()
        setUpEndTime()
        updateDuration()
    }

    /** ViewModel and Methods SetUp**/
    private fun bindViewModel() {
        mControlPanelViewModel.getCollectorList.observe(this, Observer(this::getCollectors))
        mControlPanelViewModel.priceLinearMeter.observe(this, Observer(this::getPriceLinearMeter))
    }

    private fun loadCollectorList() {
        mControlPanelViewModel.getCollectorList()
    }

    private fun getCollectors(collectorList: MutableList<CollectorSE>) {
        mCollectorList = collectorList
        setUpRecyclerView()
    }

    private fun getPriceLinearMeter(priceLinearMeter: String) {
        mPriceLinearMeter = priceLinearMeter
        mBinding.teLinealPrice.setText(mPriceLinearMeter)
    }

    private fun loadPriceLinearMeter() {
        mControlPanelViewModel.getPriceLinearMeter()
    }

    /** Other Methods**/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar(){
        with(mBinding.toolbarControlP.commonToolbar) {
            this.title = getString(R.string.bn_menu_control_panel_opc4)
            setSupportActionBar(this)
            (context as ControlPanelActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
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
        newInstance(initialHour, initialMinute, observer)
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
            R.drawable.ic_store,
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
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)
        controlPanelAdapter = ControlPanelAdapter(mCollectorList, mControlPanelViewModel)

        mBinding.recyclerControlPanel.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = controlPanelAdapter
        }

        /*controlPanelAdapter = ControlPanelAdapter(getConcessionaires())
        linearLayoutManager = LinearLayoutManager(this)

        val recycler = mBinding.recycler.apply {
            layoutManager = linearLayoutManager
            adapter = controlPanelAdapter
        }*/

        //Swipe to Delete
        /*val swipeHandler = object : SwipeToDeleteCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                controlPanelAdapter.collectorsList.removeAt(viewHolder.adapterPosition)
                controlPanelAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mBinding.recycler)*/
    }

    fun TextView.getTime(): LocalTime {
        return LocalTime.parse(text, formatter)
    }

    fun TextView.setTime(time: LocalTime) {
        text = time.toTimeText()
    }

    fun LocalTime.toTimeText(): String {
        return format(formatter)
    }

    infix fun LocalTime.hoursBetween(end: LocalTime): Double {
        return Duration.between(this, end).toMinutes() / 60.0
    }
}