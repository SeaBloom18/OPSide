package com.ops.opside.flows.sign_on.dashboardModule.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ops.opside.R
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.Preferences
import com.ops.opside.common.utils.SP_NAME
import com.ops.opside.common.utils.SP_USER_URL_PHOTO
import com.ops.opside.common.utils.startActivity
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.databinding.FragmentDashBoardBinding
import com.ops.opside.flows.sign_on.dashboardModule.adapter.TaxCollectionListAdapter
import com.ops.opside.flows.sign_on.dashboardModule.viewModel.TaxCollectionListViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : BaseFragment() {

    private lateinit var mBinding: FragmentDashBoardBinding
    private val mActivity: MainActivity by lazy { activity as MainActivity }
    private lateinit var mTaxCollectionListAdapter: TaxCollectionListAdapter
    private val mTaxCollectionListViewModel: TaxCollectionListViewModel by viewModels()
    private lateinit var mTaxCollectionList: MutableList<TaxCollectionSE>
    @Inject lateinit var preferences: Preferences

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

            tvAnalyticsUserName.text = getString(R.string.dashboard_tv_analytics_name,
                preferences.getString(SP_NAME))

            ibShowAnalytics.setOnClickListener {
                val dialog = BottomSheetAnalytics()
                dialog.show(mActivity.supportFragmentManager,dialog.tag)
            }

            ibShowFilters.setOnClickListener { initBsd() }

            if (preferences.getString(SP_USER_URL_PHOTO)?.isNotEmpty() == true) {
                Glide.with(mActivity)
                    .load(preferences.getString(SP_USER_URL_PHOTO)).circleCrop().into(mBinding.ivProfilePicture)
                mBinding.lavUserProfileAnim.visibility = View.INVISIBLE
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
        mTaxCollectionListViewModel.getShowProgress().observe(mActivity,
            Observer(mActivity::showLoading))
        mTaxCollectionListViewModel.getTaxCollectionList.observe(mActivity,
            Observer(this::getTaxCollectionList))
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
        mTaxCollectionListAdapter = TaxCollectionListAdapter(mTaxCollectionList, mActivity)

        mBinding.rvTaxCollections.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = mTaxCollectionListAdapter
        }
    }

    private fun initBsd() {
        val bottomSheetFilter =
            BottomSheetFilter(showMarket = true, showCollector = true, showDate = true)
        bottomSheetFilter.show(mActivity.supportFragmentManager, bottomSheetFilter.tag)
    }
}