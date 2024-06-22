package com.seminariomanufacturadigital.recycleapp.providers

import com.seminariomanufacturadigital.recycleapp.api.ApiRoutes
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.routers.NotasRoutes
import retrofit2.Call

//Clase 6 para conectar el backend con la app
class NotasProviders(val token:String? = null) {

    private var notasRoutesToken: NotasRoutes? = null
    val TAG = "NotasProviders"

    init {
        val api = ApiRoutes()
        if(token!= null){
            notasRoutesToken = api.getNotasRoutesGetToken(token)
        }
    }

    fun create(nota: Nota,): Call<ResponseHttp>?{
        return notasRoutesToken?.create( nota, token!!)
    }

    fun update( nota: Nota): Call<ResponseHttp>? {
        return notasRoutesToken?.update(nota, token!!)
    }
    fun updateNotaEnviada( nota: Nota): Call<ResponseHttp>? {
        return notasRoutesToken?.updateNotaEnviada(nota, token!!)
    }
    fun updateNotaPendiente( nota: Nota): Call<ResponseHttp>? {
        return notasRoutesToken?.updateNotaPendiente(nota, token!!)
    }
    fun updateNotaAprobada( nota: Nota): Call<ResponseHttp>? {
        return notasRoutesToken?.updateNotaAprobada(nota, token!!)
    }
    fun updateNotaEntregada( idNota: String): Call<ResponseHttp>? {
        return notasRoutesToken?.updateNotaEntregada(idNota, token!!)
    }
    fun findByStatusAprobada(id_operario:String, id_administrativo:String, status:String,):  Call<ArrayList<Nota>>?  {
        return notasRoutesToken?.findByStatusAprobada(id_operario, id_administrativo, status, token!!)
    }

    fun getNotasByEmpleadoAndStatus(num_empleado:String, status:String): Call<ArrayList<Nota>>? {
        return notasRoutesToken?.getNotasByEmpleadoAndStatus(num_empleado, status,token!!)
    }

    fun findByAdministrativoAndStatus(id_administrativo:String, status:String): Call<ArrayList<Nota>>? {
        return notasRoutesToken?.findByAdministrativoAndStatus(id_administrativo, status,token!!)
    }

    fun deleteNota( idNota: String):  Call<ResponseHttp>? {
        return notasRoutesToken?.deleteNota(idNota, token!!)
    }

    fun getNotasByStatus(status:String): Call<ArrayList<Nota>>? {
        return notasRoutesToken?.getNotasByStatus(status,token!!)
    }

    fun descargarArchivo(id_operario:String, id_administrativo:String, status:String,):  Call<ResponseHttp>?  {
        return notasRoutesToken?.descargarArchivo(id_operario, id_administrativo, status, token!!)
    }


//    fun findByStatus(status: String): Call<ArrayList<Nota>>? {
//        return  notasRoutesToken?.findByRol(status, token!!)
//    }


    fun getAll(): Call<ArrayList<Nota>>? {
        //Log.d(TAG, "EmpleadosProvider-findByRol: ${empleadosRoutes?.findByRol(idRol, token!!)}")
        return  notasRoutesToken?.getAll(token!!)
    }
}