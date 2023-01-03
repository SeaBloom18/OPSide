package com.ops.opside.common.utils

import com.ops.opside.BuildConfig
import com.ops.opside.common.entities.*
import javax.inject.Inject

fun TablesEnum.getName(): String {
    val base = if (BuildConfig.DEBUG){ "dev_" } else { "" }
    return when (this) {
        TablesEnum.Concessionaire       -> base.plus(DB_TABLE_CONCESSIONAIRE       )
        TablesEnum.Market               -> base.plus(DB_TABLE_MARKET               )
        TablesEnum.ForeignAssistence    -> base.plus(DB_TABLE_FOREIGNER_ASSISTENCE )
        TablesEnum.IncidentPerson       -> base.plus(DB_TABLE_INCIDENT_PERSON      )
        TablesEnum.Incident             -> base.plus(DB_TABLE_INCIDENT             )
        TablesEnum.ParticipatingConcess -> base.plus(DB_TABLE_PARTICIPATING_CONCESS)
        TablesEnum.TaxCollection        -> base.plus(DB_TABLE_TAX_COLLECTION       )
        TablesEnum.Event                -> base.plus(DB_TABLE_EVENT                )
        TablesEnum.EmailSent            -> base.plus(DB_TABLE_EMAIL_SENT           )
        TablesEnum.PendingEmail         -> base.plus(DB_TABLE_PENDING_EMAIL        )
        TablesEnum.Resources            -> base.plus(DB_TABLE_RESOURCES            )
        TablesEnum.Origin               -> base.plus(DB_TABLE_ORIGIN               )
        TablesEnum.Collector            -> base.plus(DB_TABLE_COLLECTOR            )
    }
}