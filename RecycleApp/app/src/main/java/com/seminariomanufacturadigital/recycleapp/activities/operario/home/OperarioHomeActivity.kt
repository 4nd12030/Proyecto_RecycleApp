package com.seminariomanufacturadigital.recycleapp.activities.operario.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.fragments.operario.OperarioListNotasFragment
import com.seminariomanufacturadigital.recycleapp.fragments.operario.OperarioNewNotaFragment
import com.seminariomanufacturadigital.recycleapp.fragments.operario.OperarioProfileFragment
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref

class OperarioHomeActivity : AppCompatActivity() {
    private val TAG = "OperarioHomeActivity"
   // var buttonLogout: Button? = null
    var sharedPref : SharedPref? = null

    //Agregar el menu  de navegacion
    var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operario_home)

        sharedPref = SharedPref(this)

        ///Abre el fragment por defecto
        openFragment(OperarioListNotasFragment())
       // buttonLogout = findViewById(R.id.btn_cerrar_sesion)
       // buttonLogout?.setOnClickListener{ logout() }
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_list_notas -> {
                    openFragment(OperarioListNotasFragment())
                    true
                }
                R.id.item_new_orden -> {
                    openFragment(OperarioNewNotaFragment())
                    true
                }
                R.id.item_perfil -> {
                    openFragment(OperarioProfileFragment())
                    true
                }
                else -> false
            }
        }

        getEmpleadoFromSesion()
    }


    ///Funcion que recibe el frgamento que se mostrara en el contenedor creado en  el
    //  layput principal
    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getEmpleadoFromSesion() {
        val gson = Gson()
        //Log.d(TAG, "Antes de sentencia if")
        if(!sharedPref?.getData("empleado").isNullOrBlank()){
            //Si el usuario existe en sesion
            val employe = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, "Empleado: $employe")
        }
    }

}