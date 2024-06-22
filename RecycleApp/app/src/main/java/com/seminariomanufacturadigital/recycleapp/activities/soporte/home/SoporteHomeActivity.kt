package com.seminariomanufacturadigital.recycleapp.activities.soporte.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.fragments.operario.OperarioProfileFragment
import com.seminariomanufacturadigital.recycleapp.fragments.soporte.SoporteRolesUsuariosFragment
import com.seminariomanufacturadigital.recycleapp.fragments.soporte.SoporteClientesFragment
import com.seminariomanufacturadigital.recycleapp.fragments.soporte.notas.SoporteNotasFragment
import com.seminariomanufacturadigital.recycleapp.fragments.soporte.SoporteUsuariosFragment
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref

class SoporteHomeActivity : AppCompatActivity() {

    private  val TAG = "SoporteHomeActivity"
    var bottomNavigation: BottomNavigationView? = null
    var sharedPref: SharedPref? = null
    val gson = Gson()
    var empleado: Empleado? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soporte_home)

        getEmpleadoFromSesion()

        ///Abre el fragment por defecto
        openFragment(SoporteRolesUsuariosFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_perfil ->{
                    openFragment(OperarioProfileFragment())
                    true
                }
                R.id.item_list_ordenes ->{
                    openFragment(SoporteNotasFragment())
                    true
                }
                R.id.item_usuarios ->{
                    openFragment(SoporteUsuariosFragment())
                    true
                }
                R.id.item_roles_usuarios ->{
                    openFragment(SoporteRolesUsuariosFragment())
                    true
                }
                R.id.item_clientes ->{
                    openFragment(SoporteClientesFragment())
                    true
                }
                else -> false
            }
        }



    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getEmpleadoFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, " Empleado en fun getEmpleadoFromSesion: $empleado")
        }
    }


}