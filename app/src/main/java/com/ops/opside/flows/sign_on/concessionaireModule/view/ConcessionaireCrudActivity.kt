package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.adapters.SwipeToDeleteCallback
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityConcessionaireCrudBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.MarketParticipatingAdapter
import com.ops.opside.flows.sign_on.concessionaireModule.viewModel.ConcessionaireCrudViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConcessionaireCrudActivity : BaseActivity() {

    private lateinit var mAdapter: MarketParticipatingAdapter

    private val mBinding: ActivityConcessionaireCrudBinding by lazy {
        ActivityConcessionaireCrudBinding.inflate(layoutInflater)
    }

    private val mViewModel: ConcessionaireCrudViewModel by viewModels()

    private lateinit var mMarketsList: MutableList<MarketSE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.apply {
            //btnClose.animateOnPress()
            //btnClose.setOnClickListener { onBackPressed() }
        }

        bindViewModel()
        loadMarketsList()
    }

    private fun bindViewModel() {
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
        /*itemTouchHelper.attachToRecyclerView(mBinding.rvParticipatingMarket)*/
    }

    private fun getMarketsList(marketsList: MutableList<MarketSE>){
        mMarketsList = marketsList

        initRecyclerView()
    }

    private fun loadMarketsList(){
        mViewModel.getMarketsList()
    }
}