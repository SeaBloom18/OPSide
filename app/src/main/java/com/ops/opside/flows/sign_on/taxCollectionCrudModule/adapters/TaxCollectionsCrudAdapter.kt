package com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.Entities.TaxCollectionEntity
import com.ops.opside.common.Utils.Formaters
import com.ops.opside.common.Utils.Formaters.formatCurrency
import com.ops.opside.common.Utils.tryOrPrintException
import com.ops.opside.databinding.ItemCrudTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.interfaces.TaxCollectionCrudAux
import com.ops.opside.flows.sign_on.taxCollectionModule.view.FinalizeTaxCollectionFragment


class TaxCollectionsCrudAdapter (
    var events: MutableList<TaxCollectionEntity>,
    val listener: TaxCollectionCrudAux) :
    RecyclerView.Adapter<TaxCollectionsCrudAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_crud_tax_collection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = events.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCrudTaxCollectionBinding.bind(view)

        fun bind(item : TaxCollectionEntity){
            binding.apply {
                txtTianguisName.text = item.tianguisName
                txtDate.text = Formaters.formatDate(item.date)
                txtTotalAmount.text = item.totalAmount.formatCurrency()

                imgShowMore.setOnClickListener {
                    launchFinalizeFragment()
                }
            }
        }

    }

    private fun launchFinalizeFragment() {
        tryOrPrintException {
            val fragment = FinalizeTaxCollectionFragment()

            val mBundle = Bundle()
            mBundle.putString("type", "update")
            fragment.arguments = mBundle

            val fragmentManager = (mContext as FragmentActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.add(R.id.container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            listener.hideButtons()
        }
    }

}