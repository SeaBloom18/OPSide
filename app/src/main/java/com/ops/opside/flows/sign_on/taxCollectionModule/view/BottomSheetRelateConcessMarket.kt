package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ops.opside.R
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetCreateIncidentPersonBinding
import com.ops.opside.databinding.BottomSheetRelateConcessMarketBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetRelateConcessMarketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetRelateConcessMarket(
    val concessionaire: ConcessionaireSE,
    val market: MarketSE,
    private val status: (Pair<Boolean, ParticipatingConcessSE>) -> Unit = {}
) : BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetRelateConcessMarketBinding

    private lateinit var participatingConcess: ParticipatingConcessSE
    private val mViewModel: BottomSheetRelateConcessMarketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetRelateConcessMarketBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            ibClose.setOnClickListener { dismiss() }

            etDealerName.setText(concessionaire.name)
            etMarketName.setText(market.name)
            btnRelate.setOnClickListener { relate() }
        }


        bindViewModel()
    }

    private fun bindViewModel() {
        mViewModel.addRelate.observe(
            this,
            Observer(this::isRelated)
        )

        mViewModel.persistParticipatingConcess.observe(
            this,
            Observer(this::isPersisted)
        )
    }

    private fun isRelated(idFirebase: String) {
        participatingConcess.idFirebase = idFirebase
        mViewModel.persistParticipatingConcess(participatingConcess)
        status.invoke(Pair(true, participatingConcess))
        dismiss()
    }

    private fun isPersisted(isPersisted: Boolean) {

    }


    private fun relate() {
        val linearMeters = mBinding.etLinearMeters.text.toString().trim()
        val lineBusiness = mBinding.etLineOfBusiness.text.toString().trim()
        if (linearMeters.isEmpty()) {
            toast("Debes de rellenar los metros lineales!")
        } else if (lineBusiness.isEmpty()) {
            toast("Debes de rellenar el tipo de puesto!")
        } else {
            participatingConcess = ParticipatingConcessSE(
                market.idFirebase,
                concessionaire.idFirebase,
                idFirebase = "",
                linearMeters.toDouble(),
                lineBusiness,
                market.name)
            mViewModel.relateConcessWithMarket(participatingConcess)
        }
    }

}