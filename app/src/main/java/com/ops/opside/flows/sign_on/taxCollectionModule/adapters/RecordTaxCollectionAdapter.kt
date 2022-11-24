package com.ops.opside.flows.sign_on.taxCollectionModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.utils.FORMAT_TIME
import com.ops.opside.common.utils.FORMAT_TIMESTAMP
import com.ops.opside.common.utils.Formaters
import com.ops.opside.databinding.ItemRecordTaxCollectionBinding

class RecordTaxCollectionAdapter(var events: MutableList<EventRE>, ) :
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

        fun bind(item : EventRE){
            binding.apply {
                tvName.text = item.nameConcessionaire
                tvHour.text = Formaters.parseFormat(item.timeStamp, FORMAT_TIMESTAMP, FORMAT_TIME)
                tvAction.text = item.status
                tvAmount.text = "$ ${item.amount}"

                if (adapterPosition == itemCount - 1)
                    divider.isVisible = false

                when(item.status){
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