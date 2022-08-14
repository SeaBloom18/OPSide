package com.ops.opside.flows.sign_on.marketModule.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.databinding.FragmentMarketBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View? {
        mBinding = FragmentMarketBinding.inflate(inflater, container, false)
        mActivity = activity as MainActivity

        setToolbar()
        setUpRecyclerView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //Functions
    private fun setToolbar(){
        with(binding.toolbarMarket.commonToolbar) {
            this.title = getString(R.string.bn_menu_market_opc1)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_market_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_market_search -> {
                            //Action
                            true
                        }
                        R.id.menu_market_create -> {
                            startActivity(Intent(activity, MarketRegisterActivity::class.java))
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

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

    private fun confirmMarketDelete(tianguis: TianguisSE){
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

    private fun getMarkets(): MutableList<TianguisSE> {
        val tianguis = mutableListOf<TianguisSE>()
        for (i in 1..5) tianguis.add(TianguisSE(i.toLong(), "Tianguis de muestra $i", "Direccion de muestra $i",
                                                "",0.0,0.0,0))

        return tianguis
    }

    //Interface
    override fun onDeleteMarket(tianguis: TianguisSE) {
        confirmMarketDelete(tianguis)
    }

    override fun onEditMarket(tianguis: TianguisSE) {
        editMarket()
    }

    /*
    * Requisitos de esta vista:
    * onClick: ver detalles del tianguis
    * menu: opciones de eliminar y editar
    * recyclerview item: nombre, direccion
    * */
}