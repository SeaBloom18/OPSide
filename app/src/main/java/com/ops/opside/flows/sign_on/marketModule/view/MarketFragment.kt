package com.ops.opside.flows.sign_on.marketModule.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.databinding.FragmentMarketBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.marketModule.adapters.MarketAdapter
import com.ops.opside.flows.sign_on.marketModule.adapters.OnClickListener
import com.ops.opside.flows.sign_on.marketModule.viewModel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : Fragment(), OnClickListener {

    private lateinit var mBinding: FragmentMarketBinding
    private lateinit var mActivity: MainActivity
    private lateinit var mMarketAdapter: MarketAdapter
    private val mMarketViewModel: MarketViewModel by viewModels()
    private lateinit var mMarketList: MutableList<MarketSE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View {
        mBinding = FragmentMarketBinding.inflate(inflater, container, false)
        mActivity = activity as MainActivity

        setToolbar()
        bindViewModel()
        loadMarketsList()

        return mBinding.root
    }

    /** ViewModel and Methods SetUp **/
    private fun bindViewModel() {
        mMarketViewModel.getMarketList.observe(mActivity, Observer(this::getMarketList))
    }

    private fun loadMarketsList() {
        mMarketViewModel.getMarketList()
    }

    private fun getMarketList(marketList: MutableList<MarketSE>){
        mMarketList = marketList
        setUpRecyclerView()
    }

    /** Toolbar SetUp **/
    private fun setToolbar(){
        with(mBinding.toolbarMarket.commonToolbar) {
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

    /** RecyclerView SetUp **/
    private fun setUpRecyclerView(){
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(mActivity)
        mMarketAdapter = MarketAdapter(mMarketList, this)

        mBinding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mMarketAdapter
        }
    }

    /** Interface Methods and SetUp**/
    private fun confirmMarketDelete(marketId: String){
        val dialog = BaseDialog(
            requireActivity(),
            R.drawable.ic_store,
            getString(R.string.alert_dialog_delete_title),
            getString(R.string.alert_dialog_delete_message),
            getString(R.string.common_delete),
            "",
            {
                mMarketViewModel.deleteMarket(marketId)
                Toast.makeText(activity, R.string.toast_delete_message_success, Toast.LENGTH_SHORT).show()
                loadMarketsList()
            },
            { Toast.makeText(activity, "onCancel()", Toast.LENGTH_SHORT).show() },
        )
        dialog.show()
    }

    override fun onDeleteMarket(marketId: String) {
        confirmMarketDelete(marketId)
    }
}