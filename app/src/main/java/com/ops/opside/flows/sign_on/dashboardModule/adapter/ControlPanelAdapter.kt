package com.ops.opside.flows.sign_on.dashboardModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.databinding.ItemControlPanelConcessionairePermissionBinding
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.ControlPanelViewModel

class ControlPanelAdapter(
    var collectorsList: MutableList<CollectorSE>,
    var mControlPanelViewModel: ControlPanelViewModel
) : RecyclerView.Adapter<ControlPanelAdapter.ViewHolder>() {

    private lateinit var context: Context

    /** Adapter And ViewHolder Configuration**/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_control_panel_concessionaire_permission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collectors = collectorsList[position]
        holder.apply {
            binding.tvConcessionaireName.text = collectors.name
            if (collectors.hasAccess) binding.switchHasAccess.isChecked = true
            binding.switchHasAccess.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    changeHasAccess(
                        collectors.name, collectors.idFirebase,
                        binding.switchHasAccess.isChecked
                    )
                }
                if (!isChecked) {
                    changeHasAccess(
                        collectors.name, collectors.idFirebase,
                        binding.switchHasAccess.isChecked
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = collectorsList.size


    /** Inner Class **/
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemControlPanelConcessionairePermissionBinding.bind(view)

        /** Other Methods**/
        fun changeHasAccess(collectorName: String, idFirestore: String, hasAccess: Boolean) {
            val dialog = BaseDialog(
                context,
                imageResource = R.drawable.ic_ops_warning,
                mTitle = context.getString(R.string.cp_alertdialog_title),
                mDescription = context.getString(
                    R.string.control_panel_alert_dialog_title,
                    collectorName
                ),
                buttonYesText = context.getString(R.string.common_accept),
                buttonNoText = context.getString(R.string.common_cancel),
                yesAction = {
                    mControlPanelViewModel.updateHasAccess(idFirestore, hasAccess)
                },
                noAction = {
                    binding.apply {
                        switchHasAccess.isChecked = switchHasAccess.isChecked
                    }
                }
            )
            dialog.setCancelable(false)
            dialog.show()
        }

    }
}