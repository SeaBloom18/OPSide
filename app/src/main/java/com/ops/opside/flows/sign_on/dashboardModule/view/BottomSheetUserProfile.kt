package com.ops.opside.flows.sign_on.dashboardModule.view

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.utils.SP_NAME
import com.ops.opside.databinding.BottomSheetUserProfileBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.profileModule.model.profileInteractor
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by David Alejandro González Quezada on 28/10/22.
 */
@AndroidEntryPoint
class BottomSheetUserProfile : BottomSheetDialogFragment(){

    val sharedPref = activity?.getPreferences(MODE_PRIVATE)

    private val mBinding: BottomSheetUserProfileBinding by lazy {
        BottomSheetUserProfileBinding.inflate(layoutInflater)
    }

    private val mActivity: MainActivity by lazy {
        activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            //tvUserName.text = sharedPref?.getString(SP_NAME)
        }
    }
}