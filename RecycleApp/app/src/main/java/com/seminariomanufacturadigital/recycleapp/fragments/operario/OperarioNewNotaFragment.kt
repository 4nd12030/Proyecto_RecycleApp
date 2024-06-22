package com.seminariomanufacturadigital.recycleapp.fragments.operario

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.operario.home.OperarioHomeActivity
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.NotasProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OperarioNewNotaFragment : Fragment() {

    var  TAG ="OperarioNewNotaFragment"
    var myView: View? = null
    var sharedPref: SharedPref? = null
    var empleado: Empleado? =null
    var gson = Gson()
    var notaProvider: NotasProviders?  =null

    var txtViewIdEmpleado: TextView? = null
    var txtViewNoOrden: TextView? = null
    var txtViewHora: TextView? = null
    var txtViewFecha: TextView? = null
    var txtViewNombreRealizo: TextView? = null

    var spinnerNoPacas: Spinner? = null
    var spinnerPesoxPacas: Spinner? = null
    var txtViewPesoTotal: TextView? = null
    var editTextComentario: EditText? = null

    var spinnerCliente: Spinner? = null
    var txtViewCostoxPaca: TextView? = null
    var txtViewCostoTotal: TextView? = null

    var buttonGuardar: Button? = null
    var buttonEnviar: Button? = null
    var cantidad = 0
    var kilos = 0
    var valuesNumPacas = ArrayList<Int>()
    var valuesPesoPacasKilos = ArrayList<Int>()
    var valuesPesoPacasGramos = ArrayList<Int>()

    var cantidadPiezas = 0
    var cantidadKilos = 0
    var pesoTotal = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_operario_new_nota, container, false)
        sharedPref = SharedPref(requireActivity())
        getEmpleadoFromSesion()
        notaProvider = NotasProviders(empleado?.sessionToken!!)

        txtViewIdEmpleado  = myView?.findViewById(R.id.id_empleado)
        txtViewNoOrden = myView?.findViewById(R.id.num_orden)
        txtViewHora = myView?.findViewById(R.id.hora_nota)
        txtViewFecha = myView?.findViewById(R.id.fecha_nota)
        txtViewNombreRealizo = myView?.findViewById(R.id.nombre_operario)

        spinnerNoPacas = myView?.findViewById(R.id.num_pacas)
        spinnerPesoxPacas = myView?.findViewById(R.id.peso_x_paca)
        txtViewPesoTotal = myView?.findViewById(R.id.peso_total)
        editTextComentario = myView?.findViewById(R.id.comentarios_operario)

        spinnerCliente = myView?.findViewById(R.id.nombre_cliente)
        txtViewCostoxPaca = myView?.findViewById(R.id.costo_x_kilo)
        txtViewCostoTotal = myView?.findViewById(R.id.costo_total)
        buttonGuardar = myView?.findViewById(R.id.btn_guardar)
        buttonEnviar = myView?.findViewById(R.id.btn_enviar)


        txtViewNombreRealizo?.text = "${empleado?.nombre} ${empleado?.apellidoPat} ${empleado?.apellidoMat}"
        txtViewIdEmpleado?.text = empleado?.numEmpleado
        Log.d(TAG, "Nombre empleado:  ${txtViewNombreRealizo?.text}")
        getValuesSpinner()
//        Log.d(TAG, "Cantidad piezas:  ${cantidadPiezas}")
//        Log.d(TAG, "Cantidad kilos:  ${cantidadKilos}")

        buttonGuardar?.setOnClickListener { guardarNuevaNota() }
        buttonEnviar?.setOnClickListener{ enviarNuevaNota()}
        return myView
    }

    private fun goToListNotas(){
        val i = Intent(requireContext(), OperarioHomeActivity::class.java)
        startActivity(i)
    }

    private fun guardarNuevaNota(){

        var comentarios =  editTextComentario?.text.toString()

        val nuevaNota = Nota(
            idOperario = empleado?.numEmpleado,
            numeroPacas = cantidadPiezas,
            pesoPaca = cantidadKilos,
            pesoTotal = pesoTotal,
            comentarios = comentarios
        )

        Log.d(TAG,"Comentario ${comentarios}")

        Log.d(TAG,"Orden nueva ${nuevaNota}")

        notaProvider?.create(nuevaNota)?.enqueue(object : Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(requireContext(), "${response.body()?.message}",Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }


    private fun enviarNuevaNota(){

        var comentarios =  editTextComentario?.text.toString()

        val nuevaNota = Nota(
            idOperario = empleado?.numEmpleado,
            numeroPacas = cantidadPiezas,
            pesoPaca = cantidadKilos,
            pesoTotal = pesoTotal,
            comentarios = comentarios,
            estado = "ENVIADA"
        )

        Log.d(TAG,"Comentario ${comentarios}")

        Log.d(TAG,"Orden nueva ${nuevaNota}")

        notaProvider?.create(nuevaNota)?.enqueue(object : Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(requireContext(), "${response.body()?.message}",Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }



    private fun gotoReset() {
        txtViewPesoTotal?.text = ""
        editTextComentario?.setText("")
    }


    private fun getPesoTotal(){
        pesoTotal = cantidadPiezas * cantidadKilos
        txtViewPesoTotal?.text = "${pesoTotal}"
        Log.d(TAG, "Cantidad total:  ${pesoTotal}")
    }
    private fun getValuesSpinner(){
        for(i in 0..100){
            valuesNumPacas.add(i)
            valuesPesoPacasKilos.add(i)
        }

//        for(i in 100 .. 900){
//            valuesPesoPacasGramos.add(i)
//        }

        val arrayAdapterPiezas = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, valuesNumPacas)
        spinnerNoPacas?.adapter = arrayAdapterPiezas
        spinnerNoPacas?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cantidadPiezas = valuesNumPacas[position]
                getPesoTotal()
                Log.d(TAG,  "Cantidad piezas seleccionadas : $cantidadPiezas")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val arrayAdapterKilos = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, valuesPesoPacasKilos)
        spinnerPesoxPacas?.adapter = arrayAdapterKilos
        spinnerPesoxPacas?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cantidadKilos = valuesPesoPacasKilos[position]
                getPesoTotal()
                Log.d(TAG,  "Cantidad kilos seleccionados : $cantidadKilos")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }



    private fun getEmpleadoFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, " Empleado en fun getEmpleadoFromSesion: $empleado")
        }
    }




}

