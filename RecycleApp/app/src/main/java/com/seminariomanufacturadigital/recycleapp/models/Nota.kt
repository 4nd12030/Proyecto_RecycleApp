package com.seminariomanufacturadigital.recycleapp.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Nota(
    @SerializedName("id") val id:String? = null,
    @SerializedName("id_operario") var idOperario: String? = null,
    @SerializedName("fecha_creacion") var fechaCreacion: String? = null,
    @SerializedName("numero_pacas") var numeroPacas:Int,
    @SerializedName("peso_paca") var pesoPaca: Int,
    @SerializedName("peso_total") var pesoTotal: Int? = null,
    @SerializedName("id_administrativo") var idAdministrativo: String? = null,
    @SerializedName("id_cliente") var idCliente: Int? = null,
    @SerializedName("costo_kilo") var costoKilo:Double? = null,
    @SerializedName("costo_total") var costoTotal: Double? = null,
    @SerializedName("comentarios") var comentarios: String? = null,
    @SerializedName("fecha_aprobacion") var fechaAprobacion: String? = null,
    @SerializedName("fecha_entrega") var fechaEntrega: String? = null,
    @SerializedName("estado") var estado: String? = null,
    @SerializedName("operario") val operario: Empleado? = null,
    @SerializedName("administrativo") val administrativo: Empleado? = null,
    @SerializedName("cliente") val cliente: Cliente? = null
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Nota(id=$id, idOperario=$idOperario, fechaCreacion=$fechaCreacion, numeroPacas=$numeroPacas, pesoPaca=$pesoPaca, pesoTotal=$pesoTotal, idAdministrativo=$idAdministrativo, idCliente=$idCliente, costoKilo=$costoKilo, costoTotal=$costoTotal, comentarios=$comentarios, fechaAprobacion=$fechaAprobacion, fechaEntrega=$fechaEntrega, estado=$estado, operario=$operario, administrativo=$administrativo, cliente=$cliente)"
    }


}