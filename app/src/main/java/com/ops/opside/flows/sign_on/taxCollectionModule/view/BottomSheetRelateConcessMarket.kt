package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.common.entities.room.EventRE
import com.ops.opside.common.entities.room.ParticipatingConcessRE
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
    private val status: (Pair<Boolean, ParticipatingConcessRE>) -> Unit = {}
) : BaseBottomSheetFragment() {

    private val mBinding: BottomSheetRelateConcessMarketBinding by lazy {
        BottomSheetRelateConcessMarketBinding.inflate(layoutInflater)
    }

    private lateinit var participatingConcess: ParticipatingConcessRE

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
    }

    private fun isPersisted(isPersisted: Boolean) {
        status.invoke(Pair(isPersisted, participatingConcess))
        dismiss()
    }


    private fun relate(linearMeters: Double, lineBusiness: String) {
        if (linearMeters <= 0) {
            Toast.makeText(
                requireContext(),
                "Los metros lineales deben ser mayor a 0",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (lineBusiness.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "El giro de negocio no puede estar vacío",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        participatingConcess = ParticipatingConcessRE(
            market.idFirebase,
            concessionaire.idFirebase,
            idFirebase = "",
            linearMeters,
            lineBusiness
        )

        mViewModel.relateConcessWithMarket(participatingConcess)
    }

}