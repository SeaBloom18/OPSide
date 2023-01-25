package com.ops.opside.common.utils

import com.ops.opside.common.entities.firestore.Moduls
import com.ops.opside.common.entities.firestore.ModulsTree
import com.ops.opside.common.entities.firestore.Permission

class Manager {
    companion object {
        @Volatile
        private var TREE: ModulsTree? = null
        fun getInstance(tree:String = ""): ModulsTree =
            TREE ?: synchronized(this) {
                TREE ?: ModulsTree().fromJson(tree).also { TREE = it }
            }
    }

    fun verify(modul: Moduls, permission: Permission): Boolean{
        return getInstance().hasPermission(modul,permission)
    }
}
