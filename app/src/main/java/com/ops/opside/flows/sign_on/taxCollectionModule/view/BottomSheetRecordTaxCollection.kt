package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.adapterCallback.SwipeToDeleteCallback
import com.ops.opside.databinding.BottomSheetRecordTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.ADDED
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.FLOOR_COLLECTION
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.PENALTY_FEE
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.RecordTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemRecord

class BottomSheetRecordTaxCollection : BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetRecordTaxCollectionBinding
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

        populateEvents()
        initRecyclerView()
    }

    private fun populateEvents() {
        mEventsList = getEvents()
        mAdapter = RecordTaxCollectionAdapter(mEventsList)
    }

    private fun initRecyclerView() {
        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context)

        mBinding.rvRecord.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(context!!) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.events.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mBinding.rvRecord)
    }

    private fun getEvents(): MutableList<ItemRecord> {
        val events = mutableListOf<ItemRecord>()

        events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:25 PM", 25.0))
        events.add(ItemRecord(FLOOR_COLLECTION, "Citlaly García Razo", "04:27 PM", 54.5))
        events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:29 PM", 34.0))
        events.add(ItemRecord(ADDED, "Julio Zepeda", "04:30 PM"))
        events.add(ItemRecord(FLOOR_COLLECTION, "Julio Zepeda", "04:31 PM", 34.0))
        events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:32 PM", 25.0))
        events.add(ItemRecord(PENALTY_FEE, "Citlaly García Razo", "04:33 PM"))
        events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:35 PM", 34.0))
        events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:25 PM", 25.0))
        events.add(ItemRecord(FLOOR_COLLECTION, "Citlaly García Razo", "04:27 PM", 54.5))
        events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:29 PM", 34.0))
        events.add(ItemRecord(ADDED, "Julio Zepeda", "04:30 PM"))
        events.add(ItemRecord(FLOOR_COLLECTION, "Julio Zepeda", "04:31 PM", 34.0))
        events.add(ItemRecord(FLOOR_COLLECTION, "Mario Armando Razo", "04:32 PM", 25.0))
        events.add(ItemRecord(PENALTY_FEE, "Citlaly García Razo", "04:33 PM"))
        events.add(ItemRecord(FLOOR_COLLECTION, "David Quezada", "04:35 PM", 34.0))

        return events
    }

}