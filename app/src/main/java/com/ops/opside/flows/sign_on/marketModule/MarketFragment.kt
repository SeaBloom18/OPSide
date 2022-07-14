package com.ops.opside.flows.sign_on.marketModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ops.opside.R
import com.ops.opside.databinding.FragmentMarketBinding

class MarketFragment : Fragment() {

    private var mBinding: FragmentMarketBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}