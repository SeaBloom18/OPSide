package com.ops.opside.flows.sign_on.concessionaireModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.ItemMarketBinding

class MarketParticipatingAdapter (
    var markets: MutableList<MarketSE>,
) :
    RecyclerView.Adapter<MarketParticipatingAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_market, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = markets[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = markets.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMarketBinding.bind(view)

        fun bind(item : MarketSE){

            binding.apply {
                imgShowMore.animateOnPress()
                imgShowMore.setOnClickListener {
                    Toast.makeText(mContext, "Mostrar Tianguis", Toast.LENGTH_SHORT).show()
                }

                txtMarketName.text = item.name
            }

        }

    }

}