package com.ops.opside.flows.sign_on.concessionaireModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.Entities.Concessionaire
import com.ops.opside.databinding.ItemCrudConcessionaireBinding

class ConcessionaireAdapter (
    var concessionaires: MutableList<Concessionaire>,
) :
    RecyclerView.Adapter<ConcessionaireAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_crud_concessionaire, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = concessionaires[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = concessionaires.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCrudConcessionaireBinding.bind(view)

        fun bind(item : Concessionaire){
            binding.txtConcessionaire.text = item.name

            binding.imgShowMore.setOnClickListener {
                // TODO: Launch FinalizeTaxCollectionFragment
            }
        }

    }

}