package com.ops.opside.flows.sign_on.concessionaireModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.Entities.Market
import com.ops.opside.common.Utils.animateOnPress
import com.ops.opside.databinding.ItemTianguisBinding

class TianguisParticipatingAdapter (
    var tianguis: MutableList<Market>,
) :
    RecyclerView.Adapter<TianguisParticipatingAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_tianguis, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = tianguis[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = tianguis.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTianguisBinding.bind(view)

        fun bind(item : Market){

            binding.apply {
                imgShowMore.animateOnPress()
                imgShowMore.setOnClickListener {
                    Toast.makeText(mContext, "Mostrar Tianguis", Toast.LENGTH_SHORT).show()
                }

                txtTianguisName.text = item.name
            }

        }

    }

}