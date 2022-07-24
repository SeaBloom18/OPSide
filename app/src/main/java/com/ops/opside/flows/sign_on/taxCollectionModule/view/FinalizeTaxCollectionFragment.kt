package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.databinding.FragmentFinalizeTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.AbsenceTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence

class FinalizeTaxCollectionFragment : Fragment() {

    lateinit var mBinding: FragmentFinalizeTaxCollectionBinding
    lateinit var mAdapter: AbsenceTaxCollectionAdapter
    private var mActivity: TaxCollectionActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFinalizeTaxCollectionBinding.inflate(inflater, container, false)

        setUpActivity()
        initRecyclerView()

        mBinding.btnCloseFinalize.setOnClickListener {
            closeFragment()
        }

        return mBinding.root
    }

    private fun closeFragment() {
        mActivity?.showButtons()
        mActivity?.onBackPressed()
    }

    private fun setUpActivity() {
        mActivity = activity as? TaxCollectionActivity
        mActivity?.hideButtons()
    }

    private fun initRecyclerView() {
        val absences = mutableListOf<ItemAbsence>()

        absences.add(ItemAbsence("1", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("2", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("3", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("4", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("5", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("6", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("7", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("8", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("9", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("10", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("11", "David Gonzales", "example@gmail.com"))
        absences.add(ItemAbsence("12", "David Gonzales", "example@gmail.com"))

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