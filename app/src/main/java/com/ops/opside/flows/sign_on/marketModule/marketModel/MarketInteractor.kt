package com.ops.opside.flows.sign_on.marketModule.marketModel

import com.ops.opside.common.entities.Market

class MarketInteractor {

    fun getMarkets(): MutableList<Market> {
        val markets = mutableListOf<Market>()

        val tianguis1 = Market(1, "Tianguis de muestra 1", "Direccion de muestra 1")
        val tianguis2 = Market(2, "Tianguis de muestra 2", "Direccion de muestra 2")
        val tianguis3 = Market(3, "Tianguis de muestra 3", "Direccion de muestra 3")

        markets.add(tianguis1)
        markets.add(tianguis2)
        markets.add(tianguis3)

        return markets
    }
}