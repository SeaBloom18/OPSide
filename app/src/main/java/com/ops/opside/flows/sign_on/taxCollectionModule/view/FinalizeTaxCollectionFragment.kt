package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.utils.Formaters.formatCurrency
import com.ops.opside.databinding.FragmentFinalizeTaxCollectionBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.view.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.FinalizeTaxCollectionAction
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.AbsenceTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.FinalizeTaxCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class FinalizeTaxCollectionFragment : Fragment() {

    private lateinit var mBinding: FragmentFinalizeTaxCollectionBinding
    private lateinit var mAdapter: AbsenceTaxCollectionAdapter

    private val mViewModel: FinalizeTaxCollectionViewModel by viewModels()

    private lateinit var mFinalizeCollection: FinalizeCollection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFinalizeTaxCollectionBinding.inflate(inflater, container, false)

        mBinding.apply {

            btnClose.animateOnPress()
            btnClose.setOnClickListener {
                closeFragment()
            }

            btnSend.setOnClickListener {
                mViewModel.checkBiometrics(this@FinalizeTaxCollectionFragment)
            }
        }

        setUpActivity()
        bindViewModel()

        return mBinding.root
    }

    private fun bindViewModel() {
        mViewModel.getAction().observe(requireActivity(), Observer(this::handleAction))
    }

    private fun handleAction(action: FinalizeTaxCollectionAction){
        when(action){
            is FinalizeTaxCollectionAction.SendCollection -> requireActivity().startActivity<MainActivity>()
        }
    }

    private fun closeFragment() {
        if (mFinalizeCollection.type == "create") {
            val activity = activity as? TaxCollectionActivity
            activity?.showButtons()
            activity?.onBackPressed()
        } else {
            val activity = activity as? TaxCollectionCrudActivity
            activity?.showButtons()
            activity?.onBackPressed()
        }
    }

    private fun obtainArguments(){
        tryOrPrintException {
            arguments?.let {
                tryOrPrintException {
                    it.getParcelable<FinalizeCollection>("finalizeCollection")?.let {
                        mFinalizeCollection = it
                    }

                    if (mFinalizeCollection.type == "update") {
                        mBinding.tvTitle.text = getString(R.string.tax_collection_update_title)
                        mBinding.etTotalAmount.isEnabled = true
                    }

                    Log.d("Demo", mFinalizeCollection.absences.toString())
                }
            }
        }
    }

    private fun setUpActivity() {
        obtainArguments()

        if (mFinalizeCollection.type == "create") {
            (activity as? TaxCollectionActivity)?.hideButtons()
            TaxCollectionActivity()
        } else {
            (activity as? TaxCollectionCrudActivity)?.hideButtons()
            TaxCollectionCrudActivity()
        }

        mBinding.apply {
            etMarketName.setText(mFinalizeCollection.marketName)
            etTotalAmount.setText(mFinalizeCollection.totalAmount.toString())
            etSent.setText(mFinalizeCollection.collector)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = AbsenceTaxCollectionAdapter(
            (mFinalizeCollection.absences.map {
                ItemAbsence(ID.getTemporalId(), it.name, it.email, false)
            }) as MutableList<ItemAbsence>
        )

        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context!!)

        mBinding.rvAbsence.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }

    @Parcelize
    data class FinalizeCollection(
        val type: String = "create",
        val idMarket: String,
        val marketName: String,
        val collector: String,
        val totalAmount: Double,
        val absences: MutableList<ConcessionaireSE>
    ) : Parcelable

}