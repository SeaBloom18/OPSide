package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.IncidentPersonFE
import com.ops.opside.databinding.FragmentIncidentsBinding
import com.ops.opside.flows.sign_on.incidentsModule.adapter.IncidentsAssignedAdapter
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.common.views.BaseFragment
import com.ops.opside.flows.sign_on.incidentsModule.viewModel.IncidentsAssignedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncidentsFragment : BaseFragment() {

    private lateinit var mBinding: FragmentIncidentsBinding
    private lateinit var mIncidentsAssignedAdapter: IncidentsAssignedAdapter
    private lateinit var mIncidentList: MutableList<IncidentPersonFE>
    private val mIncidentsViewModel: IncidentsAssignedViewModel by viewModels()
    private val mActivity: MainActivity by lazy { activity as MainActivity }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View {
        mBinding = FragmentIncidentsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Method call's **/
        setToolbar()
        bindViewModel()
        loadIncidentsPersonsList()
    }
    private fun bindViewModel() {
        mIncidentsViewModel.getShowProgress().observe(mActivity,
            Observer(mActivity::showLoading))
        mIncidentsViewModel._getIncidentPersonList.observe(mActivity,
            Observer(this::getIncidentPersonsList))
    }

    private fun loadIncidentsPersonsList() {
        mIncidentsViewModel.getTaxCollectionList()
    }

    private fun getIncidentPersonsList(incidentsList: MutableList<IncidentPersonFE>){
        mIncidentList = incidentsList
        setUpRecyclerViewIncident()
    }

    private fun setUpRecyclerViewIncident(){
        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(mActivity)

        mIncidentsAssignedAdapter = IncidentsAssignedAdapter(mIncidentList)

        mBinding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mIncidentsAssignedAdapter
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
                        R.id.create_incident_person -> {
                            createIncidentPerson()
                            true
                        }
                        R.id.create_incident -> {
                            createIncident()
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
            loadIncidentsPersonsList()
        }
    }

    private fun createIncidentPerson(){
        val dialog = BottomSheetCreateIncidentPerson()
        dialog.isCancelable = true
        dialog.show(mActivity.supportFragmentManager, dialog.tag)
    }

    private fun createIncident() {
        val dialog = BottomSheetCreateIncident()
        dialog.isCancelable = true
        dialog.show(mActivity.supportFragmentManager, dialog.tag)
    }
}