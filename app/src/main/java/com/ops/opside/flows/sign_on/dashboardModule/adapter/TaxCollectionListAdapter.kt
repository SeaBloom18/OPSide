package com.ops.opside.flows.sign_on.dashboardModule.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.FORMAT_DATE
import com.ops.opside.common.utils.FORMAT_SQL_DATE
import com.ops.opside.common.utils.Formaters
import com.ops.opside.common.utils.Formaters.formatCurrency
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.ItemCrudTaxCollectionBinding
import com.ops.opside.databinding.ItemMarketListBinding
import com.ops.opside.flows.sign_on.dashboardModule.interfaces.TaxCollectionListInterface
import com.ops.opside.flows.sign_on.dashboardModule.view.DashBoardFragment
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.FinalizeTaxCollectionFragment

/**
 * Created by davidgonzalez on 14/01/23
 */
class TaxCollectionListAdapter(
    private var taxCollectionList: MutableList<TaxCollectionSE>,
    private val mActivity: MainActivity):
    RecyclerView.Adapter<TaxCollectionListAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_crud_tax_collection, parent,
            false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = taxCollectionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taxCollection = taxCollectionList[position]
        with(holder) {
            bind(taxCollection)
            /*itemView.setOnClickListener {
                launchFinalizeFragment()
                Toast.makeText(context, "click $position", Toast.LENGTH_SHORT).show()

            }*/
        }
    }

    /** View Holder Inner Class **/
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemCrudTaxCollectionBinding.bind(view)

        fun bind(item: TaxCollectionSE) {
            binding.apply {
                txtMarketName.text = item.marketName
                tvCollectionTime.text = "Time\n${item.startTime}, to ${item.endTime}"
                txtDate.text = "Date\n${Formaters.parseFormat(item.startDate, FORMAT_SQL_DATE, FORMAT_DATE)}"
                txtTotalAmount.text = "Total: ${item.totalAmount.formatCurrency()}"
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
        //mListener.hideButtons()
    }
}