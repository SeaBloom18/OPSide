package com.ops.opside.flows.sign_on.incidentsModule.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.ops.opside.R
import com.ops.opside.common.entities.IncidentEntity
import com.ops.opside.common.entities.IncidentPersonEntity
import com.ops.opside.databinding.FragmentIncidentsBinding
import com.ops.opside.flows.sign_on.dashboardModule.view.ControlPanelActivity
import com.ops.opside.flows.sign_on.incidentsModule.adapter.IncidentAdapter
import com.ops.opside.flows.sign_on.incidentsModule.adapter.ListIncidentsAdapter
import com.ops.opside.flows.sign_on.mainModule.MainActivity

class IncidentsFragment : Fragment() {

    private var mBinding: FragmentIncidentsBinding? = null
    private val binding get() = mBinding!!
    private lateinit var mActivity: MainActivity

    private lateinit var listIncidentAdapter: ListIncidentsAdapter
    private lateinit var incidentAdapter: IncidentAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentIncidentsBinding.inflate(inflater, container, false)
        mActivity = activity as MainActivity

        setToolbar()

        setUpRecyclerViewIncident()
        return binding.root
    }

    private fun getIncidentList(): MutableList<IncidentEntity> {
        val incident = mutableListOf<IncidentEntity>()

        val incident1 = IncidentEntity(1, "Inasistencia 1", 54, 1)
        val incident2 = IncidentEntity(1, "Inasistencia 2", 31, 2)

        incident.add(incident1)
        incident.add(incident2)

        return incident
    }

    private fun setUpRecyclerViewIncident(){
        incidentAdapter = IncidentAdapter(getIncidents())
        linearLayoutManager = LinearLayoutManager(context)

        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = incidentAdapter
        }
    }

    private fun getIncidents(): MutableList<IncidentPersonEntity> {
        val incident = mutableListOf<IncidentPersonEntity>()

        val incident1 = IncidentPersonEntity(1, "David Gonzalez", "Mario Razo", "03/08/2022", 7, 76)
        val incident2 = IncidentPersonEntity(2, "Alejandro Quezada", "Mario Razo", "08/03/2022", 87, 54)

        incident.add(incident1)
        incident.add(incident2)

        return incident
    }

    private fun setToolbar(){
        with(binding.toolbar.commonToolbar){
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
        val dialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_see_incidents, null)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerIncident)

        listIncidentAdapter = ListIncidentsAdapter(getIncidentList())
        linearLayoutManager = LinearLayoutManager(context)

        recycler.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = listIncidentAdapter
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun createIncident(){
        val dialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_create_incident, null)

        val btnCreateIncident = view.findViewById<MaterialButton>(R.id.btnCreateIncident)
        btnCreateIncident.setOnClickListener {
            Toast.makeText(activity, "Incident created", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}