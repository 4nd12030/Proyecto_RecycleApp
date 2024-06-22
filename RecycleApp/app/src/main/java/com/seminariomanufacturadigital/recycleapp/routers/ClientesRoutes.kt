package com.seminariomanufacturadigital.recycleapp.routers

import com.seminariomanufacturadigital.recycleapp.models.Cliente
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.models.Rol
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

//Clase(Interface) 2 para conectar el backend con la app
interface ClientesRoutes {

    @GET("clientes/getAll/{is_available}")
    fun getAll(
        @Path("is_available") is_available: Boolean,
        @Header("Authorization") token: String
    ): Call<ArrayList<Cliente>>

    @PUT("clientes/update")
    fun update(
        @Body cliente: Cliente,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @POST("clientes/addCliente") //La url que se  necesita para crear un nuevo empleado
    fun  addCliente(
        @Body cliente: Cliente,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("clientes/desactivarCliente")
    fun desactivarCliente(
        @Body cliente: Cliente,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("clientes/reactivarCliente")
    fun reactivarCliente(
        @Body cliente: Cliente,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>


}