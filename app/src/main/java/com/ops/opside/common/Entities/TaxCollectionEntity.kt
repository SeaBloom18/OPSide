package com.ops.opside.common.Entities

import com.ops.opside.flows.sign_on.taxCollectionCrudModule.dataClasses.DealerAssist

data class TaxCollectionEntity(
    val idCollection: String,
    var idTianguis: String,
    var tianguisName: String,
    var totalAmount: Double,
    val date: String,
    val startTime: String,
    val endTime: String,
    val taxCollector: String,
    val dealers: MutableList<DealerAssist>)
