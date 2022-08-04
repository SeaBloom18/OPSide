package com.ops.opside.flows.sign_on.marketModule.adapters

import com.ops.opside.common.entities.Market

interface OnClickListener {
    fun onDeleteMarket(market: Market)
    fun onEditMarket(market: Market)
}