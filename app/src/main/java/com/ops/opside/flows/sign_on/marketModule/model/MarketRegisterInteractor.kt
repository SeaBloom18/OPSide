package com.ops.opside.flows.sign_on.marketModule.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ops.opside.common.entities.DB_TABLE_CONCESSIONAIRE
import com.ops.opside.common.entities.DB_TABLE_MARKET
import com.ops.opside.common.entities.TablesEnum
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.MarketFE
import com.ops.opside.common.entities.share.MarketSE
import com.ops.opside.common.utils.getName
import com.ops.opside.common.utils.tryOrPrintException
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by David Alejandro González Quezada on 04/09/22.
 */

class MarketRegisterInteractor @Inject constructor(private val firestore: FirebaseFirestore) {

    fun registerMarket(marketFE: MarketFE): Observable<Boolean>{
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                firestore.collection(TablesEnum.Market.getName())
                    .add(marketFE.getHashMap())
                    .addOnSuccessListener {
                        Log.d("Firebase", "DocumentSnapshot added with ID: " + it.id)
                        subscriber.onNext(true)
                    }
                    .addOnFailureListener {
                            e -> Log.d("Firebase", "Error adding document", e)
                        subscriber.onNext(false)
                    }
            }
        }
    }

    fun updateMarket(idFirestore: String, name: String, address: String, marketMeters: Double,
                     latitude: Double, longitude: Double): Observable<Boolean>{
        return Observable.unsafeCreate{ subscriber ->
            tryOrPrintException {
                firestore.collection(TablesEnum.Market.getName()).document(idFirestore)
                    .update(mapOf(
                        "name" to name,
                        "address" to address,
                        "marketMeters" to marketMeters,
                        "latitude" to latitude,
                        "longitude" to longitude
                    ))
                    .addOnSuccessListener {
                        subscriber.onNext(true)
                        Log.d("FireStoreUpdateSuccess", "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }

    fun getConcessionairesForMarket(idMarketFirestore: String): Observable<MutableList<String>>{
        return Observable.unsafeCreate { subscriber ->
            tryOrPrintException {
                val concessionaireList = mutableListOf<String>()
                firestore.collection(TablesEnum.Market.getName()).document(idMarketFirestore).get()
                    .addOnSuccessListener {
                        Log.d("marketData",
                            it.data?.get("concessionaires").toString()[0].toString())
                        for (document in it.get("concessionaires").toString()){
                            concessionaireList.add(document.toString())
                        }
                        subscriber.onNext(concessionaireList)
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }
            }
        }
    }
}