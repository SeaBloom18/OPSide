package com.ops.opside.flows.sign_on.taxCollectionCrudModule.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.databinding.ActivityTaxCollectionCrudBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters.TaxCollectionsCrudAdapter
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.interfaces.TaxCollectionCrudAux
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

class TaxCollectionCrudActivity : AppCompatActivity(), TaxCollectionCrudAux {

    lateinit var mBinding: ActivityTaxCollectionCrudBinding
    lateinit var mAdapter: TaxCollectionsCrudAdapter

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

        initRecyclerView()
    }

    private fun initBsd() {
        val bottomSheetFilter = BottomSheetFilter()
        bottomSheetFilter.show(supportFragmentManager, bottomSheetFilter.tag)
    }


    fun initRecyclerView() {
        val collections = mutableListOf<TaxCollectionSE>()

        for (i in 1..15) {
            collections.add(
                TaxCollectionSE(
                    i.toLong(), "",
                    "Tianguis Minicipal",
                    "",1250.0,
                    "2022-07-12",
                    "", "", ""
                )
            )
        }

        mAdapter = TaxCollectionsCrudAdapter(collections, this, this)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)

        mBinding.rvTaxCollections.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

    }

    override fun hideButtons() {
        mBinding.fabInitTaxCollection.hide()
    }

    override fun showButtons() {
        mBinding.fabInitTaxCollection.show()
    }

}