package com.ops.opside.flows.sign_on.incidentsModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.IncidentEntity
import com.ops.opside.databinding.ItemSeeIncidentBinding

class ListIncidentsAdapter(private var incidentEntity: MutableList<IncidentEntity>):
    RecyclerView.Adapter<ListIncidentsAdapter.ViewHolder>(){

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_see_incident, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val incident = incidentEntity[position]
        with(holder){
            binding.tvIncidentName.text = incident.incidentName
            binding.tvIncidentPrice.text = "Precio: ${incident.incidentPrice}"
            binding.tvIncidentCode.text = incident.incidentCode.toString()
        }
    }

    override fun getItemCount(): Int = incidentEntity.size

    //inner class
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemSeeIncidentBinding.bind(view)
    }
}