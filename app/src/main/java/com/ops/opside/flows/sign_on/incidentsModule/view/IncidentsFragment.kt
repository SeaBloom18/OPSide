package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.databinding.FragmentIncidentsBinding
import com.ops.opside.flows.sign_on.incidentsModule.adapter.IncidentAdapter
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.IncidentsViewModel
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import androidx.lifecycle.Observer
import com.ops.opside.common.views.BaseFragment

class IncidentsFragment : BaseFragment() {

    private lateinit var mBinding: FragmentIncidentsBinding
    private val mActivity: MainActivity by lazy { activity as MainActivity }
    private lateinit var mIncidentAdapter: IncidentAdapter
    private lateinit var mViewModel: IncidentsViewModel
    private lateinit var mIncidentList: MutableList<IncidentPersonFE>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View {
        mBinding = FragmentIncidentsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        bindViewModel()
        loadIncidentsPersonsList()
    }
    private fun bindViewModel() {
        mViewModel = ViewModelProvider(requireActivity())[IncidentsViewModel::class.java]
        mViewModel.getIncidentsPersonList.observe(mActivity, Observer(this::getIncidentPersonsList))
    }

    private fun loadIncidentsPersonsList() {
        mViewModel.getIncidentsPersonList()
    }

    private fun getIncidentPersonsList(incidentsList: MutableList<IncidentPersonFE>){
        mIncidentList = incidentsList
        setUpRecyclerViewIncident()
    }

    private fun setUpRecyclerViewIncident(){
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(mActivity)

        mIncidentAdapter = IncidentAdapter(mIncidentList)

        mBinding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mIncidentAdapter
        }
    }

    private fun setToolbar(){
        with(mBinding.toolbarFragIncidents.commonToolbar){
            this.title = getString(R.string.bn_menu_incidents_opc5)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.incidents_toolbar_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when(menuItem.itemId){
                        R.id.create_incident -> {
                            createIncident()
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun createIncident(){
        val dialog = BottomSheetCreateMarket()
        dialog.isCancelable = true
        dialog.show(mActivity.supportFragmentManager, dialog.tag)
    }
}