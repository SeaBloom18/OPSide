package com.ops.opside.flows.sign_on.taxCollectionCrudModule.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.FORMAT_SQL_DATE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.utils.startActivity
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityTaxCollectionCrudBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters.TaxCollectionsCrudAdapter
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.interfaces.TaxCollectionCrudAux
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.viewModel.TaxCollectionCrudViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class TaxCollectionCrudActivity : BaseActivity(), TaxCollectionCrudAux {

    private lateinit var mAdapter: TaxCollectionsCrudAdapter

    private val mBinding: ActivityTaxCollectionCrudBinding by lazy {
        ActivityTaxCollectionCrudBinding.inflate(layoutInflater)
    }

    private val mViewModel: TaxCollectionCrudViewModel by viewModels()

    private lateinit var mCollectionList: MutableList<TaxCollectionSE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                startActivity<TaxCollectionActivity>()
            }
        }

        bindViewModel()
        loadCollectionsList()
    }

    private fun bindViewModel() {
        mViewModel.getCollectionsList.observe(this, Observer(this::getCollectionsList))
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))
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

    private fun loadCollectionsList() {
        mViewModel.getCollectionsList()
    }

    private fun getCollectionsList(collectionList: MutableList<TaxCollectionSE>) {
        mCollectionList = collectionList

        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT_SQL_DATE)
        mCollectionList = mCollectionList.sortedByDescending {
            LocalDate.parse(
                it.startDate,
                dateTimeFormatter
            )
        } as MutableList<TaxCollectionSE>

        initRecyclerView()
    }

    override fun hideButtons() {
        mBinding.fabInitTaxCollection.hide()
    }

    override fun showButtons() {
        mBinding.fabInitTaxCollection.show()
    }

}