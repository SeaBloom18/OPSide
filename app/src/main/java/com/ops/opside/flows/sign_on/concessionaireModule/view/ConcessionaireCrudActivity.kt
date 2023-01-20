package com.ops.opside.flows.sign_on.concessionaireModule.view

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ops.opside.R
import com.ops.opside.common.adapters.SwipeToDeleteCallback
import com.ops.opside.common.dialogs.BaseDialog
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.entities.share.ParticipatingConcessSE
import com.ops.opside.common.utils.Formaters.orFalse
import com.ops.opside.common.utils.PDFUtils
import com.ops.opside.common.utils.PermissionManagger
import com.ops.opside.common.views.BaseActivity
import com.ops.opside.databinding.ActivityConcessionaireCrudBinding
import com.ops.opside.flows.sign_off.loginModule.actions.LoginAction
import com.ops.opside.flows.sign_on.concessionaireModule.action.ConcessionaireCrudAction
import com.ops.opside.flows.sign_on.concessionaireModule.adapters.MarketParticipatingAdapter
import com.ops.opside.flows.sign_on.concessionaireModule.viewModel.ConcessionaireCrudViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.view.BottomSheetRelateConcessMarket
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConcessionaireCrudActivity : BaseActivity() {

    private lateinit var mAdapter: MarketParticipatingAdapter

    private val mBinding: ActivityConcessionaireCrudBinding by lazy {
        ActivityConcessionaireCrudBinding.inflate(layoutInflater)
    }

    private val mViewModel: ConcessionaireCrudViewModel by viewModels()

    @Inject
    lateinit var permissionManagger: PermissionManagger

    private lateinit var mRelatesList: MutableList<ParticipatingConcessSE>
    private lateinit var mConcessionaire: ConcessionaireSE
    private lateinit var mMarketsList: MutableList<MarketSE>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mConcessionaire =
            intent.getParcelableExtra("concess") ?: ConcessionaireSE()

        mBinding.apply {
            etConcessionaireName.setText(mConcessionaire.name)
            etAddress.setText(mConcessionaire.address)
            etEmail.setText(mConcessionaire.email)
            etPhone.setText(mConcessionaire.phone)
            etAbsences.setText("0")

            btnAddMarket.setOnClickListener {
                openRealateConcess()
            }

            tilPickMarket.isGone = permissionManagger.getPermission().not()
            spPickMarket.isGone = permissionManagger.getPermission().not()
            btnAddMarket.isGone = permissionManagger.getPermission().not()
        }

        setToolbar()
        bindViewModel()
        loadMarketsList()
    }

    private fun bindViewModel() {
        mViewModel.getRelatesList.observe(this, Observer(this::getRelatesList))
        mViewModel.getMarkets.observe(this, Observer(this::getMarkets))
        mViewModel.getAction().observe(this, Observer(this::handleAction))
    }

    private fun setToolbar() {
        with(mBinding.toolbarCrudConce.commonToolbar) {
            this.title = getString(R.string.bn_menu_concessionaire_opc2)

            this.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_common_fragment_toolbar, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_cancel -> {
                            onBackPressed()
                            true
                        }
                        R.id.menu_print -> {
                            PDFUtils.generatePDFBadgeSize(
                                this@ConcessionaireCrudActivity,
                                mutableListOf(
                                    PDFUtils.qrModel(
                                        mConcessionaire.name,
                                        mConcessionaire.idFirebase
                                    )
                                )
                            )
                            true
                        }
                        else -> false
                    }
                }
            }, this@ConcessionaireCrudActivity, Lifecycle.State.RESUMED)
        }
    }

    private fun handleAction(action: ConcessionaireCrudAction) {
        when (action) {
            is ConcessionaireCrudAction.MessageError -> showError(action.error)
            is ConcessionaireCrudAction.SuccesRelation -> addRelationToList()
            is ConcessionaireCrudAction.SucessEliminationRelation -> {
                mRelatesList.remove(action.relation)
                initRecyclerView()
                toast(getString(R.string.concessionaire_elimination_success))
            }
        }
    }

    private fun verifyIfExistRelation(marketName: String): Boolean {
        mRelatesList.filter { it.marketName == marketName }.map { return true }
        return false
    }

    private fun addRelationToList(){
        initRecyclerView()
    }

    private fun openRealateConcess() {
        val marketName = mBinding.spPickMarket.text.toString()

        if (marketName == "") {
            toast(getString(R.string.concessionaire_pick_market))
            return
        }

        if (verifyIfExistRelation(marketName)) {
            toast(getString(R.string.concessionaire_market_was_already_related))
            return
        }

        val dialog = BottomSheetRelateConcessMarket(
            mConcessionaire, searchMarketByName(marketName)
        ) {
            if (it.first) {
                mViewModel.addMarketToConcess(
                    searchMarketByName(marketName).idFirebase,
                    mConcessionaire.idFirebase
                )
                mRelatesList.add(it.second)
            } else {
                toast(getString(R.string.concessionaire_cant_add_market))
            }
        }

        dialog.show(supportFragmentManager, dialog.tag)
    }

    private fun initRecyclerView() {
        mAdapter = MarketParticipatingAdapter(mRelatesList){ relation ->
            val dialog = BaseDialog(
                context = this,
                imageResource = R.drawable.ic_ops_delete,
                mTitle = getString(R.string.concessionaire_delete_relation),
                mDescription = getString(R.string.concessionaire_market_will_delete),
                buttonYesText = getString(R.string.common_accept),
                buttonNoText = getString(R.string.common_cancel),
                yesAction = {
                    mViewModel.deleteRelate(relation)
                }
            )
            dialog.show()
        }

        val linearLayoutManager: RecyclerView.LayoutManager
        linearLayoutManager = LinearLayoutManager(this)

        mBinding.rvParticipatingMarket.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.markets.removeAt(viewHolder.adapterPosition)
                mAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        /*itemTouchHelper.attachToRecyclerView(mBinding.rvParticipatingMarket)*/

        mAdapter.notifyDataSetChanged()
    }

    private fun getRelatesList(relatesList: MutableList<ParticipatingConcessSE>) {
        mRelatesList = relatesList
        initRecyclerView()
    }

    private fun getMarkets(marketsList: MutableList<MarketSE>) {
        mMarketsList = marketsList
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                marketsList.map { it.name })
        mBinding.spPickMarket.setAdapter(adapter)
    }

    private fun searchMarketByName(marketName: String): MarketSE {
        return mMarketsList.filter { it.name == marketName }[0]
    }

    private fun loadMarketsList() {
        mViewModel.getMarketList()
        mViewModel.getRelatesList(mConcessionaire.idFirebase)
    }
}