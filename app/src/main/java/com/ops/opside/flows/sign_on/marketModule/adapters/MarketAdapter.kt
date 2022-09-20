package com.ops.opside.flows.sign_on.marketModule.adapters

import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.databinding.ItemMarketListBinding

class MarketAdapter(private var markets: MutableList<MarketSE>, private var listener: OnClickListener):
    RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_market_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = markets[position]
        with(holder){
            binding.tvMarketName.text = market.name
            binding.tvItemAddress.text = market.address
            binding.tvItemConcessionaires.text = "Concessionaires count: ${market.numberConcessionaires}"

            binding.ivDelete.setOnClickListener { listener.onDeleteMarket(market.idFirebase)}
            binding.ivEdit.setOnClickListener { listener.onEditMarket(market) }
        }

        //setUpItem(holder)
    }

    override fun getItemCount(): Int = markets.size

    //Inner class
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemMarketListBinding.bind(view)
    }

    /*private fun setUpItem(holder: ViewHolder){
        holder.binding.ibArrow.setOnClickListener {
            if (holder.binding.group.visibility == View.GONE){ //Si la vista esta oculta
                TransitionManager.beginDelayedTransition(holder.binding.container)
                holder.binding.group.visibility = View.VISIBLE
                holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_up)
            } else{ //Si la vista esta expuesta
                TransitionManager.endTransitions(holder.binding.container)
                holder.binding.group.visibility = View.GONE
                holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_down)
            }
        }
    }*/
}