package com.ops.opside.common.entities.firestore

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.Expose

data class ModulsTree(
    var moduls: MutableList<Modul> = mutableListOf(),
    @Transient
    var idFirebase: String = "",
    var nameRol: String = "",
    @Transient
    var numberRol: Int = 1
) {
    fun toJson(): String {
        return try {
            Gson().toJson(this)
        } catch (e: Exception) {
            ""
        }
    }

    fun fromJson(tree: String = ""): ModulsTree {
        return try {
            Gson().fromJson(tree,ModulsTree::class.java)
        } catch (e: Exception){
            Log.e("Error", e.message.toString())
            ModulsTree()
        }
    }

    fun hasPermission(modul: Moduls, permission: Permission): Boolean{
        val index = when (permission) {
            Permission.create -> 0
            Permission.read -> 1
            Permission.update -> 2
            Permission.delete -> 3
        }
        return this.moduls.filter {
            it.name == modul.name
        }[0].permissions[index]
    }

    fun getHashMap(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()
        map["tree"] = toJson()
        map["numberRol"] = numberRol
        return map
    }
}

data class Modul(var name: String, var access: Boolean, var permissions: MutableList<Boolean>)

enum class Permission{
    create,
    read,
    update,
    delete
}

enum class Moduls{
    Login,
    Registration,
    Concessionaire,
    DashBoard,
    Dealer,
    Incident,
    Market,
    Profile,
    TaxCollectionCrud,
    TaxCollection,
    ControlPanel,
    Permission
}