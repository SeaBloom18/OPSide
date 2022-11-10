package com.ops.opside.flows.sign_on.dashboardModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.CollectorSE
import com.ops.opside.databinding.ItemControlPanelConcessionairePermissionBinding

class ControlPanelAdapter(var collectorsList: MutableList<CollectorSE>):
RecyclerView.Adapter<ControlPanelAdapter.ViewHolder>(){

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_control_panel_concessionaire_permission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collectors = collectorsList[position]
        holder.binding.tvConcessionaireName.text = collectors.name
    }

    override fun getItemCount(): Int = collectorsList.size

    /*fun setConcessionaires(concessionaireRE: List<ConcessionaireSE>){
        this.collectorsLits = concessionaireRE as MutableList<CollectorSE>
        notifyDataSetChanged()
    }*/

    /** Inner Class **/
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemControlPanelConcessionairePermissionBinding.bind(view)
    }
}