package com.ops.opside.flows.sign_on.dealerModule.view.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ops.opside.common.utils.applySchedulers
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

    private val updateImageURL = MutableLiveData<Boolean>()
    var updateImage: LiveData<Boolean> = updateImageURL

    fun showPersonalInfo(): Triple<String?, String?, String?> =
        mDealerInteractor.showPersonalInfo()

    fun showAboutInfo(): Triple<String?, String?, String?> =
        mDealerInteractor.showAboutInfo()

    fun showPricesInfo(): Pair<String?, String?> =
        mDealerInteractor.showPricesInfo()

    fun uploadUserImage(uri: Uri) {
        disposable.add(
            mDealerInteractor.uploadUserImage(uri).applySchedulers()
                .doOnSubscribe { showProgress.value = true }
                .subscribe(
                    {
                        showProgress.value = false
                        updateImageURL.value = true
                        Log.d("imageUrl", it.toString())
                        mDealerInteractor.apply {
                            updateImageURL(it.toString())
                            deleteUserImage()
                        }
                    },
                    {
                        Log.e("Error", it.toString())
                        showProgress.value = false
                    }
                )
        )
    }
}