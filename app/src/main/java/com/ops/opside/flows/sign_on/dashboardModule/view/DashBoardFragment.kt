package com.ops.opside.flows.sign_on.dashboardModule.view

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.ops.opside.R
import com.ops.opside.common.dialogs.InDevelopmentFragment
import com.ops.opside.common.utils.startActivity
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.databinding.FragmentDashBoardBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.view.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardFragment : BaseFragment() {

    private var mBinding: FragmentDashBoardBinding? = null
    private val binding get() = mBinding!!
    private lateinit var mActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        mBinding = FragmentDashBoardBinding.inflate(inflater, container, false)
        mActivity = activity as MainActivity

        setToolbar()
        binding.apply {

            fabInitTaxCollection.setOnClickListener {
                mActivity.startActivity<TaxCollectionActivity>()
            }

            /*fabTaxCollectionCrud.setOnClickListener {
                mActivity.startActivity<TaxCollectionCrudActivity>()
            }*/
        }

        //mActivity.supportFragmentManager.beginTransaction().add(R.id.fragment_container, InDevelopmentFragment()).commit()

        return binding.root
    }

    /** Toolbar SetUp**/
    private fun setToolbar(){
        with(binding.toolbarTaxDashboard.commonToolbar) {
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
        }
    }

    /** Override Methods **/
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}