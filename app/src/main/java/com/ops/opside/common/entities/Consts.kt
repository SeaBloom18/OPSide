package com.ops.opside.common.entities

/** FIRESTORE **/
const val DB_TABLE_CONCESSIONAIRE = "concessionaire"
const val DB_TABLE_MARKET = "market"
const val DB_TABLE_FOREIGNER_ASSISTENCE = "foreignerAssistence"
const val DB_TABLE_INCIDENT_PERSON = "incidentPerson"
const val DB_TABLE_INCIDENT = "incident"
const val DB_TABLE_PARTICIPATING_CONCESS = "participatingConcess"
const val DB_TABLE_TAX_COLLECTION = "taxCollection"
const val DB_TABLE_EVENT = "event"
const val DB_TABLE_EMAIL_SENT = "emailSent"
const val DB_TABLE_PENDING_EMAIL = "pendingEmail"
const val DB_TABLE_RESOURCES = "resources"
const val DB_TABLE_ORIGIN = "origin"
const val DB_TABLE_COLLECTOR = "collector"
const val DB_TABLE_EMAIL = "email"
const val DB_TABLE_ROL = "rol"

/** SHARED PREFERENCES **/
const val SP_FOREIGN_CONCE_ROLE = 1
const val SP_NORMAL_CONCE_ROLE = 2
const val SP_COLLECTOR_ROLE = 3
const val SP_TAX_EXECUTOR_ROLE = 4
const val SP_SUPER_USER_ROLE = 5

/** PUT EXTRAS **/
const val PUT_EXTRA_LATITUDE = "latitude"
const val PUT_EXTRA_LONGITUDE = "longitude"
const val PUT_EXTRA_MARKET = "market"

/** Firebase Storage **/
const val PATH_COLLECTOR_REFERENCE = "opsUserProfile/CollectorsUserPhotos/"
const val PATH_CONCESSIONAIRE_REFERENCE = "opsUserProfile/ConcessionairesUserPhotos/"
const val LINK_FIREBASE_STORAGE = "gs://opss-fbd9e.appspot.com/"