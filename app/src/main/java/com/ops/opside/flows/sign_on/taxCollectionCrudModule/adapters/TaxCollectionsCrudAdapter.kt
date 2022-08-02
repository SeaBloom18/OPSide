package com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.Utils.Formaters
import com.ops.opside.databinding.ItemCrudTaxCollectionBinding
import com.ops.opside.common.Entities.TaxCollectionModel

class TaxCollectionsCrudAdapter (
    var events: MutableList<TaxCollectionModel>,
) :
    RecyclerView.Adapter<TaxCollectionsCrudAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_crud_tax_collection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = events.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCrudTaxCollectionBinding.bind(view)

        fun bind(item : TaxCollectionModel){
            binding.apply {
                txtTianguisName.text = item.tianguisName
                txtDate.text = Formaters.formatDate(item.date)
                txtTotalAmount.text = Formaters.formatMoney(item.totalAmount)
            }
        }

    }

}