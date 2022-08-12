package com.ops.opside.flows.sign_on.marketModule.adapters

import com.ops.opside.common.entities.share.TianguisSE

interface OnClickListener {
    fun onDeleteMarket(tianguis: TianguisSE)
    fun onEditMarket(tianguis: TianguisSE)
}