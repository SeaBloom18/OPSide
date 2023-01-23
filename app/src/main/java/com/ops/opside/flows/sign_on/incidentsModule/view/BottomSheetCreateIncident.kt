package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ops.opside.common.entities.firestore.IncidentFE
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.utils.*
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
    private val mIncidentFE: IncidentFE = IncidentFE()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetCreateIncidentBinding.inflate(layoutInflater)
        mBinding.apply {
            mBinding.btnCreateIncident.setOnClickListener { insertIncident() }
        }
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

    private fun insertIncident() {
        with(mIncidentFE) {
            val teIncidentName = mBinding.teIncidentName.text.toString().trim()
            val teIncidentPrice = mBinding.teIncidentPrice.text.toString().trim()
            val teIncidentDescription = mBinding.teIncidentDescription.text.toString().trim()
            if (teIncidentName.isNotEmpty()) {
                incidentName = teIncidentName
                incidentPrice = teIncidentPrice
                incidentDescription = teIncidentDescription
                mBottomSheetCreateIncidentViewModel.insertIncident(mIncidentFE)
            } else {
                toast("debes de llenar todos los valorea")
            }
        }
    }
}