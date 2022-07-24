package com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses

data class ItemRecord(val action: String, val name: String, val hour: String, var amount: Double = 0.0)
