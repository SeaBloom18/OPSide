package com.ops.opside.flows.sign_on.concessionaireModule.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.databinding.ItemCrudConcessionaireBinding
import com.ops.opside.flows.sign_on.concessionaireModule.view.ConcessionaireCrudActivity
import java.util.*


class ConcessionaireAdapter(
    var concessionaireRES: MutableList<ConcessionaireSE>
) :
    RecyclerView.Adapter<ConcessionaireAdapter.ViewHolder>() {

    private lateinit var mContext: Context
    private val mFilteredData: MutableList<ConcessionaireSE> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        if (mFilteredData.isEmpty())
            mFilteredData.addAll(concessionaireRES)
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_crud_concessionaire, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = concessionaireRES[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = concessionaireRES.size

    fun filter(filterText: String) {
        concessionaireRES.clear()
        if (filterText.isEmpty()) {
            concessionaireRES.addAll(mFilteredData)
        } else {
            for (concess in mFilteredData) {
                if (concess.name.lowercase(Locale.getDefault())
                        .contains(filterText.lowercase(Locale.getDefault()))
                ) {
                    concessionaireRES.add(concess)
                }
            }
        }

        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCrudConcessionaireBinding.bind(view)

        fun bind(item: ConcessionaireSE) {

            binding.apply {
                txtConcessionaire.text = item.name

                imgShowMore.animateOnPress()
                imgShowMore.setOnClickListener {

                    mContext.launchActivity<ConcessionaireCrudActivity>(
                        bundleOf("concess" to item)
                    )

                }
            }
        }

    }

}