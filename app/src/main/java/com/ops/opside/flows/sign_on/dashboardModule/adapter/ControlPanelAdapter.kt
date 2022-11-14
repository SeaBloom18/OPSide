package com.ops.opside.flows.sign_on.dashboardModule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.DB_TABLE_COLLECTOR
import com.ops.opside.common.entities.DB_TABLE_MARKET
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.databinding.ItemControlPanelConcessionairePermissionBinding
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.ControlPanelViewModel
import com.ops.opside.flows.sign_on.marketModule.viewModel.MarketViewModel

class ControlPanelAdapter(var collectorsList: MutableList<CollectorSE>):
RecyclerView.Adapter<ControlPanelAdapter.ViewHolder>(){

    private lateinit var context: Context
    val firestore = Firebase.firestore

    /** Adapter And ViewHolder Configuration**/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_control_panel_concessionaire_permission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collectors = collectorsList[position]
        holder.apply {
            binding.tvConcessionaireName.text = collectors.name
            if (collectors.hasAccess) binding.switchHasAccess.isChecked = true
            binding.switchHasAccess.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    changeHasAccess(collectors.name, collectors.idFirebase,
                        binding.switchHasAccess.isChecked)
                }
                if (!isChecked) {
                    changeHasAccess(collectors.name, collectors.idFirebase,
                        binding.switchHasAccess.isChecked)
                }
            }
        }
    }

    override fun getItemCount(): Int = collectorsList.size

    /*fun setConcessionaires(concessionaireRE: List<ConcessionaireSE>){
        this.collectorsLits = concessionaireRE as MutableList<CollectorSE>
        notifyDataSetChanged()
    }*/

    /** Inner Class **/
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemControlPanelConcessionairePermissionBinding.bind(view)

        /** Other Methods**/
        fun changeHasAccess(collectorName: String, idFirestore: String, hasAccess: Boolean) {
            val dialog = BaseDialog(
                context,
                imageResource = R.drawable.ic_ops_warning,
                mTitle = context.getString(R.string.cp_alertdialog_title),
                mDescription = context.getString(R.string.control_panel_alert_dialog_title, collectorName),
                buttonYesText = context.getString(R.string.common_accept),
                buttonNoText = context.getString(R.string.common_cancel),
                yesAction = {
                    updateHasAccess(idFirestore, hasAccess)
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

        private fun updateHasAccess(idFirestore: String, hasAccess: Boolean) {
            tryOrPrintException {
                firestore.collection(DB_TABLE_COLLECTOR).document(idFirestore).update("hasAccess", hasAccess)
                    .addOnSuccessListener {
                        Log.d("FireStoreDelete", "DocumentSnapshot successfully deleted!")
                        Toast.makeText(context, "Acceso Actualizado correctamente!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Log.w("FireStoreDelete", "Error deleting document", it)
                        Toast.makeText(context, "Error al actulizar acceso!", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}