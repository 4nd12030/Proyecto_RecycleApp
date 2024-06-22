package com.seminariomanufacturadigital.recycleapp.routers

import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.models.Rol
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface TiposRolesRoutes {

    @GET("tiposRoles/getAll")
    fun getAll(
        @Header("Authorization") token: String
    ): Call<ArrayList<Rol>>


    @Multipart
    @POST("tiposRoles/addRol") //La url que se  necesita para crear un nuevo empleado
    fun  addRol(
        @Part imagen: MultipartBody.Part,
        @Part("rol") rol: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}