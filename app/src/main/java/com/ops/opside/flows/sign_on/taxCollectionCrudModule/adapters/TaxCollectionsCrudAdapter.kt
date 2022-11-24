package com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.FORMAT_DATE
import com.ops.opside.common.utils.FORMAT_SQL_DATE
import com.ops.opside.common.utils.Formaters
import com.ops.opside.common.utils.Formaters.formatCurrency
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.ItemCrudTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.interfaces.TaxCollectionCrudAux
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.view.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.FinalizeTaxCollectionFragment


class TaxCollectionsCrudAdapter(
    private var mEvents: MutableList<TaxCollectionSE>,
    private val mListener: TaxCollectionCrudAux,
    private val mActivity: TaxCollectionCrudActivity
) :
    RecyclerView.Adapter<TaxCollectionsCrudAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_crud_tax_collection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mEvents[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = mEvents.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCrudTaxCollectionBinding.bind(view)

        fun bind(item: TaxCollectionSE) {
            binding.apply {
                txtMarketName.text = item.marketName
                txtDate.text = Formaters.parseFormat(item.startDate, FORMAT_SQL_DATE, FORMAT_DATE)
                txtTotalAmount.text = item.totalAmount.formatCurrency()

                imgShowMore.animateOnPress()
                imgShowMore.setOnClickListener {
                    launchFinalizeFragment()
                }
            }
        }

    }

    private fun launchFinalizeFragment() {
        val bundle = Bundle()
        bundle.putString("type", "update")

        mActivity.launchFragment(
            FinalizeTaxCollectionFragment(),
            mActivity.supportFragmentManager,
            R.id.container,
            bundle
        )
        mListener.hideButtons()
    }

}