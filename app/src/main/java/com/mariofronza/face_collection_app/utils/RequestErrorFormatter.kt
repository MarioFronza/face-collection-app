package com.mariofronza.face_collection_app.utils

import org.json.JSONObject

object RequestErrorFormatter {

    fun formatErrorBody(errorBody: String): String {
        return try {
            val jObjError = JSONObject(errorBody)
            jObjError.getString("message")
        } catch (e: Exception) {
            "Erro na requisição"
        }
    }

}