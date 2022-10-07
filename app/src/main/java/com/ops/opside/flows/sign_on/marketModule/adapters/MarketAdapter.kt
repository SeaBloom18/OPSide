package com.ops.opside.flows.sign_on.marketModule.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.PUT_EXTRA_MARKET
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.databinding.ItemMarketListBinding
import com.ops.opside.flows.sign_on.marketModule.view.MarketRegisterActivity

class MarketAdapter(private var markets: MutableList<MarketSE>, private var listener: OnClickListener):
    RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    private lateinit var context: Context

    /** ADAPTER SETUP **/
    /** CreateViewHolder **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_market_list, parent, false)
        return ViewHolder(view)
    }

    /** BindViewHolder **/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = markets[position]
        with(holder){
            binding.tvMarketName.text = market.name
            binding.tvItemAddress.text = market.address
            binding.tvItemConcessionaires.text = market.numberConcessionaires.toString().length.toString()

            binding.ivDelete.setOnClickListener {
                listener.onDeleteMarket(market.idFirebase)
                //markets.removeAt(position)
                notifyDataSetChanged()
            }
            binding.ivEdit.setOnClickListener {
                val intent = Intent(context, MarketRegisterActivity::class.java)
                intent.putExtra(PUT_EXTRA_MARKET, market)
                context.startActivity(intent)
            }
        }

    }

    /** ItemCount **/
    override fun getItemCount(): Int = markets.size

    /** Inner Class **/
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemMarketListBinding.bind(view)
    }
}