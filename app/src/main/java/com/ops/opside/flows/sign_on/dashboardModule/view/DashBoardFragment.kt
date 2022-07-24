package com.ops.opside.flows.sign_on.dashboardModule.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ops.opside.R
import com.ops.opside.databinding.FragmentDashBoardBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.View.TaxCollectionCrudFragment
import com.ops.opside.flows.sign_on.taxCollectionModule.view.FinalizeTaxCollectionFragment
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

class DashBoardFragment : Fragment() {

    lateinit var mBinding: FragmentDashBoardBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentDashBoardBinding.inflate(inflater, container, false)

        mBinding.fabInitTaxCollection.setOnClickListener {
            val intent = Intent(activity, TaxCollectionActivity::class.java)
            activity!!.startActivity(intent)
        }

        mBinding.fabTaxCollectionCrud.setOnClickListener {
            val fragment = TaxCollectionCrudFragment()

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            fragmentTransaction?.add(R.id.container, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        return mBinding.root
    }



}