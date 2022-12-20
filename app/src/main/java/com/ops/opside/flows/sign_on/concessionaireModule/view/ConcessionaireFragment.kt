package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.common.bsd.KEY_FILTER_REQUEST
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
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            teSearch.doAfterTextChanged {
                mAdapter.filter(it.toString())
            }
        }

        bindViewModel()
        setToolbar()
        loadConcessionairesList()
    }

    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(requireActivity(), Observer(this::showLoading))
        mViewModel.getConcessionairesList.observe(
            requireActivity(),
            Observer(this::getConcessList)
        )
    }

    private fun setToolbar() {
        with(mBinding.toolbarFragConce.commonToolbar) {
            this.title = getString(R.string.bn_menu_concessionaire_opc2)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_concessionaire_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
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
            val bottomSheetFilter = BottomSheetFilter(
                showMarket = true,
                showCollector = false,
                showDate = false
            )

            parentFragment?.setFragmentResultListener(KEY_FILTER_REQUEST) { _, bundle ->
                mViewModel.getConcessByMarketList(
                    bundle.getStringArrayList("market") as MutableList<String>
                )
            }

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

        mAdapter.notifyDataSetChanged()
    }

    private fun getConcessList(concessionairesList: MutableList<ConcessionaireSE>) {
        mConcessionairesList = concessionairesList
        initRecyclerView()
    }

    private fun loadConcessionairesList() {
        mViewModel.getConcessionairesList()
    }


}