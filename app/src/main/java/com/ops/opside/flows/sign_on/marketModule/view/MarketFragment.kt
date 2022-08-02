package com.ops.opside.flows.sign_on.marketModule.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ops.opside.R
import com.ops.opside.common.Entities.Market
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.databinding.FragmentMarketBinding
import com.ops.opside.flows.sign_on.marketModule.adapters.MarketAdapter
import com.ops.opside.flows.sign_on.marketModule.adapters.OnClickListener
import com.ops.opside.flows.sign_on.marketModule.marketModel.MarketInteractor
import com.ops.opside.flows.sign_on.marketModule.viewModel.MarketViewModel

class MarketFragment : Fragment(), OnClickListener {

    private var mBinding: FragmentMarketBinding? = null
    private val binding get() = mBinding!!

    private lateinit var marketAdapter: MarketAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    private lateinit var mMarketViewModel: MarketViewModel

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

        //setUpViewModel() // aun din funcionar pero ya listo para cuando este la db
        setUpRecyclerView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //Functions
    private fun setUpViewModel(){
        mMarketViewModel = ViewModelProvider(requireActivity()).get(MarketViewModel::class.java)
        mMarketViewModel.getMarkets().observe(requireActivity()){
            marketAdapter.setStores(it)
        }
    }

    private fun setUpRecyclerView(){
        marketAdapter = MarketAdapter(getMarkets(), this)
        linearLayoutManager = LinearLayoutManager(context)

        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = marketAdapter
        }
    }

    private fun confirmMarketDelete(market: Market){
        val dialog = BaseDialog(
            requireActivity(),
            getString(R.string.alert_dialog_delete_title),
            getString(R.string.alert_dialog_delete_message),
            getString(R.string.common_delete),
            "",
            { Toast.makeText(activity, R.string.toast_delete_message_success, Toast.LENGTH_SHORT).show() },
            { Toast.makeText(activity, "onCancel()", Toast.LENGTH_SHORT).show() },
        )
        dialog.show()
    }

    private fun editMarket(){
        //startActivity(Intent(context, MarketRegisterActivity::class.java))
        Toast.makeText(context, "Editar item", Toast.LENGTH_SHORT).show()
    }

    fun getMarkets(): MutableList<Market> {
        val markets = mutableListOf<Market>()

        val tianguis1 = Market(1, "Tianguis de muestra 1", "Direccion de muestra 1")
        val tianguis2 = Market(2, "Tianguis de muestra 2", "Direccion de muestra 2")
        val tianguis3 = Market(3, "Tianguis de muestra 3", "Direccion de muestra 3")
        val tianguis4 = Market(4, "Tianguis de muestra 4", "Direccion de muestra 4")
        val tianguis5 = Market(5, "Tianguis de muestra 5", "Direccion de muestra 5")

        markets.add(tianguis1)
        markets.add(tianguis2)
        markets.add(tianguis3)
        markets.add(tianguis4)
        markets.add(tianguis5)

        return markets
    }

    //Interface
    override fun onDeleteMarket(market: Market) {
        confirmMarketDelete(market)
    }

    override fun onEditMarket(market: Market) {
        editMarket()
    }

    /*
    * Requisitos de esta vista:
    * onClick: ver detalles del tianguis
    * menu: opciones de eliminar y editar
    * recyclerview item: nombre, direccion
    * */
}