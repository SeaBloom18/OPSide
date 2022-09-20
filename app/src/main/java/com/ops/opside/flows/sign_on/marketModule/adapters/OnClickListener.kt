package com.ops.opside.flows.sign_on.marketModule.adapters

import com.ops.opside.common.entities.share.MarketSE

interface OnClickListener {
    fun onDeleteMarket(marketId: String)
    fun onEditMarket(market: MarketSE)
}