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
import com.ops.opside.flows.sign_on.marketModule.interfaces.OnClickListener
import com.ops.opside.flows.sign_on.marketModule.view.MarketRegisterActivity
import java.util.*

class MarketAdapter(var markets: MutableList<MarketSE>, private var listener: OnClickListener):
    RecyclerView.Adapter<MarketAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val mFilteredData: MutableList<MarketSE> = mutableListOf()

    /** ADAPTER SETUP **/
    /** CreateViewHolder **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        if (mFilteredData.isEmpty())
            mFilteredData.addAll(markets)
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_market_list, parent, false)
        return ViewHolder(view)
    }

    /** BindViewHolder **/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = markets[position]
        with(holder){
            binding.tvMarketName.text = market.name
            binding.tvItemAddress.text = market.address
            //TODO agregar string ${getString(R.string.tv_linear_meter)}:
            binding.tvMarketMeters.text = "Metros Lineales: ${market.marketMeters}"
            binding.ivDelete.setOnClickListener {
                listener.onDeleteMarket(market.idFirebase, position)
            }
            binding.ivEdit.setOnClickListener {
                val intent = Intent(context, MarketRegisterActivity::class.java)
                intent.putExtra(PUT_EXTRA_MARKET, market)
                context.startActivity(intent)
                notifyItemChanged(position)
            }
        }
    }

    /** ItemCount **/
    override fun getItemCount(): Int = markets.size

    fun filter(filterText: String) {
        markets.clear()
        if (filterText.isEmpty()) {
            markets.addAll(mFilteredData)
        } else {
            for (market in mFilteredData) {
                if (market.name.lowercase(Locale.getDefault())
                        .contains(filterText.lowercase(Locale.getDefault()))
                ) {
                    this.markets.add(market)
                }
            }
        }
        notifyDataSetChanged()
    }

    /** Inner Class **/
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemMarketListBinding.bind(view)
    }

    fun updateListPostDelete(position: Int) {
        markets.removeAt(position)
        notifyItemRemoved(position)
    }
}