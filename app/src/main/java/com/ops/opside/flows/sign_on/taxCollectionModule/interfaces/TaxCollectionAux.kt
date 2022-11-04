package com.ops.opside.flows.sign_on.taxCollectionModule.interfaces

import com.ops.opside.common.entities.room.EventRE

interface TaxCollectionAux {
    fun hideButtons()
    fun showButtons()
    fun revertEvent(event: EventRE)
}