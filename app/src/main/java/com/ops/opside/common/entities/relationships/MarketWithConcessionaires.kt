package com.ops.opside.common.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ops.opside.common.entities.room.ParticipatingConcessRE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.entities.share.MarketSE

data class MarketWithConcessionaires(
    @Embedded val market: MarketSE,
    @Relation(
        parentColumn =  "idFirebase",
        entity = ConcessionaireSE::class,
        entityColumn =  "idFirebase",
        associateBy = Junction(
            ParticipatingConcessRE::class,
            parentColumn = "idMarket",
            entityColumn = "idConcessionaire"
        )
    ) val concessionairetIdList: List<String>
)
