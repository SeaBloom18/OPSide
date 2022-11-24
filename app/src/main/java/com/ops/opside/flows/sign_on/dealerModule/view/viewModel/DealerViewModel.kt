package com.ops.opside.flows.sign_on.dealerModule.view.viewModel

import com.ops.opside.common.viewModel.CommonViewModel
import com.ops.opside.flows.sign_on.dealerModule.view.model.DealerInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 02/11/22.
 */
@HiltViewModel
class DealerViewModel @Inject constructor(
    private val mDealerInteractor: DealerInteractor): CommonViewModel(){

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        mDealerInteractor.showPersonalInfo()

    fun showAboutInfo(): Pair<String?, String?> =
        mDealerInteractor.showAboutInfo()

    fun showPricesInfo(): Pair<String?, String?> =
        mDealerInteractor.showPricesInfo()
}