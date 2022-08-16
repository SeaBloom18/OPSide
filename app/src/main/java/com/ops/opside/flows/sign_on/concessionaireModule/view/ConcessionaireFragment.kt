package com.ops.opside.flows.sign_on.concessionaireModule.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.tryOrPrintException
import com.ops.opside.common.bsd.BottomSheetFilter
import com.ops.opside.databinding.FragmentConcessionaireBinding
import com.ops.opside.databinding.FragmentIncidentsBinding
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.ConcessionaireAdapter
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity

class ConcessionaireFragment : Fragment() {

    private var mBinding: FragmentConcessionaireBinding? = null
    private val binding get() = mBinding!!
    private lateinit var mAdapter: ConcessionaireAdapter
    private lateinit var mActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentConcessionaireBinding.inflate(inflater, container, false)

        mBinding.apply {
            //imgFilter.setOnClickListener { initBsd() }
        }

        initRecyclerView()
        setUpActivity()
        setToolbar()

        return binding.root
    }

    private fun setUpActivity() {
        mActivity = activity as MainActivity
    }

    private fun initBsd() {
        tryOrPrintException {
            val bottomSheetFilter = BottomSheetFilter()
            bottomSheetFilter.show(mActivity.supportFragmentManager, bottomSheetFilter.tag)
        }
    }

    private fun initRecyclerView() {
        val collections = mutableListOf<ConcessionaireSE>()

        for (i in 1..30) {
            collections.add(
                ConcessionaireSE(
                    i.toLong(), "Concesionario $i",
                    "David Gonzalez",
                    "",
                    "",
                    "", 3.0, ""
                )
            )
        }

        mAdapter = ConcessionaireAdapter(collections)

        var linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(context)

        binding.rvConcessionaires.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }

    private fun setToolbar(){
        with(binding.toolbar.commonToolbar){
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
                        R.id.menu_concessionaire_search -> {
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

}