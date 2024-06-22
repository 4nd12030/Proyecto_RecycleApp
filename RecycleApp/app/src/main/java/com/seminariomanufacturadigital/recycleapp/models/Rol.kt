package com.seminariomanufacturadigital.recycleapp.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Rol(
    @SerializedName("id") val id:String? = null,
    @SerializedName("imagen") var imagen: String? = null,
    @SerializedName("nombre") var nombre: String
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return nombre
    }
}