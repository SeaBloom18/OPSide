package com.ops.opside.flows.sign_on.marketModule.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.common.Entities.Market
import com.ops.opside.databinding.FragmentMarketBinding
import com.ops.opside.flows.sign_on.marketModule.adapters.MarketAdapter

class MarketFragment : Fragment() {

    private var mBinding: FragmentMarketBinding? = null
    private val binding get() = mBinding!!

    private lateinit var marketAdapter: MarketAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMarketBinding.inflate(inflater, container, false)

        binding.fabAddMarket.setOnClickListener {
            startActivity(Intent(activity, MarketRegisterActivity::class.java))
        }

        marketAdapter = MarketAdapter(getMarkets())
        linearLayoutManager = LinearLayoutManager(context)

        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = marketAdapter
        }



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun getMarkets(): MutableList<Market>{
        val markets = mutableListOf<Market>()

        val tianguis1 = Market(1, "Tianguis de muestra 1", "Direccion de muestra 1")
        val tianguis2 = Market(2, "Tianguis de muestra 2", "Direccion de muestra 2")
        val tianguis3 = Market(3, "Tianguis de muestra 3", "Direccion de muestra 3")

        markets.add(tianguis1)
        markets.add(tianguis2)
        markets.add(tianguis3)

        return markets
    }

    /*
    * Requisitos de esta vista:
    * onClick: ver detalles del tianguis
    * menu: opciones de eliminar y editar
    * recyclerview item: nombre, direccion
    * */
}