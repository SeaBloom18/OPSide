package com.ops.opside.flows.sign_on.concessionaireModule.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.databinding.ItemCrudConcessionaireBinding
import com.ops.opside.flows.sign_on.concessionaireModule.view.ConcessionaireCrudActivity

class ConcessionaireAdapter (
    var concessionaireRES: MutableList<ConcessionaireSE>,
) :
    RecyclerView.Adapter<ConcessionaireAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_crud_concessionaire, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = concessionaireRES[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = concessionaireRES.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCrudConcessionaireBinding.bind(view)

        fun bind(item : ConcessionaireSE){
            binding.txtConcessionaire.text = item.name

            binding.imgShowMore.setOnClickListener {
                val intent = Intent(mContext,
                    ConcessionaireCrudActivity::class.java)
                mContext.startActivity(intent)
            }
        }

    }

}