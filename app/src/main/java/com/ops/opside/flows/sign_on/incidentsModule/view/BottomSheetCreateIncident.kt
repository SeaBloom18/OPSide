package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.IncidentFE
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.common.utils.*
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetCreateIncidentBinding
import com.ops.opside.flows.sign_on.incidentsModule.actions.CreateIncidentAction
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.BottomSheetCreateIncidentViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by davidgonzalez on 23/01/23
 */
@AndroidEntryPoint
class BottomSheetCreateIncident : BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetCreateIncidentBinding
    private val mActivity: MainActivity by lazy { activity as MainActivity }
    private val mBottomSheetCreateIncidentViewModel: BottomSheetCreateIncidentViewModel by viewModels()
    private val mIncidentFE: IncidentFE = IncidentFE()

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

        mBinding.apply {
            mBinding.btnCreateIncident.setOnClickListener { insertIncident() }
            mBinding.btnDismiss.setOnClickListener { dismiss() }
        }

        /** Methods call's **/
        bindViewModel()
    }

    /** ViewModel setUp **/
    private fun bindViewModel() {
        mBottomSheetCreateIncidentViewModel.getShowProgress().observe(mActivity,
            Observer(mActivity::showLoading))
        mBottomSheetCreateIncidentViewModel.getAction().observe(mActivity,
            Observer(this::handleAction))
    }

    private fun handleAction(action: CreateIncidentAction) {
        when(action) {
            is CreateIncidentAction.ShowMessageSuccess -> {
                toast(getString(R.string.bs_create_incident_handle_success))
            }
            is CreateIncidentAction.ShowMessageError -> {
                toast(getString(R.string.bs_create_incident_handle_error))
            }
        }
    }

    /** Methods **/
    private fun insertIncident() {
        with(mIncidentFE) {
            val teIncidentName = mBinding.teIncidentName.text.toString().trim()
            val teIncidentPrice = mBinding.teIncidentPrice.text.toString().trim()
            val teIncidentDescription = mBinding.teIncidentDescription.text.toString().trim()
            if (teIncidentName.isEmpty()) {
                mBinding.tilIncidentName.error = "The name is necessary to complete the incident"
            } else if (teIncidentPrice.isEmpty()) {
                toast(getString(R.string.common_toast_fill_text))
            } else if (teIncidentDescription.isEmpty()) {
                toast(getString(R.string.common_toast_fill_text))
            } else {
                incidentName = teIncidentName
                incidentPrice = teIncidentPrice
                incidentDescription = teIncidentDescription
                mBottomSheetCreateIncidentViewModel.insertIncident(mIncidentFE)
            }
        }
    }
}