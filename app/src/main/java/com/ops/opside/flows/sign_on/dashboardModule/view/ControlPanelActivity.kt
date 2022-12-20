package com.ops.opside.flows.sign_on.dashboardModule.view

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.common.utils.TimePickerDialog.Companion.newInstance
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityControlPanelBinding
import com.ops.opside.flows.sign_off.registrationModule.actions.RegistrationAction
import com.ops.opside.flows.sign_on.dashboardModule.actions.ControlPanelAction
import com.ops.opside.flows.sign_on.dashboardModule.adapter.ControlPanelAdapter
import com.ops.opside.flows.sign_on.dashboardModule.interfaces.ControlPanelInterface
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.ControlPanelViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class ControlPanelActivity : BaseActivity(), ControlPanelInterface {

    private lateinit var mBinding: ActivityControlPanelBinding
    private lateinit var controlPanelAdapter: ControlPanelAdapter
    private val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    private val mControlPanelViewModel: ControlPanelViewModel by viewModels()
    private lateinit var mCollectorList: MutableList<CollectorSE>
    private var mPriceLinearMeter: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityControlPanelBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnSaveChanges.setOnClickListener { confirmUpdateLinearPrice() }
        }

        /** Methods call's **/
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
        mControlPanelViewModel.getShowProgress().observe(this, Observer(this::showLoading))
        mControlPanelViewModel.getCollectorList.observe(this, Observer(this::getCollectors))
        mControlPanelViewModel.priceLinearMeter.observe(this, Observer(this::getPriceLinearMeter))
        mControlPanelViewModel.getAction().observe(this, Observer(this::handleAction))

    }

    private fun loadCollectorList() {
        mControlPanelViewModel.getCollectorList()
    }

    private fun getCollectors(collectorList: MutableList<CollectorSE>) {
        mCollectorList = collectorList
        setUpRecyclerView()
    }

    private fun getPriceLinearMeter(priceLinearMeter: Float) {
        mPriceLinearMeter = priceLinearMeter
        mBinding.teLinealPrice.setText(mPriceLinearMeter.toString())
    }

    private fun loadPriceLinearMeter() {
        mControlPanelViewModel.getPriceLinearMeter()
    }

    /** Sealed Class handleAction**/
    private fun handleAction(action: ControlPanelAction) {
        when(action) {
            is ControlPanelAction.ShowMessageSuccess -> {
                toast(getString(R.string.control_panel_hasaccess_updated_success))
            }
            is ControlPanelAction.ShowMessageError -> {
                toast(getString(R.string.control_panel_hasaccess_updated_error))
            }
        }
    }

    /** Other Methods**/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
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

    private fun showDialog(
        initialHour: Int,
        initialMinute: Int,
        observer: TimePickerDialog.OnTimeSetListener
    ) {
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


    private fun setUpRecyclerView() {
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)
        controlPanelAdapter = ControlPanelAdapter(mCollectorList, this, mControlPanelViewModel)

        mBinding.recyclerControlPanel.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = controlPanelAdapter
        }

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

    private fun TextView.getTime(): LocalTime {
        return LocalTime.parse(text, formatter)
    }

    private fun TextView.setTime(time: LocalTime) {
        text = time.toTimeText()
    }

    fun LocalTime.toTimeText(): String {
        return format(formatter)
    }

    private infix fun LocalTime.hoursBetween(end: LocalTime): Double {
        return Duration.between(this, end).toMinutes() / 60.0
    }

    private fun confirmUpdateLinearPrice() {
        val dialog = BaseDialog(
            context = this,
            imageResource = R.drawable.ic_ops_btn_confirm,
            mTitle = getString(R.string.control_panel_dialog_title),
            mDescription = getString(R.string.control_panel_dialog_message),
            buttonNoText = getString(R.string.common_cancel),
            buttonYesText = getString(R.string.common_accept),
            yesAction = {
                //TODO Refact
                mControlPanelViewModel.updateLinealPriceMeter(
                    "Ulmp4yMD4noSlOE6IwpX",
                    mBinding.teLinealPrice.text.toString().trim()
                )
                toast(getString(R.string.control_panel_dialog_yes_action_message))
            },
            noAction = {
                toast(getString(R.string.control_panel_dialog_no_action_message))
            },
        )
        dialog.setCancelable(false)
        dialog.show()
    }

    /** Interface Methods **/
    override fun switchHasAccess(idFirestore: String, hasAccess: Boolean) {
        mControlPanelViewModel.updateHasAccess(idFirestore, hasAccess)
    }
}