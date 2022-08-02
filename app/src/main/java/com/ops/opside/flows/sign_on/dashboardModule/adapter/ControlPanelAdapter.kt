package com.ops.opside.flows.sign_on.dashboardModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.Entities.Concessionaire
import com.ops.opside.databinding.ItemControlPanelConcessionairePermissionBinding
import com.ops.opside.databinding.ItemMarketListBinding

class ControlPanelAdapter(var concessionaire: MutableList<Concessionaire>):
RecyclerView.Adapter<ControlPanelAdapter.ViewHolder>(){

    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_control_panel_concessionaire_permission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val concessionaire = concessionaire.get(position)
        holder.binding.tvConcessionaireName.text = concessionaire.name

    }

    override fun getItemCount(): Int = concessionaire.size

    fun setConcessionaires(concessionaire: List<Concessionaire>){
        this.concessionaire = concessionaire as MutableList<Concessionaire>
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemControlPanelConcessionairePermissionBinding.bind(view)
    }
}