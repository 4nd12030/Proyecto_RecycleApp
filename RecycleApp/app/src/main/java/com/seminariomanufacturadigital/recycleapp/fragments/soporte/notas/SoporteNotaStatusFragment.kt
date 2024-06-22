package com.seminariomanufacturadigital.recycleapp.fragments.soporte.notas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.adapters.NotasOperarioAdapters
import com.seminariomanufacturadigital.recycleapp.adapters.NotasSoporteAdapters
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.providers.NotasProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoporteNotaStatusFragment : Fragment() {

    var  myView: View? = null
    var TAG = "SoporteNotaStatusFragment"
    var recyclerViewNotas: RecyclerView? = null
    var adapter: NotasSoporteAdapters? = null
    var sharedPref:SharedPref? =null
    var status = ""
    val gson = Gson()
    var usuario: Empleado? = null
    var notaProvider: NotasProviders? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_soporte_nota_status, container, false)
        sharedPref = SharedPref(requireActivity())
        status = arguments?.getString("status")!!
        Log.d(TAG, "Estatus: ${status}")

        getUserFromSesion()
        notaProvider = NotasProviders(usuario?.sessionToken)

        recyclerViewNotas = myView?.findViewById(R.id.recyclerview_notas)
        recyclerViewNotas?.layoutManager = LinearLayoutManager(requireContext())

        getNotas()

        return myView
    }

    private fun getNotas() {
        Log.d(TAG, "Estatus get orden: ${status}")
        notaProvider?.getNotasByStatus(status)?.enqueue(object: Callback<ArrayList<Nota>> {
            override fun onResponse(
                call: Call<ArrayList<Nota>>,
                response: Response<ArrayList<Nota>>
            ) {
                if(response.body() != null){
                    val notas = response.body()
                    Log.d(TAG, "${notas}")
                    adapter = NotasSoporteAdapters(requireActivity(), notas!!)
                    recyclerViewNotas?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Nota>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getUserFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, "Usuario  en sesion: $usuario")
        }
    }

}