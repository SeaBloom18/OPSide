package com.ops.opside.flows.sign_on.dashboardModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.databinding.ItemControlPanelConcessionairePermissionBinding

class ControlPanelAdapter(var concessionaireRE: MutableList<ConcessionaireSE>):
RecyclerView.Adapter<ControlPanelAdapter.ViewHolder>(){

    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_control_panel_concessionaire_permission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val concessionaire = concessionaireRE.get(position)
        holder.binding.tvConcessionaireName.text = concessionaire.name

    }

    override fun getItemCount(): Int = concessionaireRE.size

    fun setConcessionaires(concessionaireRE: List<ConcessionaireSE>){
        this.concessionaireRE = concessionaireRE as MutableList<ConcessionaireSE>
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemControlPanelConcessionairePermissionBinding.bind(view)
    }
}