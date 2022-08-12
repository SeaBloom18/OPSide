package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.R
import com.ops.opside.common.entities.share.TianguisSE
import com.ops.opside.databinding.BottomSheetPickTianguisBinding
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetPickTianguisViewModel

class BottomSheetPickTianguis(
    private val selection: (TianguisSE) -> Unit = {}
) : BottomSheetDialogFragment() {

    private lateinit var mBinding: BottomSheetPickTianguisBinding
    private lateinit var mViewModel: BottomSheetPickTianguisViewModel
    private lateinit var mActivity: TaxCollectionActivity
    private lateinit var mTianguisList: MutableList<TianguisSE>
    private var mSelectedTianguis: TianguisSE? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = BottomSheetPickTianguisBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            btnPickTianguis.setOnClickListener {
                returnSelectedTianguis()
            }
        }

        setUpActivity()
        bindViewModel()
        loadTianguisList()
    }

    private fun returnSelectedTianguis() {
        mSelectedTianguis = searchSelectedTianguis()

        if (mSelectedTianguis != null){
            selection.invoke(mSelectedTianguis!!)
            dismiss()
        } else{
            Toast.makeText(mActivity, getString(R.string.tax_collection_choose_tianguis),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpActivity() {
        mActivity = activity as TaxCollectionActivity
    }

    private fun searchSelectedTianguis(): TianguisSE? {
        for (item in mTianguisList){
            if (mBinding.spPickTianguis.text.toString() == item.name){
                return item
            }
        }

        return null
    }

    private fun bindViewModel(){
        mViewModel = ViewModelProvider(requireActivity())[BottomSheetPickTianguisViewModel::class.java]

        mViewModel.getTianguisList.observe(this, Observer(this::getTianguisList))
    }

    private fun loadTianguisList(){
        mViewModel.getTianguisList()
    }

    private fun setUpSpinner() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, getTianguisListNames())
       mBinding.spPickTianguis.setAdapter(adapter)
    }

    private fun getTianguisListNames(): MutableList<String> {
        return mTianguisList.map { it.name }.toMutableList()
    }


    private fun getTianguisList(tianguisList: MutableList<TianguisSE>) {
        mTianguisList = tianguisList
        setUpSpinner()
    }


}