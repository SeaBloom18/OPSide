package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.databinding.BottomSheetSeeIncidentsBinding
import com.ops.opside.flows.sign_on.incidentsModule.adapter.ListIncidentsAdapter
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.BottomSheetIncidentsListViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord

class BottomSheetIncidentsList: BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetSeeIncidentsBinding
    private lateinit var mActivity: MainActivity
    private lateinit var mListIncidentAdapter: ListIncidentsAdapter
    private lateinit var mViewModel: BottomSheetIncidentsListViewModel
    private lateinit var mIncidentList: MutableList<IncidentSE>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = BottomSheetSeeIncidentsBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActivity()
        bindViewModel()
        loadIncidentsList()
    }

    private fun loadIncidentsList() {
        mViewModel.getIncidentsList()
    }

    private fun bindViewModel() {
        mViewModel = ViewModelProvider(requireActivity())[BottomSheetIncidentsListViewModel::class.java]
        mViewModel.getIncidentsList.observe(this, Observer(this::getIncidentsList))
    }

    private fun getIncidentsList(incidentsList: MutableList<IncidentSE>){
        mIncidentList = incidentsList
        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(mActivity)

        mListIncidentAdapter = ListIncidentsAdapter(mIncidentList)

        mBinding.recyclerIncident.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mListIncidentAdapter
        }
    }

    private fun setUpActivity() {
        mActivity = activity as MainActivity
    }
}