package com.seminariomanufacturadigital.recycleapp.providers

import com.seminariomanufacturadigital.recycleapp.api.ApiRoutes
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.models.Rol
import com.seminariomanufacturadigital.recycleapp.routers.TiposRolesRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class TiposRolesProvider(val token: String) {
    private var tiposRolesRoutes: TiposRolesRoutes? = null

    init{
        val api = ApiRoutes()
        tiposRolesRoutes = api.getTiposRolesRoutesGetToken(token)
    }

    fun getAll(): Call<ArrayList<Rol>>? {
        return tiposRolesRoutes?.getAll(token)
    }

    fun addRol(file: File, rol: Rol): Call<ResponseHttp>?{
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val imagen = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), rol.toJson())
        return tiposRolesRoutes?.addRol(imagen, requestBody,  token)
    }
}