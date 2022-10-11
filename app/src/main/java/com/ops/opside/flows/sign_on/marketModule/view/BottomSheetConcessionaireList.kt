package com.ops.opside.flows.sign_on.marketModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ops.opside.databinding.BottomSheetPickMarketBinding
import com.ops.opside.databinding.BottomSheetShowConcessBinding
import com.ops.opside.flows.sign_on.mainModule.view.MainActivity
import com.ops.opside.flows.sign_on.marketModule.model.ConcessionaireListInteractor
import com.ops.opside.flows.sign_on.taxCollectionModule.view.TaxCollectionActivity
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by David Alejandro González Quezada on 10/10/22.
 */
@AndroidEntryPoint
class BottomSheetConcessionaireList: BottomSheetDialogFragment() {

    private lateinit var mConcessionaireListInteractor: ConcessionaireListInteractor
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
            tvBSTitle.text = (mConcessionaireListInteractor.getConcessionaireList().toString())
        }
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