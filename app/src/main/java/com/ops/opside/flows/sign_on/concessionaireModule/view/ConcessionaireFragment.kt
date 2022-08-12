package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.databinding.FragmentConcessionaireBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.ConcessionaireAdapter
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity

class ConcessionaireFragment : Fragment() {

    lateinit var mBinding: FragmentConcessionaireBinding
    lateinit var mAdapter: ConcessionaireAdapter
    lateinit var mActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentConcessionaireBinding.inflate(inflater, container, false)

        mBinding.apply {
            imgFilter.setOnClickListener { initBsd() }
        }

        initRecyclerView()
        setUpActivity()

        return mBinding.root
    }

    private fun setUpActivity() {
        mActivity = activity as MainActivity
    }

    private fun initBsd() {
        tryOrPrintException {
            val bottomSheetFilter = BottomSheetFilter()
            bottomSheetFilter.show(mActivity.supportFragmentManager, bottomSheetFilter.tag)
        }
    }

    private fun initRecyclerView() {
        val collections = mutableListOf<ConcessionaireSE>()

        for (i in 1..30) {
            collections.add(
                ConcessionaireSE(
                    i.toLong(), "Concesionario $i",
                    "",
                    "",
                    "",
                    "", 3.0, ""
                )
            )
        }

        mAdapter = ConcessionaireAdapter(collections)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context)

        mBinding.rvConcessionaires.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }


}