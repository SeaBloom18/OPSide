package com.ops.opside.flows.sign_on.dashboardModule.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.ops.opside.R
import com.ops.opside.databinding.FragmentDashBoardBinding
import com.ops.opside.databinding.FragmentMarketBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.View.TaxCollectionCrudFragment
import com.ops.opside.flows.sign_on.taxCollectionModule.view.FinalizeTaxCollectionFragment
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

class DashBoardFragment : Fragment() {

    private var mBinding: FragmentDashBoardBinding? = null
    private val binding get() = mBinding!!

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim) }
    private var closed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentDashBoardBinding.inflate(inflater, container, false)

        binding.fabMenu.setOnClickListener {
            OnAddButtonClick()
        }

        binding.fabInitTaxCollection.setOnClickListener {
            val intent = Intent(activity, TaxCollectionActivity::class.java)
            activity!!.startActivity(intent)
        }

        binding.fabTaxCollectionCrud.setOnClickListener {
            val fragment = TaxCollectionCrudFragment()

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            fragmentTransaction?.add(R.id.container, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        binding.imgSettings.setOnClickListener { startActivity(Intent(activity, ControlPanelActivity::class.java)) }

        return binding.root
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