package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.adapters.SwipeToDeleteCallback
import com.ops.opside.databinding.BottomSheetRecordTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.RecordTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetRecordTaxCollectionViewModel

class BottomSheetRecordTaxCollection : BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetRecordTaxCollectionBinding
    private lateinit var mViewModel: BottomSheetRecordTaxCollectionViewModel
    private lateinit var mActivity: TaxCollectionActivity
    private lateinit var mEventsList: MutableList<ItemRecord>
    private lateinit var mAdapter: RecordTaxCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetRecordTaxCollectionBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActivity()
        bindViewModel()
        loadEventsList()
    }

    private fun setUpActivity() {
        mActivity = activity as TaxCollectionActivity
    }

    private fun loadEventsList(){
        mViewModel.getEventsList()
    }

    private fun bindViewModel() {
        mViewModel = ViewModelProvider(requireActivity())[BottomSheetRecordTaxCollectionViewModel::class.java]
        mViewModel.getEventsList.observe(this, Observer(this::getEventsList))
    }

    private fun initRecyclerView() {
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(mActivity)

        mAdapter = RecordTaxCollectionAdapter(mEventsList)

        mBinding.rvRecord.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(mActivity) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.events.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mBinding.rvRecord)
    }

    private fun getEventsList(eventsList: MutableList<ItemRecord>){
        mEventsList = eventsList
        initRecyclerView()
    }

}