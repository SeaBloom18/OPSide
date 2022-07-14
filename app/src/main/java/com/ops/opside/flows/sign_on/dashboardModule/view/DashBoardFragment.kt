package com.ops.opside.flows.sign_on.dashboardModule.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ops.opside.databinding.FragmentDashBoardBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity

class DashBoardFragment : Fragment() {

    private var mBinding: FragmentDashBoardBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDashBoardBinding.inflate(inflater, container, false)

        mBinding!!.fabInitTaxCollection.setOnClickListener {
            val intent = Intent (activity, TaxCollectionActivity::class.java)
            activity!!.startActivity(intent)
        }

        setUpPieChart()
        return binding.root
    }

    private fun setUpPieChart(){}

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}