package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.databinding.FragmentConcessionaireBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.ConcessionaireAdapter
import com.ops.opside.flows.sign_on.concessionaireModule.viewModel.ConcessionaireViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConcessionaireFragment : BaseFragment() {

    private lateinit var mBinding: FragmentConcessionaireBinding
    private lateinit var mAdapter: ConcessionaireAdapter

    private val mActivity: MainActivity by lazy {
        activity as MainActivity
    }

    private val mViewModel: ConcessionaireViewModel by viewModels()

    private lateinit var mConcessionairesList: MutableList<ConcessionaireSE>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentConcessionaireBinding.inflate(inflater, container, false)

        bindViewModel()
        setToolbar()
        loadConcessionaresList()
        return mBinding.root
    }

    private fun bindViewModel() {
        mViewModel.getConcessionairesList.observe(requireActivity(), Observer(this::getAbsencesList))
    }

    private fun setToolbar(){
        with(mBinding.toolbarFragConce.commonToolbar){
            this.title = getString(R.string.bn_menu_concessionaire_opc2)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_concessionaire_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when(menuItem.itemId){
                        R.id.menu_concessionaire_filter -> {
                            initBsd()
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun initBsd() {
        tryOrPrintException {
            val bottomSheetFilter = BottomSheetFilter()
            bottomSheetFilter.show(mActivity.supportFragmentManager, bottomSheetFilter.tag)
        }
    }

    private fun initRecyclerView() {
        mAdapter = ConcessionaireAdapter(mConcessionairesList)

        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context)

        mBinding.rvConcessionaires.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }

    private fun getAbsencesList(concessionaresList: MutableList<ConcessionaireSE>){
        mConcessionairesList = concessionaresList

        initRecyclerView()
    }

    private fun loadConcessionaresList(){
        mViewModel.getConcessionairesList()
    }


}