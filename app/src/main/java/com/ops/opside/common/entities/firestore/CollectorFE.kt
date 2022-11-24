package com.ops.opside.common.entities.firestore

data class CollectorFE(
    val idFirebase: String = "",
    var name: String = "",
    var imageURL: String = "",
    var address: String = "",
    var phone: String = "",
    var email: String ="",
    var role: Int = 0,
    var hasAccess: Boolean = false,
    var password: String = ""
){
    fun getHashMap(): MutableMap<String, Any>{

        val map: MutableMap<String, Any> = mutableMapOf()

        map["name"] = name
        map["imageURL"] = imageURL
        map["address"] = address
        map["phone"] = phone
        map["email"] = email
        map["role"] = role
        map["hasAccess"] = hasAccess
        map["password"] = password

        return map
    }

    fun parseToSE(): CollectorFE {
        return CollectorFE(
            idFirebase,
            name,
            imageURL,
            address,
            phone,
            email,
            role,
            hasAccess,
            password
        )
    }
}
