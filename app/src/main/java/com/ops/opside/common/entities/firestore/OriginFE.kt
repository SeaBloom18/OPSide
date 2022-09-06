package com.ops.opside.common.entities.firestore

/**
 * Created by David Alejandro González Quezada on 02/09/22.
 */
data class OriginFE(
    val idFirestore: String,
    var originName: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OriginFE

        if (idFirestore != other.idFirestore) return false
        if (originName != other.originName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idFirestore.hashCode()
        result = 31 * result + originName.hashCode()
        return result
    }
}