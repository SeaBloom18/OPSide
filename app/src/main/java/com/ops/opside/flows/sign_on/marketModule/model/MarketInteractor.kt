package com.ops.opside.flows.sign_on.marketModule.model

import com.ops.opside.common.entities.share.TianguisSE

class MarketInteractor {

    fun getMarkets(): MutableList<TianguisSE> {
        val tianguis = mutableListOf<TianguisSE>()

        val tianguis1 = TianguisSE(1, "Tianguis de muestra 1", "Direccion de muestra 1","",0.0,0.0,0)
        val tianguis2 = TianguisSE(2, "Tianguis de muestra 2", "Direccion de muestra 2","",0.0,0.0,0)
        val tianguis3 = TianguisSE(3, "Tianguis de muestra 3", "Direccion de muestra 3","",0.0,0.0,0)

        tianguis.add(tianguis1)
        tianguis.add(tianguis2)
        tianguis.add(tianguis3)

        return tianguis
    }
}