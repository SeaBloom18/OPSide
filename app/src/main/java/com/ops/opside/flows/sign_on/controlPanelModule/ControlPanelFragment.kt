package com.ops.opside.flows.sign_on.controlPanelModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ops.opside.R
import com.ops.opside.databinding.FragmentControlPanelBinding
import com.ops.opside.databinding.FragmentIncidentsBinding

class ControlPanelFragment : Fragment() {

    private var mBinding: FragmentControlPanelBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentControlPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}