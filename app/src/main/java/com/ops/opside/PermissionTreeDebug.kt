package com.ops.opside

import com.ops.opside.common.entities.firestore.Moduls
import com.ops.opside.common.entities.firestore.Permission
import com.ops.opside.common.utils.Manager

fun main() {
    /*val modulMarkets = Modul("Market", true, mutableListOf(true,true,true,false))
    val modulTaxCollection = Modul("TaxCollection", true, mutableListOf(true,true,true,false))
    val modulConcessionaire = Modul("Concessionaire", true, mutableListOf(true,true,true,false))

    val moduls = ModulsTree(mutableListOf(modulMarkets,modulTaxCollection,modulConcessionaire))*/

    val json = "{\"moduls\":[{\"name\":\"Market\",\"access\":true,\"permissions\":[true,true,true,false]},{\"name\":\"TaxCollection\",\"access\":true,\"permissions\":[true,true,true,false]},{\"name\":\"Concessionaire\",\"access\":true,\"permissions\":[true,true,true,false]}]}"
    Manager.getInstance(json)

    println("hasPermission: ${Manager().verify(Moduls.Market, Permission.delete)}")
}