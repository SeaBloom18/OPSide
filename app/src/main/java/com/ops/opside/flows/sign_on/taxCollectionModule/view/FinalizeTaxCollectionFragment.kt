package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.common.utils.launchActivity
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.databinding.FragmentFinalizeTaxCollectionBinding
import com.ops.opside.flows.sign_on.taxCollectionCrudModule.view.TaxCollectionCrudActivity
import com.ops.opside.flows.sign_on.taxCollectionModule.adapters.AbsenceTaxCollectionAdapter
import com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses.ItemAbsence
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.FinalizeTaxCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinalizeTaxCollectionFragment : Fragment() {

    private lateinit var mBinding: FragmentFinalizeTaxCollectionBinding
    private lateinit var mAdapter: AbsenceTaxCollectionAdapter

    private val mViewModel: FinalizeTaxCollectionViewModel by viewModels()

    private lateinit var mAbsencesList: MutableList<ItemAbsence>
    private lateinit var mTypeTransaction: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFinalizeTaxCollectionBinding.inflate(inflater, container, false)

        mBinding.apply {

            btnCloseFinalize.animateOnPress()
            btnCloseFinalize.setOnClickListener {
                closeFragment()
            }

            cbAgreeDeclaration.setOnClickListener {
                btnSend.isEnabled = cbAgreeDeclaration.isChecked
            }

            btnSend.setOnClickListener {
                if (mTypeTransaction == "create") {
                    launchActivity(TaxCollectionActivity(), context!!)
                } else {
                    launchActivity(TaxCollectionCrudActivity(), context!!)
                }
            }

        }

        setUpActivity()
        bindViewModel()
        loadAbsencesList()

        return mBinding.root
    }

    private fun bindViewModel() {
        mViewModel.getAbsencesList.observe(requireActivity(), Observer(this::getAbsencesList))
    }


    private fun closeFragment() {
        if (mTypeTransaction == "create") {
            val activity = activity as? TaxCollectionActivity
            activity?.showButtons()
            activity?.onBackPressed()
        } else {
            val activity = activity as? TaxCollectionCrudActivity
            activity?.showButtons()
            activity?.onBackPressed()
        }
    }

    private fun setUpActivity() {

        tryOrPrintException {
            val bundle = arguments

            bundle?.let {
                tryOrPrintException {
                    mTypeTransaction = bundle.getString("type")!!

                    if (mTypeTransaction == "update") {
                        mBinding.txtTitle.text = getString(R.string.tax_collection_update_title)
                        mBinding.etTotalAmount.isEnabled = true
                    }
                }
            }
        }

        if (mTypeTransaction == "create") {
            (activity as? TaxCollectionActivity)?.hideButtons()
            TaxCollectionActivity()
        } else {
            (activity as? TaxCollectionCrudActivity)?.hideButtons()
            TaxCollectionCrudActivity()
        }

    }

    private fun initRecyclerView() {
        mAdapter = AbsenceTaxCollectionAdapter(mAbsencesList)

        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context!!)

        mBinding.rvAbsence.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }

    private fun getAbsencesList(absencesList: MutableList<ItemAbsence>){
        mAbsencesList = absencesList

        initRecyclerView()
    }

    private fun loadAbsencesList(){
        mViewModel.getAbsencesList()
    }

}