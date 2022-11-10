package com.ops.opside.common.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ops.opside.R
import com.ops.opside.databinding.FragmentInDevelopmentBinding

class InDevelopmentFragment  : Fragment() {

    private val mBinding: FragmentInDevelopmentBinding by lazy {
        FragmentInDevelopmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

}