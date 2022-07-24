package com.ops.opside.flows.sign_on.taxCollectionCrudModule.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.databinding.FragmentTaxCollectionCrudBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters.TaxCollectionsCrudAdapter
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.dataClasses.TaxCollectionModel

class TaxCollectionCrudFragment : Fragment() {

    lateinit var mBinding: FragmentTaxCollectionCrudBinding
    lateinit var mAdapter: TaxCollectionsCrudAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentTaxCollectionCrudBinding.inflate(inflater, container, false)

        mBinding.imgClose.setOnClickListener {
            val fragment = TaxCollectionCrudFragment()

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            fragmentTransaction?.remove(fragment)?.commit()
        }

        initRecyclerView()

        return mBinding.root
    }

    fun initRecyclerView() {
        val collections = mutableListOf<TaxCollectionModel>()

        for (i in 1..15) {
            collections.add(
                TaxCollectionModel(
                    "$i", "",
                    "Tianguis Minicipal",
                    1250.0,
                    "2022-07-12",
                    "", "", "", mutableListOf()
                )
            )
        }

        mAdapter = TaxCollectionsCrudAdapter(collections)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context)

        mBinding.rvTaxCollections.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

    }

}