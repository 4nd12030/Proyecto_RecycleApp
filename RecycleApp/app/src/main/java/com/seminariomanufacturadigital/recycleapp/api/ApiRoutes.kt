package com.seminariomanufacturadigital.recycleapp.api

import com.seminariomanufacturadigital.recycleapp.routers.ClientesRoutes
import com.seminariomanufacturadigital.recycleapp.routers.EmpleadosRoutes
import com.seminariomanufacturadigital.recycleapp.routers.NotasRoutes
import com.seminariomanufacturadigital.recycleapp.routers.TiposRolesRoutes


//Clase 5 para conectar el backend con la app
class ApiRoutes {
    val API_URL = "http://192.168.100.92:3000/api/recycle/"
    val retrofit = RetrofitClient()

    fun getEmpleadosRoutes(): EmpleadosRoutes{
        return retrofit.getClient(API_URL).create(EmpleadosRoutes::class.java)
    }

    fun getEmpleadosRoutesGetToken(token: String): EmpleadosRoutes{
        return retrofit.getClientWebToken(API_URL, token).create(EmpleadosRoutes::class.java)
    }

    fun getTiposRolesRoutesGetToken(token: String): TiposRolesRoutes{
        return  retrofit.getClientWebToken(API_URL, token).create(TiposRolesRoutes::class.java)
    }

    fun getNotasRoutesGetToken(token: String):NotasRoutes{
        return retrofit.getClientWebToken(API_URL, token).create(NotasRoutes::class.java)
    }

    fun getClientesRoutesGetToken(token: String): ClientesRoutes{
        return retrofit.getClientWebToken(API_URL, token).create(ClientesRoutes::class.java)
    }

}