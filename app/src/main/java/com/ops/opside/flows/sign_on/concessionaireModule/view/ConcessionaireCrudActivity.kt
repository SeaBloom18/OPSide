package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.adapters.SwipeToDeleteCallback
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.ActivityConcessionaireCrudBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.MarketParticipatingAdapter
import com.ops.opside.flows.sign_on.concessionaireModule.viewModel.ConcessionaireCrudViewModel

class ConcessionaireCrudActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityConcessionaireCrudBinding
    private lateinit var mAdapter: MarketParticipatingAdapter
    private lateinit var mViewModel: ConcessionaireCrudViewModel
    private lateinit var mMarketsList: MutableList<MarketSE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityConcessionaireCrudBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            btnClose.animateOnPress()
            btnClose.setOnClickListener { onBackPressed() }
        }

        bindViewModel()
        loadMarketsList()
    }

    private fun bindViewModel() {
        mViewModel = ViewModelProvider(this)[ConcessionaireCrudViewModel::class.java]

        mViewModel.getMarketsList.observe(this, Observer(this::getMarketsList))
    }

    private fun initRecyclerView() {
        mAdapter = MarketParticipatingAdapter(mMarketsList)

        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)

        mBinding.rvParticipatingMarket.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.markets.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mBinding.rvParticipatingMarket)
    }

    private fun getMarketsList(marketsList: MutableList<MarketSE>){
        mMarketsList = marketsList

        initRecyclerView()
    }

    private fun loadMarketsList(){
        mViewModel.getMarketsList()
    }
}