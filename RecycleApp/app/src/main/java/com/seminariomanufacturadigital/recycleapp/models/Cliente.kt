package com.seminariomanufacturadigital.recycleapp.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Cliente(
    @SerializedName("id") val id:Int? = null,
    @SerializedName("razon_social") var razon_social: String,
    @SerializedName("direccion") var direccion: String,
    @SerializedName("telefono") var telefono: String,
    @SerializedName("is_available") var is_available: String? = null,
    @SerializedName("fecha_desactivacion") var fecha_desactivacion: String? = null,
    ) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

//    override fun toString(): String {
//        return "Cliente(id=$id, razon_social='$razon_social', direccion='$direccion', telefono='$telefono', is_available=$is_available, fecha_desactivacion=$fecha_desactivacion)"
//    }

        override fun toString(): String {
        return razon_social
    }


}