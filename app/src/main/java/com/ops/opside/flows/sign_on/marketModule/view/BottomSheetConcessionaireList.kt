package com.ops.opside.flows.sign_on.marketModule.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.R
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.databinding.BottomSheetShowConcessBinding
import com.ops.opside.flows.sign_on.marketModule.viewModel.ConcessionaireListViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by David Alejandro González Quezada on 10/10/22.
 */
@AndroidEntryPoint
class BottomSheetConcessionaireList(private val market: (MarketSE) -> Unit = {}): BottomSheetDialogFragment() {

    private var mSelectedMarket: MarketSE? = null
    private lateinit var mMarketsList: MutableList<MarketSE>
    private val mViewModel: ConcessionaireListViewModel by viewModels()
    private val mBinding: BottomSheetShowConcessBinding by lazy {
        BottomSheetShowConcessBinding.inflate(layoutInflater)
    }
    private val mActivity: MarketRegisterActivity by lazy {
        activity as MarketRegisterActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            //tvBSTitle.text = market.invoke()
            //btnDeleteConcess.setOnClickListener { mViewModel.getMarketId("prueba id") }
        }
        //bindViewModel()
        returnSelectedMarket()
    }

    private fun returnSelectedMarket() {
        mSelectedMarket = searchSelectedMarket()

        if (mSelectedMarket != null){
            market.invoke(mSelectedMarket!!)
            dismiss()
        } else{
            Toast.makeText(mActivity, "error",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchSelectedMarket(): MarketSE? {
        for (item in mMarketsList){
            if (mBinding.tvBSTitle.text.toString() == item.name){
                return item
            }
        }
        return null
    }

    private fun bindViewModel(){
        mBinding.tvBSTitle.text = mViewModel.getMarketFirestoreId.observe(this, Observer(this::getMarketFirestoreId)).toString()
        Log.d("marketId", mViewModel.getMarketFirestoreId.observe(this, Observer(this::getMarketFirestoreId)).toString())
    }

    private fun getMarketFirestoreId(marketId: String){
        mBinding.tvBSTitle.text = marketId
        Log.d("marketId", marketId)
        Toast.makeText(mActivity, marketId, Toast.LENGTH_SHORT).show()
    }
}

/* Recorrer la lista y consultar el nombre para poder agregarlo al spinner,
junto con el id poder eliminarlo y ver su perfil completo*/

/*val dialog = BottomSheetDialog(this)
       val view = layoutInflater.inflate(R.layout.bottom_sheet_show_concess, null)

       val autoCompUserName = view.findViewById<AutoCompleteTextView>(R.id.acUserName)
       val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, concessionaires)
       autoCompUserName.setAdapter(adapter)

       val group = view.findViewById<Group>(R.id.groupConce)

       val textViewList = view.findViewById<TextView>(R.id.tvViewAllConcess)
       textViewList.setOnClickListener {
           if (group.visibility == View.VISIBLE){ //Si la vista esta oculta
               //TransitionManager.beginDelayedTransition(holder.binding.marketCardView)
               group.visibility = View.GONE
               //holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_up)
           } else{ //Si la vista esta expuesta
               //TransitionManager.endTransitions(holder.binding.marketCardView)
               group.visibility = View.VISIBLE
               //holder.binding.ibArrow.setImageResource(R.drawable.ic_item_arrow_down)
           }
       }
       dialog.setContentView(view)
       dialog.show()*/