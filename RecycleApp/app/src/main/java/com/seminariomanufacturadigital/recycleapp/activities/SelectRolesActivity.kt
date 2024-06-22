package com.seminariomanufacturadigital.recycleapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.adapters.RolesAdapter
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref

class SelectRolesActivity : AppCompatActivity() {

    var TAG = "SelectRolesActivity"

    var recyclerViewRoles : RecyclerView? = null
    var empleado: Empleado? = null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_roles)

        recyclerViewRoles = findViewById(R.id.recyclerview_roles)
        recyclerViewRoles?.layoutManager = LinearLayoutManager(this)

        getEmployeFromSession()
        Log.d(TAG, "Empleado $empleado")

        adapter = RolesAdapter(this, empleado?.roles!!)
        Log.d(TAG, "Roles Empleado ${empleado?.roles}")
        recyclerViewRoles?.adapter = adapter
    }

    private fun getEmployeFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("empleado").isNullOrBlank()) {
            //Si le usuario existe en sesion
            empleado = gson.fromJson(sharedPref.getData("empleado"), Empleado::class.java)
            //Log.d(TAG, "Empleado: $empleado")
        }
    }
}