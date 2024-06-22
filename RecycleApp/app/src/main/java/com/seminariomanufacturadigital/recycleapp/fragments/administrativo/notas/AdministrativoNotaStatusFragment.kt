package com.seminariomanufacturadigital.recycleapp.fragments.administrativo.notas

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
import com.seminariomanufacturadigital.recycleapp.adapters.NotasAdministrativoAdapters
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.providers.NotasProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdministrativoNotaStatusFragment : Fragment() {

    var  myView: View? = null
    var TAG = "AdministrativoNotaStatusFragment"
    var recyclerViewNotas: RecyclerView? = null
    var adapter: NotasAdministrativoAdapters? = null
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
        myView= inflater.inflate(R.layout.fragment_operario_nota_status, container, false)
        sharedPref = SharedPref(requireActivity())
        status = arguments?.getString("status")!!
//        if(status == "NUEVA"){
//            status = "ENVIADA"
//        }
        Log.d(TAG, "Estatus: ${status}")

        getUserFromSesion()
        notaProvider = NotasProviders(usuario?.sessionToken)

        recyclerViewNotas = myView?.findViewById(R.id.recyclerview_notas)
        recyclerViewNotas?.layoutManager = LinearLayoutManager(requireContext())

        getNotas()

        return myView
    }

    private fun getNotas() {
        Log.d(TAG, "Estatus get Notas: ${status}")
        Log.d(TAG, "Administrativo get Notas: ${usuario?.numEmpleado!!}")
        if(status == "ENVIADA"){
            notaProvider?.getNotasByStatus(status)?.enqueue(object: Callback<ArrayList<Nota>> {
                override fun onResponse( call: Call<ArrayList<Nota>>,response: Response<ArrayList<Nota>> ) {
                    if(response.body() != null){
                        val notas = response.body()
                        adapter = NotasAdministrativoAdapters(requireActivity(), notas!!)
                        recyclerViewNotas?.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<ArrayList<Nota>>, t: Throwable) {
                    Toast.makeText(requireActivity(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }
        else {
            Log.d(TAG, "estatus else: ${status}")
            Log.d(TAG, "Administrativo else: ${usuario?.numEmpleado!!}")
            notaProvider?.findByAdministrativoAndStatus(usuario?.numEmpleado!! ,status)?.enqueue(object: Callback<ArrayList<Nota>> {
                override fun onResponse(call: Call<ArrayList<Nota>>, response: Response<ArrayList<Nota>>) {
                    if(response.body() != null){
                        val notas = response.body()
                        Log.d(TAG, "NOTAS PENDIENTES else: ${notas}")
                        adapter = NotasAdministrativoAdapters(requireActivity(), notas!!)
                        recyclerViewNotas?.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<ArrayList<Nota>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }


    }

    private fun getUserFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, "Usuario  en sesion: $usuario")
        }
    }

}