package com.ops.opside.flows.sign_on.dashboardModule.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.ops.opside.R
import com.ops.opside.databinding.FragmentDashBoardBinding
import com.ops.opside.flows.sign_on.mainModule.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.View.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

class DashBoardFragment : Fragment() {

    private var mBinding: FragmentDashBoardBinding? = null
    private val binding get() = mBinding!!
    private lateinit var mActivity: MainActivity

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim) }
    private var closed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentDashBoardBinding.inflate(inflater, container, false)
        mActivity = activity as MainActivity

        setToolbar()
        binding.fabMenu.setOnClickListener {
            OnAddButtonClick()
        }

        binding.fabInitTaxCollection.setOnClickListener {
            val intent = Intent(activity, TaxCollectionActivity::class.java)
            activity!!.startActivity(intent)
        }

        binding.fabTaxCollectionCrud.setOnClickListener {
            val intent = Intent(activity, TaxCollectionCrudActivity::class.java)
            activity!!.startActivity(intent)
        }

        return binding.root
    }

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
                            startActivity(Intent(activity, ControlPanelActivity::class.java))
                            true
                        }
                        R.id.taxNotification -> {
                            Toast.makeText(mActivity, "Notification", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.taxProfile -> {
                            Toast.makeText(mActivity, "Profile", Toast.LENGTH_SHORT).show()
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun OnAddButtonClick() {
        setVisibility(closed)
        setAnimation(closed)
        closed = !closed;
    }

    // A Function used to set the Animation effect
    private fun setAnimation(closed:Boolean) {
        if(!closed){
            binding.fabTaxCollectionCrud.startAnimation(fromBottom)
            binding.fabInitTaxCollection.startAnimation(fromBottom)
            binding.fabMenu.startAnimation(rotateOpen)
        }else{
            binding.fabTaxCollectionCrud.startAnimation(toBottom)
            binding.fabInitTaxCollection.startAnimation(toBottom)
            binding.fabMenu.startAnimation(rotateClose)
        }
    }
    // used to set visibility to VISIBLE / INVISIBLE
    private fun setVisibility(closed:Boolean) {
        if(!closed)
        {
            binding.fabTaxCollectionCrud.visibility = View.VISIBLE
            binding.fabInitTaxCollection.visibility = View.VISIBLE
        }else{
            binding.fabTaxCollectionCrud.visibility = View.INVISIBLE
            binding.fabInitTaxCollection.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}