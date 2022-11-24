package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.entities.share.IncidentSE
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetCreateIncidentBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity

class BottomSheetCreateMarket(private val incident: (IncidentSE) -> Unit = {}): BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetCreateIncidentBinding
    private lateinit var mActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetCreateIncidentBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            btnCreateIncident.setOnClickListener {
                Toast.makeText(activity, "Incident created", Toast.LENGTH_SHORT).show()
            }
        }

        setUpActivity()

    }

    private fun setUpActivity() {
        mActivity = activity as MainActivity
    }
}