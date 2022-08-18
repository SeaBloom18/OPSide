package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.FragmentConcessionaireBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.ConcessionaireAdapter
import com.ops.opside.flows.sign_on.concessionaireModule.viewModel.ConcessionaireViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.FinalizeTaxCollectionViewModel

class ConcessionaireFragment : Fragment() {

    lateinit var mBinding: FragmentConcessionaireBinding
    lateinit var mAdapter: ConcessionaireAdapter
    lateinit var mActivity: MainActivity
    lateinit var mViewModel: ConcessionaireViewModel
    lateinit var mConcessionaresList: MutableList<ConcessionaireSE>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentConcessionaireBinding.inflate(inflater, container, false)

        mBinding.apply {
            imgFilter.animateOnPress()
            imgFilter.setOnClickListener { initBsd() }
        }

        setUpActivity()
        bindViewModel()
        loadConcessionaresList()
        return mBinding.root
    }

    private fun bindViewModel() {
        mViewModel = ViewModelProvider(this)[ConcessionaireViewModel::class.java]

        mViewModel.getConcessionairesList.observe(requireActivity(), Observer(this::getAbsencesList))
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
        mAdapter = ConcessionaireAdapter(mConcessionaresList)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context)

        mBinding.rvConcessionaires.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }

    private fun getAbsencesList(concessionaresList: MutableList<ConcessionaireSE>){
        mConcessionaresList = concessionaresList

        initRecyclerView()
    }

    private fun loadConcessionaresList(){
        mViewModel.getConcessionairesList()
    }


}