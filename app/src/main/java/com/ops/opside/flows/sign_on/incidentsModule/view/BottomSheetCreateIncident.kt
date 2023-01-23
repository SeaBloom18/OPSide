package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetCreateIncidentBinding
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.BottomSheetCreateIncidentViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by davidgonzalez on 23/01/23
 */
@AndroidEntryPoint
class BottomSheetCreateIncident : BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetCreateIncidentBinding
    private lateinit var mActivity: MainActivity
    private val mBottomSheetCreateIncidentViewModel: BottomSheetCreateIncidentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetCreateIncidentBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActivity()
        bindViewModel()
    }

    /** ViewModel setUp **/

    private fun bindViewModel() {

    }

    private fun setUpActivity() {
        mActivity = activity as MainActivity
    }
}