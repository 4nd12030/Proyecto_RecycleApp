package com.seminariomanufacturadigital.recycleapp.fragments.soporte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.soporte.add.SoporteNuevoRolActivity
import com.seminariomanufacturadigital.recycleapp.adapters.TiposRolesAdapter
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Rol
import com.seminariomanufacturadigital.recycleapp.providers.TiposRolesProvider
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoporteRolesUsuariosFragment : Fragment() {

    val gson = Gson()
    var TAG = "SoporteRolesUsuariosFragment"
    var myView: View? = null
    var recyclerViewRolesUsuario: RecyclerView? = null
    var tiposRolesProvider: TiposRolesProvider? = null
    var empleado: Empleado? = null
    var adapter: TiposRolesAdapter? = null
    var sharedPref: SharedPref? = null
    var roles = ArrayList<Rol>()
    var toolbar: Toolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment/
        myView = inflater.inflate(R.layout.fragment_soporte_roles_usuarios, container, false)
        Log.d(TAG, "Entraste a ${TAG} y este es el fragment: ${myView}")
        setHasOptionsMenu(true)

       //// CONFIGURACION DEL TOOLBAR
        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        toolbar?.title = "Roles de la aplicación"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        ///CONFIGURACION DEL RECYCLERVIEW DE ROLES (LISTA DE ROLES)
        recyclerViewRolesUsuario = myView?.findViewById(R.id.recyclerview_roles)
        recyclerViewRolesUsuario?.layoutManager = LinearLayoutManager(requireContext())

        sharedPref = SharedPref(requireActivity())
        getEmployeFromSession()

        tiposRolesProvider = TiposRolesProvider(empleado?.sessionToken!!)
        Log.d(TAG,  "Token : ${empleado?.sessionToken!!}")

        getTiposRoles()
        return myView
    }
    /////////////////////
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_rol, menu)
        Log.d(TAG, "Menu add")
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==   R.id.item_add){
            goToAddPerfil()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToAddPerfil() {
        val i = Intent(requireContext(), SoporteNuevoRolActivity::class.java)
        startActivity(i)
    }
//////////////////////////////////////////////

    private fun  getTiposRoles(){
        Log.d(TAG,  "getTiposRoles() : ${tiposRolesProvider}")
        tiposRolesProvider?.getAll()?.enqueue(object: Callback<ArrayList<Rol>> {
            override fun onResponse(
                call: Call<ArrayList<Rol>>,
                response: Response<ArrayList<Rol>>
            ) {
                Log.d(TAG,  "Respuesta : ${response.body()}")
                if(response.body() != null){
                    roles = response.body()!!
                    adapter = TiposRolesAdapter(requireActivity(), roles)
                    recyclerViewRolesUsuario?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Rol>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun getEmployeFromSession(){
        if(!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si le usuario existe en sesion
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            //Log.d(TAG, "Empleado: $empleado")
        }
    }

  }