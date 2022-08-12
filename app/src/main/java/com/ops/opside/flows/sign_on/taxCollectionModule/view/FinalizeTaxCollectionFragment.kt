package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.databinding.FragmentFinalizeTaxCollectionBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.view.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.AbsenceTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence

class FinalizeTaxCollectionFragment : Fragment() {

    lateinit var mBinding: FragmentFinalizeTaxCollectionBinding
    lateinit var mAdapter: AbsenceTaxCollectionAdapter
    private lateinit var mTypeTransaction: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFinalizeTaxCollectionBinding.inflate(inflater, container, false)

        mBinding.apply {

            btnCloseFinalize.setOnClickListener {
                closeFragment()
            }

            cbAgreeDeclaration.setOnClickListener {
                btnSend.isEnabled = cbAgreeDeclaration.isChecked
            }

            btnSend.setOnClickListener {
                val intent = Intent(activity, MainActivity::class.java)
                activity!!.startActivity(intent)
            }

            tryOrPrintException {
                val bundle = arguments

                bundle?.let {
                    mTypeTransaction = bundle.getString("type")!!

                    if (mTypeTransaction == "update") {
                        txtTitle.text = getString(R.string.tax_collection_update_title)
                        etTotalAmount.isEnabled = true
                    }
                }
            }

        }

        setUpActivity()
        initRecyclerView()

        return mBinding.root
    }


    private fun closeFragment() {
        if (mTypeTransaction == "create") {
            val activity = activity as? TaxCollectionActivity
            activity?.showButtons()
            activity?.onBackPressed()
        } else {
            val activity = activity as? TaxCollectionCrudActivity
            activity?.showButtons()
            activity?.onBackPressed()
        }
    }

    private fun setUpActivity() {
        if (mTypeTransaction == "create")
            (activity as? TaxCollectionActivity)?.hideButtons()
        else
            (activity as? TaxCollectionCrudActivity)?.hideButtons()
    }

    private fun initRecyclerView() {
        val absences = mutableListOf<ItemAbsence>()

        for (i in 1..15){
            absences.add(ItemAbsence("$i", "David Gonzales", "example@gmail.com"))
        }

        mAdapter = AbsenceTaxCollectionAdapter(absences)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context!!)

        mBinding.rvAbsence.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }

}