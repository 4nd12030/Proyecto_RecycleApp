package com.seminariomanufacturadigital.recycleapp.routers

import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

//Clase(Interface) 2 para conectar el backend con la app
interface NotasRoutes {

    @POST("notas/create") //La url que se  necesita para crear una nuevo nota
    fun  create(
        @Body nota: Nota,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>


    @PUT("notas/update")
    fun update(
        @Body nota: Nota,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("notas/updateNotaEnviada")
    fun updateNotaEnviada(
        @Body nota: Nota,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("notas/updateNotaPendiente")
    fun updateNotaPendiente(
        @Body nota: Nota,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("notas/updateNotaAprobada")
    fun updateNotaAprobada(
        @Body nota: Nota,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("notas/updateNotaEntregada/{id}")
    fun updateNotaEntregada(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @GET("notas/getAll")
    fun getAll(
        @Header("Authorization") token: String
    ): Call<ArrayList<Nota>>

    @GET("notas/findByEmpleadoAndStatus/{num_empleado}/{status}")
    fun getNotasByEmpleadoAndStatus(
        @Path("num_empleado") num_empleado:  String,
        @Path("status") status: String,
        @Header("Authorization") token: String
    ):Call<ArrayList<Nota>>

    @GET("notas/findByAdministrativoAndStatus/{id_administrativo}/{status}")
    fun findByAdministrativoAndStatus(
        @Path("id_administrativo") id_administrativo:  String,
        @Path("status") status: String,
        @Header("Authorization") token: String
    ):Call<ArrayList<Nota>>

    @GET("notas/findByStatusAprobada/{id_operario}//{id_administrativo}/{status}")
    fun findByStatusAprobada(
        @Path("id_operario") id_adminid_operarioistrativo:  String,
        @Path("id_administrativo") id_administrativo:  String,
        @Path("status") status: String,
        @Header("Authorization") token: String
    ):Call<ArrayList<Nota>>

    @DELETE("notas/deleteNota/{id}")
    fun deleteNota(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @GET("notas/findByStatus/{status}")
    fun getNotasByStatus(
        @Path("status") status: String,
        @Header("Authorization") token: String
    ):Call<ArrayList<Nota>>


    @GET("notas/descargarArchivo/{id_operario}/{id_administrativo}/{status}")
    fun descargarArchivo(
        @Path("id_operario") id_adminid_operarioistrativo:  String,
        @Path("id_administrativo") id_administrativo:  String,
        @Path("status") status: String,
        @Header("Authorization") token: String
    ):Call<ResponseHttp>

}