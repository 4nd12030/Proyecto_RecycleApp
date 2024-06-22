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
import com.seminariomanufacturadigital.recycleapp.activities.soporte.add.SoporteNuevoUsuarioActivity
import com.seminariomanufacturadigital.recycleapp.adapters.ListaUsuariosAdapter
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoporteUsuariosFragment : Fragment() {

    var TAG = "SoporteUsuariosFragment"
    var myView: View? = null
    var gson = Gson()
    var sharedPref: SharedPref? = null
    var toolbar: Toolbar? = null
    var recyclerViewListaUsuarios: RecyclerView? = null
    var listaUsuariosProvider: EmpleadosProviders? = null
    var empleado: Empleado? = null
    var adapter: ListaUsuariosAdapter? = null
    var usuarios = ArrayList<Empleado>()

    var is_available: Boolean = true
    var estado: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_soporte_usuarios, container, false)
        setHasOptionsMenu(true)

        //// CONFIGURACION DEL TOOLBAR
        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        nombreToolbar(is_available)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        recyclerViewListaUsuarios = myView?.findViewById(R.id.recyclerview_lista_usuarios)
        recyclerViewListaUsuarios?.layoutManager = LinearLayoutManager(requireContext())

        sharedPref = SharedPref(requireActivity())
        getEmployeFromSession()

        listaUsuariosProvider = EmpleadosProviders(empleado?.sessionToken)

        getListaUsuarios(is_available)

        return myView
    }


    private fun nombreToolbar(is_available: Boolean){
//        estado = if(is_available){
//            "Activos"
//        } else {
//            "Inactivos"
//        }
        estado = when(is_available) {
            false  ->  "Inactivos"
            else -> "Activos"
        }
        toolbar?.title = "Usuarios ${estado}"
    }

    private fun getListaUsuarios(is_available:Boolean) {
        listaUsuariosProvider?.getAll(is_available)?.enqueue(object : Callback<ArrayList<Empleado>> {
            override fun onResponse(
                call: Call<ArrayList<Empleado>>,
                response: Response<ArrayList<Empleado>>
            ) {
                if(response.body() != null){
                    usuarios = response.body()!!
                    adapter = ListaUsuariosAdapter(requireActivity(), usuarios, is_available)
                    recyclerViewListaUsuarios?.adapter = adapter

                }
            }

            override fun onFailure(call: Call<ArrayList<Empleado>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

    }

    /////////////////////
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_user, menu)
        Log.d(TAG, "Menu add")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_add -> goToAddUsuario()
            R.id.item_user_inactivos -> {
                is_available = false
                nombreToolbar(is_available)
                getListaUsuarios(is_available)
            }
            R.id.item_user_activos -> {
                is_available = true
                nombreToolbar(is_available)
                getListaUsuarios(is_available)
            }
        }
//        if(item.itemId ==   R.id.item_add){
//            goToAddUsuario()
//        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToAddUsuario() {
        val i = Intent(requireContext(), SoporteNuevoUsuarioActivity::class.java)
        startActivity(i)
    }
//////////////////////////////////////////////



    private fun getEmployeFromSession(){
        if(!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si le usuario existe en sesion
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            //Log.d(TAG, "Empleado: $empleado")
        }
    }

}