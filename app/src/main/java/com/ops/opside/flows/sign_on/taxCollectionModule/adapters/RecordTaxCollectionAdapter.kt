package com.ops.opside.flows.sign_on.taxCollectionModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.databinding.ItemRecordTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord

class RecordTaxCollectionAdapter(
    var events: MutableList<ItemRecord>,
) :
    RecyclerView.Adapter<RecordTaxCollectionAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_record_tax_collection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = events.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecordTaxCollectionBinding.bind(view)

        fun bind(item : ItemRecord){
            binding.apply {
                tvName.text = item.name
                tvHour.text = item.hour
                tvAction.text = item.action
                tvAmount.text = "$ ${item.amount}"

                when(item.action){
                    FLOOR_COLLECTION -> {
                        tvAction.setTextColor(mContext.getColor(R.color.secondaryLightColor))
                        imgIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_money))
                    }
                    ADDED -> {
                        tvAction.setTextColor(mContext.getColor(R.color.warning))
                        imgIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_shop))
                        tvAmount.isVisible = false
                    }
                    PENALTY_FEE -> {
                        tvAction.setTextColor(mContext.getColor(R.color.error))
                        imgIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_penalty_fee))
                        tvAmount.isVisible = false
                    }
                }

            }
        }

    }

}

const val FLOOR_COLLECTION = "Cobro de Piso"
const val ADDED = "Agregado al Tianguis"
const val PENALTY_FEE = "Multa"