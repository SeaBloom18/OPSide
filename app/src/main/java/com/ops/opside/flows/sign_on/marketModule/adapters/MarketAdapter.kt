package com.ops.opside.flows.sign_on.marketModule.adapters

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.Entities.Market
import com.ops.opside.databinding.ItemMarketListBinding

class MarketAdapter(private val markets: List<Market>):
    RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_market_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = markets.get(position)
        with(holder){
            binding.tvMarketName.text = market.name
            binding.group.visibility = View.GONE
        }

        holder.binding.ibArrow.setOnClickListener {
            if (holder.binding.group.visibility == View.GONE){ //Si la vista esta oculta
                TransitionManager.beginDelayedTransition(holder.binding.marketCardView)
                holder.binding.group.visibility = View.VISIBLE
                holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_up)
            } else{ //Si la vista esta expuesta
                TransitionManager.endTransitions(holder.binding.marketCardView)
                holder.binding.group.visibility = View.GONE
                holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_down)
            }
            //Toast.makeText(context, "clic", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = markets.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemMarketListBinding.bind(view)
    }
}