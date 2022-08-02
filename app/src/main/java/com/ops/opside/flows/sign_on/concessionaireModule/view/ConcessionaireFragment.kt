package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.Entities.Concessionaire
import com.ops.opside.databinding.FragmentConcessionaireBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.ConcessionaireAdapter
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.adapters.TaxCollectionsCrudAdapter
import com.ops.opside.common.Entities.TaxCollectionModel

class ConcessionaireFragment : Fragment() {

    lateinit var mBinding: FragmentConcessionaireBinding
    lateinit var mAdapter: ConcessionaireAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {

        mBinding = FragmentConcessionaireBinding.inflate(inflater, container, false)

        initRecyclerView()

        return mBinding.root
    }

    private fun initRecyclerView() {
        val collections = mutableListOf<Concessionaire>()

        for (i in 1..30) {
            collections.add(
                Concessionaire(
                    "$i", "Concesionario $i",
                    "",
                    "",
                    "",
                    0.0, "", 0, mutableListOf()
                )
            )
        }

        mAdapter = ConcessionaireAdapter(collections)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context)

        mBinding.rvConcessionaires.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }


}