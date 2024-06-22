package com.seminariomanufacturadigital.recycleapp.providers

import android.util.Log
import com.seminariomanufacturadigital.recycleapp.api.ApiRoutes
import com.seminariomanufacturadigital.recycleapp.models.Cliente
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.models.Rol
import com.seminariomanufacturadigital.recycleapp.routers.ClientesRoutes
import com.seminariomanufacturadigital.recycleapp.routers.EmpleadosRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

//Clase 6 para conectar el backend con la app
class ClientesProviders(val token:String? = null) {
    private var clientesRoutesToken: ClientesRoutes? = null
    val TAG = "ClientesProviders"

    init {
        val api = ApiRoutes()
            clientesRoutesToken = api.getClientesRoutesGetToken(token!!)
    }


    fun update( cliente: Cliente): Call<ResponseHttp>? {
        return clientesRoutesToken?.update(cliente, token!!)
    }


    fun addCliente(  cliente: Cliente): Call<ResponseHttp>?{
        return clientesRoutesToken?.addCliente( cliente, token!!)
    }

    fun getAll(is_available: Boolean): Call<ArrayList<Cliente>>? {
        return clientesRoutesToken?.getAll(is_available, token!!)
    }

    fun desactivarCliente(cliente: Cliente): Call<ResponseHttp>?{
        return clientesRoutesToken?.desactivarCliente( cliente,  token!!)
    }

    fun reactivarCliente(cliente: Cliente): Call<ResponseHttp>?{
        return clientesRoutesToken?.reactivarCliente( cliente,  token!!)
    }


}