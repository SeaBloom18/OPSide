package com.ops.opside.flows.sign_on.marketModule.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.Market
import com.ops.opside.databinding.FragmentMarketBinding
import com.ops.opside.flows.sign_on.mainModule.MainActivity
import com.ops.opside.flows.sign_on.marketModule.adapters.MarketAdapter
import com.ops.opside.flows.sign_on.marketModule.adapters.OnClickListener
import com.ops.opside.flows.sign_on.marketModule.viewModel.MarketViewModel


class MarketFragment : Fragment(), OnClickListener {

    private var mBinding: FragmentMarketBinding? = null
    private val binding get() = mBinding!!
    private lateinit var mActivity: MainActivity

    private lateinit var marketAdapter: MarketAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var mMarketViewModel: MarketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMarketBinding.inflate(inflater, container, false)

        mActivity = activity as MainActivity

        binding.fabAddMarket.setOnClickListener {
            startActivity(Intent(activity, MarketRegisterActivity::class.java))
        }

        //setUpViewModel() // aun sin funcionar pero ya listo para cuando este la db
        setUpRecyclerView()
        setToolbar()

        return binding.root
    }

    //Override Methods
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


    //Methods
    private fun setToolbar(){
        with(binding.toolbarMarket.commonToolbar) {
            this.title = getString(R.string.bn_menu_market_opc1)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_market_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.search -> {
                            //Action
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun setUpViewModel(){
        mMarketViewModel = ViewModelProvider(requireActivity())[MarketViewModel::class.java]
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

    private fun getMarkets(): MutableList<Market> {
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