package com.ops.opside.common.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.ConcessionaireSE

data class ConcessionaireWithMarkets(
    @Embedded val concessionaire: ConcessionaireSE,
    @Relation(
        parentColumn =  "idConcessionaire",
        entityColumn =  "idMarket",
        associateBy = Junction(ParticipatingConcessRE::class)
    ) val marketIdList: MutableList<String>
)
