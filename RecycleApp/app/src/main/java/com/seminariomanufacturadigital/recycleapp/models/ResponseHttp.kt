package com.seminariomanufacturadigital.recycleapp.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

//Clase 4 para conectar el backend con la app
class ResponseHttp(
    @SerializedName("message") val message: String,
    @SerializedName("succes") val isSucces: Boolean,
    @SerializedName("data") val data : JsonObject,
    @SerializedName("error") val error : String,
) {
    override fun toString(): String{
        return "ResponseHttp(message= '$message', isSucces=$isSucces, data=$data, error='$error')"
    }
}