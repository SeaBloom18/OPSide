package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.TaxCollectionSE
import com.ops.opside.common.utils.*
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.databinding.FragmentFinalizeTaxCollectionBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.view.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.actions.FinalizeTaxCollectionAction
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.AbsenceTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.EmailObject
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.FinalizeTaxCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class FinalizeTaxCollectionFragment : BaseFragment() {

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
        mViewModel.getShowProgress().observe(requireActivity(), Observer(this::showLoading))
        mViewModel.getAction().observe(requireActivity(), Observer(this::handleAction))
    }

    private fun handleAction(action: FinalizeTaxCollectionAction) {
        when (action) {
            is FinalizeTaxCollectionAction.SendCollection -> insertTaxCollection()
            is FinalizeTaxCollectionAction.SendEmails -> sendAbsenceEmails(mAdapter.getListAbsenceEmails())
            is FinalizeTaxCollectionAction.ShowMessageError -> showError(action.error)
            is FinalizeTaxCollectionAction.FinalizeCollection -> finalizeCollection()
        }
    }

    private fun insertTaxCollection() {
        try {
            mFinalizeCollection.taxCollection.endDate =
                CalendarUtils.getCurrentTimeStamp(FORMAT_SQL_DATE)
            mFinalizeCollection.taxCollection.endTime =
                CalendarUtils.getCurrentTimeStamp(FORMAT_TIME)
            mViewModel.searchIfExistTaxCollection(mFinalizeCollection.taxCollection)
        } catch (e: Exception) {
            showError(e.message.toString())
        }
    }

    private fun sendAbsenceEmails(list: List<ItemAbsence>) {
        GlobalScope.launch {

            val emails: MutableList<EmailObject> = mutableListOf()
            list.forEach { concessionaire ->
                val body = getString(
                    R.string.tax_collection_absence_email,
                    concessionaire.dealerName,
                    mFinalizeCollection.marketName,
                    mFinalizeCollection.collector
                )
                emails.add(
                    EmailObject(
                        subject = "Inasistencia ${CalendarUtils.getCurrentTimeStamp(FORMAT_DATE)}",
                        body = body,
                        recipient = concessionaire.email
                    )
                )
            }

            EmailSender.send(emails) {
                if (it.first) {
                    showLoading(false)
                    mViewModel.closeTaxcollection(mFinalizeCollection.taxCollection)
                } else {
                    showError(it.second)
                }
            }

        }
    }

    private fun finalizeCollection() {
        if (mFinalizeCollection.type == "create") {
            val activity = activity as? TaxCollectionActivity
            activity?.startActivity<MainActivity>()
        } else {
            val activity = activity as? TaxCollectionCrudActivity
            activity?.startActivity<MainActivity>()
        }
    }

    private fun closeFragment() {
        if (mFinalizeCollection.type == "create") {
            val activity = activity as? TaxCollectionActivity
            activity?.showButtons()
            activity!!.supportFragmentManager.popBackStack()
        } else {
            val activity = activity as? TaxCollectionCrudActivity
            activity?.showButtons()
            activity!!.supportFragmentManager.popBackStack()
        }
    }

    private fun obtainArguments() {
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

    override fun onPause() {
        super.onPause()
        closeFragment()
    }

    @Parcelize
    data class FinalizeCollection(
        val type: String = "create",
        val idMarket: String,
        val marketName: String,
        val collector: String,
        val totalAmount: Double,
        val taxCollection: TaxCollectionSE,
        val absences: MutableList<ConcessionaireSE>
    ) : Parcelable
}