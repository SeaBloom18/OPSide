package com.ops.opside.flows.sign_on.incidentsModule.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
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
import com.ops.opside.flows.sign_on.incidentsModule.adapter.ListIncidentsAdapter

class IncidentsFragment : Fragment() {

    private var mBinding: FragmentIncidentsBinding? = null
    private val binding get() = mBinding!!
    private lateinit var mActivity: MainActivity

    private lateinit var mIncidentAdapter: IncidentAdapter

    private lateinit var mViewModel: IncidentsViewModel
    private lateinit var mIncidentList: MutableList<IncidentPersonFE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        mBinding = FragmentIncidentsBinding.inflate(inflater, container, false)
        mActivity = activity as MainActivity

        setToolbar()
        bindViewModel()
        loadIncidentsPersonsList()

        return binding.root
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

        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mIncidentAdapter
        }
    }

    private fun setToolbar(){
        with(binding.toolbarFragIncidents.commonToolbar){
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
                        R.id.see_incident -> {
                            seeIncidents()
                            true
                        }
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    private fun seeIncidents(){
        val dialog = BottomSheetIncidentsList()
        dialog.show(mActivity.supportFragmentManager, dialog.tag)
    }

    private fun createIncident(){
        val dialog = BottomSheetCreateMarket()
        dialog.isCancelable = true
        dialog.show(mActivity.supportFragmentManager, dialog.tag)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}