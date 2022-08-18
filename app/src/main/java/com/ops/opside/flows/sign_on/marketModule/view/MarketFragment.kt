package com.ops.opside.flows.sign_on.marketModule.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.MarketSE
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

    private lateinit var mMarketAdapter: MarketAdapter

    private lateinit var mMarketViewModel: MarketViewModel
    private lateinit var mMarketList: MutableList<MarketSE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View? {
        mBinding = FragmentMarketBinding.inflate(inflater, container, false)
        mActivity = activity as MainActivity

        setToolbar()
        bindViewModel()
        loadMarketsList()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun loadMarketsList() {
        mMarketViewModel.getMarketList()
    }

    private fun bindViewModel() {
        mMarketViewModel = ViewModelProvider(requireActivity())[MarketViewModel::class.java]
        mMarketViewModel.getMarketList.observe(mActivity, Observer(this::getMarketList))
    }

    private fun getMarketList(marketList: MutableList<MarketSE>){
        mMarketList = marketList
        setUpRecyclerView()
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

    private fun setUpRecyclerView(){
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(mActivity)
        mMarketAdapter = MarketAdapter(mMarketList, this)

        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mMarketAdapter
        }
    }

    private fun confirmMarketDelete(market: MarketSE){
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
        startActivity(Intent(mActivity, MarketRegisterActivity::class.java))
        Toast.makeText(mActivity, "Editar item", Toast.LENGTH_SHORT).show()
    }

    //Interface
    override fun onDeleteMarket(market: MarketSE) {
        confirmMarketDelete(market)
    }

    override fun onEditMarket(market: MarketSE) {
        editMarket()
    }

    /*
    * Requisitos de esta vista:
    * onClick: ver detalles del tianguis
    * menu: opciones de eliminar y editar
    * recyclerview item: nombre, direccion
    * */
}