package com.ops.opside.flows.sign_on.taxCollectionModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.databinding.ItemAbsenceTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord

class AbsenceTaxCollectionAdapter(
    var events: MutableList<ItemAbsence>,
) :
    RecyclerView.Adapter<AbsenceTaxCollectionAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_absence_tax_collection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = events.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemAbsenceTaxCollectionBinding.bind(view)

        fun bind(item : ItemAbsence){
            binding.txtName.text = item.dealerName
        }

    }

}