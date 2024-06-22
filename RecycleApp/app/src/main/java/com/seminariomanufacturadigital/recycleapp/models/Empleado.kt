package com.seminariomanufacturadigital.recycleapp.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

//Clase 3 para conectar el backend con la app
class Empleado (
    //Apunta a la columna  que existeennuestra basede datos que correpsonde con el nombre
    @SerializedName("id") val id:String? = null,
    @SerializedName("no_empleado") val numEmpleado: String,
    @SerializedName("imagen") var image: String? = null,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("apellidopat") var apellidoPat: String,
    @SerializedName("apellidomat") var apellidoMat: String,
    @SerializedName("telefono") var telefono : String,
    @SerializedName("contrasena") val contrasena : String,
    //@SerializedName("rolEmpleado") val rolEmpleado : String,
    @SerializedName("session_token") val sessionToken: String?  = null,
    @SerializedName("is_available") val isAvailable: Boolean? = null,
    @SerializedName("fecha_desactivacion") val fechaDesactivacion: String? = null,
    @SerializedName("roles") val roles:  ArrayList<Rol>? = null
){

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Empleado(id=$id, numEmpleado='$numEmpleado', image=$image, nombre='$nombre', apellidoPat='$apellidoPat', apellidoMat='$apellidoMat', telefono='$telefono', contrasena='$contrasena', sessionToken=$sessionToken, isAvailable=$isAvailable, fechaDesactivacion=$fechaDesactivacion, roles=$roles)"
    }


}
