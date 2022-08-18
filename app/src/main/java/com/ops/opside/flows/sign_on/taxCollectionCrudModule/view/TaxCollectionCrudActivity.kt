package com.ops.opside.flows.sign_on.taxCollectionCrudModule.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.databinding.ActivityTaxCollectionCrudBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters.TaxCollectionsCrudAdapter
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.interfaces.TaxCollectionCrudAux
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.viewModel.TaxCollectionCrudViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

class TaxCollectionCrudActivity : AppCompatActivity(), TaxCollectionCrudAux {

    lateinit var mBinding: ActivityTaxCollectionCrudBinding
    lateinit var mAdapter: TaxCollectionsCrudAdapter
    lateinit var mViewModel: TaxCollectionCrudViewModel
    lateinit var mCollectionList: MutableList<TaxCollectionSE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTaxCollectionCrudBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.apply {
            imgClose.animateOnPress()
            imgClose.setOnClickListener {
                onBackPressed()
            }

            imgFilter.animateOnPress()
            imgFilter.setOnClickListener {
                initBsd()
            }

            fabInitTaxCollection.animateOnPress()
            fabInitTaxCollection.setOnClickListener {
                launchActivity(TaxCollectionActivity(), this@TaxCollectionCrudActivity)
            }
        }

        bindViewModel()
        loadCollectionsList()
    }

    private fun bindViewModel() {
        mViewModel = ViewModelProvider(this)[TaxCollectionCrudViewModel::class.java]

        mViewModel.getCollectionsList.observe(this, Observer(this::getCollectionsList))
    }

    private fun initBsd() {
        val bottomSheetFilter = BottomSheetFilter()
        bottomSheetFilter.show(supportFragmentManager, bottomSheetFilter.tag)
    }


    private fun initRecyclerView() {
        mAdapter = TaxCollectionsCrudAdapter(mCollectionList, this, this)

        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)

        mBinding.rvTaxCollections.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

    }

    private fun loadCollectionsList(){
        mViewModel.getCollectionsList()
    }

    private fun getCollectionsList(collectionList: MutableList<TaxCollectionSE>) {
        mCollectionList = collectionList
        initRecyclerView()
    }

    override fun hideButtons() {
        mBinding.fabInitTaxCollection.hide()
    }

    override fun showButtons() {
        mBinding.fabInitTaxCollection.show()
    }

}