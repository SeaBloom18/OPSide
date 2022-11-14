package com.ops.opside.common.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ops.opside.common.entities.share.PendingEmailSE

@Dao
interface PendingEmailDao {
    @Delete
    fun deletePendingEmail(pendingEmail: PendingEmailSE)

    @Insert
    fun addPendingEmail(pendingEmail: PendingEmailSE): Long

    @Query("SELECT * FROM pendingEmail")
    fun getAllPendingEmails(): MutableList<PendingEmailSE>

    @Query("SELECT * FROM pendingEmail WHERE idTaxCollection = :idTaxCollection")
    fun getAllPendingEmailsByCollection(idTaxCollection: String): MutableList<PendingEmailSE>
}