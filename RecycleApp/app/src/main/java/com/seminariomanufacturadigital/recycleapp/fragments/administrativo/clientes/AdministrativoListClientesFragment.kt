package com.seminariomanufacturadigital.recycleapp.fragments.administrativo.clientes

import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout
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
import com.seminariomanufacturadigital.recycleapp.activities.administrativo.add.AdministrativoNuevoClienteActivity
import com.seminariomanufacturadigital.recycleapp.activities.soporte.add.SoporteNuevoUsuarioActivity
import com.seminariomanufacturadigital.recycleapp.adapters.ClientesAdapter
import com.seminariomanufacturadigital.recycleapp.adapters.TiposRolesAdapter
import com.seminariomanufacturadigital.recycleapp.models.Cliente
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Rol
import com.seminariomanufacturadigital.recycleapp.providers.ClientesProviders
import com.seminariomanufacturadigital.recycleapp.providers.TiposRolesProvider
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdministrativoListClientesFragment : Fragment() {

    val gson = Gson()
    var TAG = "AdministrativoListClientesFragment"
    var myView: View? = null
    var recyclerViewClientes: RecyclerView? = null
    var clientesProvider: ClientesProviders? = null
    var empleado: Empleado? = null
    var adapter: ClientesAdapter? = null
    var sharedPref: SharedPref? = null
    var clientes = ArrayList<Cliente>()
    var toolbar: Toolbar? = null
    var is_available: Boolean = true
    var estado: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView =  inflater.inflate(R.layout.fragment_administrativo_list_clientes, container, false)
        setHasOptionsMenu(true) //habilita la opcion de menu en el toolbar

        //// CONFIGURACION DEL TOOLBAR
        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        nombreToolbar(is_available)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        ///CONFIGURACION DEL RECYCLERVIEW DE ROLES (LISTA DE ROLES)
        recyclerViewClientes = myView?.findViewById(R.id.recyclerview_clientes)
        recyclerViewClientes?.layoutManager = LinearLayoutManager(requireContext())

        sharedPref = SharedPref(requireActivity())
        getEmployeFromSession()

        clientesProvider = ClientesProviders(empleado?.sessionToken!!)
        Log.d(TAG,  "Token : ${empleado?.sessionToken!!}")

        getClientes(is_available)
        return myView
    }

    private fun nombreToolbar(is_available: Boolean){
        if(is_available){
            estado = "Activos"
        } else {
            estado = "Inactivos"
        }
        toolbar?.title = "Clientes ${estado}"
    }

    private fun  getClientes(is_available:Boolean){

        clientesProvider?.getAll(is_available)?.enqueue(object: Callback<ArrayList<Cliente>> {
            override fun onResponse(
                call: Call<ArrayList<Cliente>>,
                response: Response<ArrayList<Cliente>>
            ) {
                Log.d(TAG,  "Respuesta : ${response.body()}")
                if(response.body() != null){
                    clientes = response.body()!!
                    adapter = ClientesAdapter(requireActivity(), clientes, is_available)
                    recyclerViewClientes?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Cliente>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

    }


    /////////////////////
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_cliente, menu)
        Log.d(TAG, "Menu add")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==   R.id.item_add){
            goToAddCliente()
        }

        if(item.itemId ==   R.id.item_cliente_inactivos){
            is_available = false
            nombreToolbar( is_available)
            getClientes(is_available)
        }

        if(item.itemId ==   R.id.item_cliente_activos){
            is_available = true
            nombreToolbar( is_available)
            getClientes(is_available)
        }


        return super.onOptionsItemSelected(item)
    }

    private fun goToAddCliente() {
        val i = Intent(requireContext(), AdministrativoNuevoClienteActivity::class.java)
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