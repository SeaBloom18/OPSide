package com.ops.opside.flows.sign_on.dashboardModule.view

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.dialogs.InDevelopmentFragment
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.startActivity
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.databinding.FragmentDashBoardBinding
import com.ops.opside.flows.sign_on.dashboardModule.adapter.TaxCollectionListAdapter
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.TaxCollectionListViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.marketModule.adapters.MarketAdapter
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.view.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : BaseFragment() {

    private lateinit var mBinding: FragmentDashBoardBinding
    private val mActivity: MainActivity by lazy { activity as MainActivity }
    private lateinit var mTaxCollectionListAdapter: TaxCollectionListAdapter
    private val mTaxCollectionListViewModel: TaxCollectionListViewModel by viewModels()
    private lateinit var mTaxCollectionList: MutableList<TaxCollectionSE>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        mBinding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {

            fabInitTaxCollection.setOnClickListener {
                mActivity.startActivity<TaxCollectionActivity>()
            }

            /*fabTaxCollectionCrud.setOnClickListener {
                mActivity.startActivity<TaxCollectionCrudActivity>()
            }*/
        }
        /** Call's Methods **/
        setToolbar()
        bindViewModel()
    }

    /** ViewModel and Methods SetUp **/
    private fun bindViewModel() {
        mTaxCollectionListViewModel.getShowProgress().observe(mActivity, Observer(mActivity::showLoading))
        mTaxCollectionListViewModel.getTaxCollectionList.observe(mActivity, Observer(this::getTaxCollectionList))
    }

    private fun getTaxCollectionList(taxCollectionList: MutableList<TaxCollectionSE>) {
        mTaxCollectionList = taxCollectionList
        setUpRecyclerView()
    }

    private fun loadTaxCollectionList() {
        mTaxCollectionListViewModel.getTaxCollectionList()
    }

    /** Toolbar SetUp**/
    private fun setToolbar(){
        with(mBinding.toolbarTaxDashboard.commonToolbar) {
            this.title = getString(R.string.dashboard_analytics)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_tax_collection_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.taxSettings -> {
                            mActivity.startActivity<ControlPanelActivity>()
                            true
                        }
                        /*R.id.taxNotification -> {
                            Toast.makeText(mActivity, "Notification", Toast.LENGTH_SHORT).show()
                            true
                        }*/
                        R.id.taxProfile -> {
                            val dialog = BottomSheetUserProfile()
                            dialog.show(mActivity.supportFragmentManager, dialog.tag)
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
            loadTaxCollectionList()
        }
    }

    private fun setUpRecyclerView() {
        val gridLayoutManager: RecyclerView.LayoutManager
        gridLayoutManager = GridLayoutManager(mActivity, 2)
        mTaxCollectionListAdapter = TaxCollectionListAdapter(mTaxCollectionList)

        mBinding.rvTaxCollections.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = mTaxCollectionListAdapter
        }
    }
}