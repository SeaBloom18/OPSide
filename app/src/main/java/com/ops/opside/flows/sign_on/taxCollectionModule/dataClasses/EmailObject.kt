package com.ops.opside.flows.sign_on.taxCollectionModule.dataClasses

data class EmailObject(
    val subject: String,
    val body: String,
    val recipient: String
) {
    fun getHashMap(): MutableMap<String, Any> {
        val mapMessage = mutableMapOf<String,String>()
        val map        = mutableMapOf<String,Any>()

        mapMessage["subject"] = subject
        mapMessage["text"]    = body

        map["to"]             = recipient
        map["message"]        = mapMessage

        return map
    }
}
