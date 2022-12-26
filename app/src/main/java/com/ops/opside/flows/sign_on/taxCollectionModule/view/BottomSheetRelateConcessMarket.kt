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
import com.ops.opside.databinding.BottomSheetRelateConcessMarketBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetRelateConcessMarketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetRelateConcessMarket(
    val concessionaire: ConcessionaireSE,
    val market: MarketSE,
    private val status: (Pair<Boolean, ParticipatingConcessSE>) -> Unit = {}
) : BaseBottomSheetFragment() {

    private val mBinding: BottomSheetRelateConcessMarketBinding by lazy {
        BottomSheetRelateConcessMarketBinding.inflate(layoutInflater)
    }

    private lateinit var participatingConcess: ParticipatingConcessSE

    private val mViewModel: BottomSheetRelateConcessMarketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            ibClose.setOnClickListener { dismiss() }

            etDealerName.setText(concessionaire.name)
            etMarketName.setText(market.name)
            btnRelate.setOnClickListener {
                relate(etLinearMeters.text.toString().toDouble(), etLineOfBusiness.text.toString())
            }
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


    private fun relate(linearMeters: Double, lineBusiness: String) {
        if (linearMeters <= 0) {
            toast(getString(R.string.bottom_sheet_relateconcessmarket_toast_linear_meter_validation))
            return
        }

        if (lineBusiness.isEmpty()) {
            toast(getString(R.string.bottom_sheet_relateconcessmarket_toast_line_business_validation))
            return
        }

        participatingConcess = ParticipatingConcessSE(
            market.idFirebase,
            concessionaire.idFirebase,
            idFirebase = "",
            linearMeters,
            lineBusiness,
            market.name
        )

        mViewModel.relateConcessWithMarket(participatingConcess)
    }

}