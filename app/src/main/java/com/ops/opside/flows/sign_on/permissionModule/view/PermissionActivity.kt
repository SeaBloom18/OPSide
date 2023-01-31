package com.ops.opside.flows.sign_on.permissionModule.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.Modul
import com.ops.opside.common.entities.firestore.Moduls
import com.ops.opside.common.entities.firestore.ModulsTree
import com.ops.opside.common.utils.Formaters.orZero
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.FragmentPermissionBinding
import com.ops.opside.flows.sign_on.permissionModule.adapter.PermissionAdapter
import com.ops.opside.flows.sign_on.permissionModule.viewModel.PermissionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionActivity : BaseActivity() {

    private val mBinding: FragmentPermissionBinding by lazy {
        FragmentPermissionBinding.inflate(layoutInflater)
    }

    private val mViewModel: PermissionViewModel by viewModels()

    private lateinit var mAdapter: PermissionAdapter
    private lateinit var mRoles: MutableMap<String, ModulsTree>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.apply {
            btnSearchRol.setOnClickListener {
                val rolName = spPickRol.text.toString()
                if (mRoles.containsKey(rolName))
                    setUpRecyclerView(mRoles[rolName] ?: ModulsTree())
            }
            fabAddRol.setOnClickListener {
                val rolName = teRolName.text.toString()
                if (rolName.isEmpty()) {
                    toast("Asignale un nombre al rol")
                    return@setOnClickListener
                }
                mAdapter.mModules.numberRol = mRoles.size.orZero() + 1
                mAdapter.mModules.nameRol = teRolName.text.toString()
                mViewModel.createNewRol(mAdapter.getPermissionTreeVisible())
            }
        }

        setToolbar()
        bindViewModel()
        setUpRecyclerView()

        mViewModel.getAllRoles()
    }

    private fun bindViewModel() {
        mViewModel.getShowProgress().observe(this, Observer(this::showLoading))

        mViewModel.getAllRoles.observe(this, Observer(this::setUpSpinner))
    }

    /** Other Methods**/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        with(mBinding.toolbarFrgPermission.commonToolbar) {
            this.title = "Roles"
            setSupportActionBar(this)
            (context as PermissionActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    //menuInflater.inflate(R.menu.menu_common_fragment_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_add_rol -> {
                            setForNewRol()
                            true
                        }
                        /*R.id.menu_rol_reset -> {
                            mAdapter.resetPermissionToOrigin()
                            true
                        }*/
                        R.id.menu_check_all -> {
                            mAdapter.checkAllPermissions()
                            true
                        }
                        else -> false
                    }
                }
            }, this@PermissionActivity, Lifecycle.State.RESUMED)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_rol, menu)
        return true
    }

    private fun setUpSpinner(roles: MutableMap<String, ModulsTree>) {
        mRoles = roles
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roles.map { it.key })

        mBinding.spPickRol.setAdapter(adapter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpRecyclerView(tree: ModulsTree = ModulsTree()) {
        val gridLayoutManager: RecyclerView.LayoutManager
        gridLayoutManager = GridLayoutManager(this, 2)

        mAdapter = PermissionAdapter(tree, this)

        mBinding.rvModules.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = mAdapter
        }
        mAdapter.notifyDataSetChanged()

        mAdapter.saveBeforeTree()
    }

    private fun setForNewRol() {
        mBinding.tilRolName.isGone = false
        mBinding.fabAddRol.isGone = false

        val moduls: MutableList<Modul> = mutableListOf()

        Moduls.values().map {
            val modulName = when (it) {
                Moduls.Login -> getString(R.string.tables_name_login)
                Moduls.Registration -> getString(R.string.tables_name_registration)
                Moduls.Concessionaire -> getString(R.string.tables_name_concessionaire)
                Moduls.DashBoard -> getString(R.string.tables_name_dashboard)
                Moduls.Dealer -> getString(R.string.tables_name_dealer)
                Moduls.Incident -> getString(R.string.tables_name_incident)
                Moduls.Market -> getString(R.string.tables_name_market)
                Moduls.Profile -> getString(R.string.tables_name_profile)
                Moduls.TaxCollectionCrud -> getString(R.string.tables_name_taxcollection_crud)
                Moduls.TaxCollection -> getString(R.string.tables_name_taxcollection)
                Moduls.ControlPanel -> getString(R.string.tables_name_control_panel)
                Moduls.Permission -> getString(R.string.tables_name_permission)
            }

            moduls.add(Modul(modulName, false, mutableListOf(false, false, false, false)))
        }

        val tree = ModulsTree(moduls)

        setUpRecyclerView(tree)
    }


}