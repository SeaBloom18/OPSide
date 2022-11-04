package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.adapters.SwipeToDeleteCallback
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.databinding.BottomSheetRecordTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ADDED
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.RecordTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.interfaces.TaxCollectionAux
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetRecordTaxCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetRecordTaxCollection(val idTaxCollection: String) : BottomSheetDialogFragment() {

    private lateinit var mAdapter: RecordTaxCollectionAdapter
    private lateinit var mBinding: BottomSheetRecordTaxCollectionBinding
    private var mListener: TaxCollectionAux? = null

    private val mActivity: TaxCollectionActivity by lazy {
        activity as TaxCollectionActivity
    }

    private val mViewModel: BottomSheetRecordTaxCollectionViewModel by viewModels()

    private lateinit var mEventsList: MutableList<EventRE>
    private var tempDeletedEvent: Pair<Int,EventRE?> = Pair(0,null)


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

        mBinding.apply {
            btnClose.setOnClickListener { dismiss() }
        }

        bindViewModel()
        loadEventsList()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as? TaxCollectionAux
    }


    private fun loadEventsList() {
        mViewModel.getEventsList(idTaxCollection)
    }

    private fun bindViewModel() {
        mViewModel.getEventsList.observe(this, Observer(this::getEventsList))
        mViewModel.wasEventDeleted.observe(this, Observer(this::wasEventDeleted))
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
                tempDeletedEvent = Pair(
                    viewHolder.adapterPosition,
                    mAdapter.events[viewHolder.adapterPosition]
                )

                mViewModel.deleteEvent(mAdapter.events[viewHolder.adapterPosition])

                mAdapter.events.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mBinding.rvRecord)
    }

    private fun getEventsList(eventsList: MutableList<EventRE>) {
        if (eventsList.isEmpty()){
            mBinding.txtNoHasEvents.isGone = false
            return
        }

        mEventsList = eventsList
        initRecyclerView()
    }

    private fun wasEventDeleted(itWas: Boolean){
        if (itWas){
            mBinding.txtNoHasEvents.isGone = mEventsList.isNotEmpty()

            tempDeletedEvent.second?.let { mListener?.revertEvent(it) }
        }
    }

}