package com.seminariomanufacturadigital.recycleapp.activities.soporte.listas.usuarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.adapters.UsuariosAdapter
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoporteListaUsuariosActivity : AppCompatActivity() {

    var TAG = "SoporteListaUsuariosActivity"
    val gson = Gson()
    var recyclerViewUsuarios: RecyclerView? = null
    var adapter: UsuariosAdapter? = null
    var usuariosProviders: EmpleadosProviders? = null
    var empleado: Empleado? = null
    var  usuarios: ArrayList<Empleado> = ArrayList()
    var toolbar: Toolbar? = null
    var sharedPref: SharedPref? = null

    var idRol: String? = null
    var nombreRol: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soporte_lista_usuarios)

        idRol = intent.getStringExtra("idRol")
        Log.d(TAG, "Id Rol: ${idRol}")
        nombreRol = intent.getStringExtra("nombreRol")
        nombreRol = nombreRol?.toLowerCase()
        Log.d(TAG, "Nombre Rol: ${nombreRol}")

        recyclerViewUsuarios = findViewById(R.id.recyclerview_usuarios_rol)
        //recyclerViewUsuarios?.layoutManager = GridLayoutManager(this, 2)
        recyclerViewUsuarios?.layoutManager = LinearLayoutManager(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title = "Lista de usuarios ${nombreRol}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //habilita la flecha hacia atras


        sharedPref = SharedPref(this)
        getEmpleadoFromSesion()
        usuariosProviders = EmpleadosProviders(empleado?.sessionToken)

        getUsuarios()
    }



    private fun getUsuarios(){
        usuariosProviders?.findByRol(idRol!!)?.enqueue(object: Callback<ArrayList<Empleado>> {
            override fun onResponse(
                call: Call<ArrayList<Empleado>>,
                response: Response<ArrayList<Empleado>>
            ) {
                Log.d(TAG, "Response: ${ response.body() }")
                if(response.body() != null){
                    usuarios = response.body()!!
                    adapter = UsuariosAdapter(this@SoporteListaUsuariosActivity, usuarios)
                    recyclerViewUsuarios?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Empleado>>, t: Throwable) {
                Toast.makeText(this@SoporteListaUsuariosActivity, t.message, Toast.LENGTH_LONG).show()
                Log.d(TAG,"Error: ${t.message}")
            }

        })
    }



    private fun getEmpleadoFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            //Log.d(TAG, " Empleado en fun getEmpleadoFromSesion: $empleado")
        }
    }
}