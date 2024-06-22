package com.seminariomanufacturadigital.recycleapp.routers

import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
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
interface EmpleadosRoutes {
    @POST("empleados/create") //La url que se  necesita para crear un nuevo empleado
    fun  register(@Body empleado: Empleado
    ): Call<ResponseHttp>

    @FormUrlEncoded
    @POST("empleados/login")
    fun login(@Field("no_empleado") numEmpleadc: String,
              @Field("contrasena") contrasena:String
    ) :Call<ResponseHttp>

    @Multipart
    @PUT("empleados/update")
    fun   update(
        @Part image: MultipartBody.Part,
        @Part("empleado") empleado: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("empleados/updateWithoutImage")
    fun updateWithoutImage(
        @Body empleado: Empleado,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @GET("empleados/findByRol/{id_rol}")
    fun findByRol(
        @Path("id_rol") idRol: String,
        @Header("Authorization") token: String
    ): Call<ArrayList<Empleado>>

    @Multipart
    @POST("empleados/addUser/{id_rol}") //La url que se  necesita para crear un nuevo empleado
    fun  addUser(
        @Path("id_rol") idRol: String,
        @Part imagen: MultipartBody.Part,
        @Part("empleado") empleado: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @POST("empleados/addUserWithoutImage/{id_rol}") //La url que se  necesita para crear un nuevo empleado
    fun addUserWithoutImage(
        @Path("id_rol") idRol: String,
        @Body empleado: Empleado,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
    @GET("empleados/getAll/{is_available}")
    fun getAll(
        @Path("is_available") is_available:Boolean,
        @Header("Authorization") token: String
    ): Call<ArrayList<Empleado>>

    @PUT("empleados/desactivarEmpleado")
    fun desactivarEmpleado(
        @Body empleado: Empleado,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("empleados/reactivarEmpleado")
    fun reactivarEmpleado(
        @Body empleado: Empleado,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

}