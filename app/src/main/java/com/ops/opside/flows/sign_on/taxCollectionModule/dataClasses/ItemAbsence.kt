package com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ItemAbsence(
    val id: String,
    val dealerName: String,
    val email: String,
    var isAssist: Boolean = false
) : Parcelable