package com.ops.opside.flows.sign_on.incidentsModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.IncidentPersonEntity
import com.ops.opside.databinding.ItemRecollectionIncidentsBinding

class IncidentAdapter(private var incidentPersonEntity: MutableList<IncidentPersonEntity>):
    RecyclerView.Adapter<IncidentAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_recollection_incidents, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val incident = incidentPersonEntity.get(position)
        with(holder){
            binding.tvReportName.text = incident.reportName
            binding.tvCompoundTextAssign.text = "Incidencia asignada a ${incident.assignName} con la fecha ${incident.date}, $${incident.price}"
            binding.tvIncidentCode.text = incident.code.toString()
        }
    }

    override fun getItemCount(): Int = incidentPersonEntity.size

    //inner class
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemRecollectionIncidentsBinding.bind(view)
    }
}