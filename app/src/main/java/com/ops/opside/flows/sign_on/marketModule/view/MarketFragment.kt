package com.ops.opside.flows.sign_on.marketModule.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.databinding.FragmentMarketBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.marketModule.actions.MarketAction
import com.ops.opside.flows.sign_on.marketModule.adapters.MarketAdapter
import com.ops.opside.flows.sign_on.marketModule.interfaces.OnClickListener
import com.ops.opside.flows.sign_on.marketModule.viewModel.MarketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : BaseFragment(), OnClickListener {

    private lateinit var mBinding: FragmentMarketBinding
    private lateinit var mMarketAdapter: MarketAdapter
    private val mMarketViewModel: MarketViewModel by viewModels()
    private lateinit var mMarketList: MutableList<MarketSE>
    private val mActivity: MainActivity by lazy { activity as MainActivity }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View {
        mBinding = FragmentMarketBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        bindViewModel()
        //loadMarketsList()
    }

    /** ViewModel and Methods SetUp **/
    private fun bindViewModel() {
        mMarketViewModel.getShowProgress().observe(mActivity, Observer(mActivity::showLoading))
        mMarketViewModel.getMarketList.observe(mActivity, Observer(this::getMarketList))
        mMarketViewModel.getAction().observe(mActivity, Observer(this::handleAction))
    }

    private fun loadMarketsList() {
        mMarketViewModel.getMarketList()
    }

    private fun getMarketList(marketList: MutableList<MarketSE>){
        mMarketList = marketList
        setUpRecyclerView()
    }

    /** Sealed Class handleAction**/
    private fun handleAction(action: MarketAction) {
        when(action) {
            is MarketAction.ShowMessageSuccess -> {
                toast(getString(R.string.registration_updated_market_success))
            }
            is MarketAction.ShowMessageError -> {
                toast(getString(R.string.registration_updated_market_error))
            }
        }
    }

    /** Toolbar SetUp **/
    private fun setToolbar(){
        with(mBinding.toolbarFrgMarket.commonToolbar) {
            this.title = getString(R.string.bn_menu_market_opc1)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_market_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_market_create -> {
                            startActivity(Intent(activity, MarketRegisterActivity::class.java))
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
            loadMarketsList()
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
    private fun confirmMarketDelete(marketId: String, position: Int){
        val dialog = BaseDialog(
            requireActivity(),
            R.drawable.ic_ops_delete,
            getString(R.string.alert_dialog_delete_title),
            getString(R.string.alert_dialog_delete_message),
            getString(R.string.common_delete),
            "",
            {
                mMarketViewModel.deleteMarket(marketId)
                mMarketAdapter.updateListPostDelete(position)
                Toast.makeText(activity, R.string.toast_delete_message_success, Toast.LENGTH_SHORT).show()
            },
            { Toast.makeText(activity, "onCancel()", Toast.LENGTH_SHORT).show() },
        )
        dialog.show()
    }

    override fun onDeleteMarket(marketId: String, position: Int) {
        confirmMarketDelete(marketId, position)
    }

    override fun onResume() {
        loadMarketsList()
        super.onResume()
    }
}