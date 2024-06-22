package com.seminariomanufacturadigital.recycleapp.activities.administrativo.update

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.administrativo.home.AdministrativeHomeActivity
import com.seminariomanufacturadigital.recycleapp.activities.operario.home.OperarioHomeActivity
import com.seminariomanufacturadigital.recycleapp.models.Cliente
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.ClientesProviders
import com.seminariomanufacturadigital.recycleapp.providers.NotasProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdministrativoUpdateNotaActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var TAG = "AdministrativoUpdateNotaActivity"
    var nota: Nota? = null
    var nuevaNota: Nota? = null
    var gson = Gson()
    var notaProvider: NotasProviders? = null
    var sharedPref: SharedPref? =null
    var usuario: Empleado? =null
    var clienteProviders: ClientesProviders? = null

    var textViewIdNota: TextView? = null
    var textViewFecha: TextView? = null
    var textViewHora: TextView? = null
    var textViewOperarioNombre: TextView? = null
    var textViewOperarioId: TextView? = null
    var textViewPacas: TextView? = null
    var textViewPesoXPaca: TextView? = null
    var textViewPesoTotal: TextView? = null
    var textViewComentarios: TextView? = null

    var spinnerCliente: Spinner? =  null
    var textViewDireccion: TextView? = null
    var editTextCostoPorKilo: EditText? = null
    var imageViewCheck: ImageView? = null
    var textViewTotal: TextView? = null

    var imgViewEliminar: ImageView? = null
    var imgViewGuardar: ImageView? = null
    var imgViewBorrar: ImageView? = null
    var imgViewEnviar: ImageView? = null

    var costoTotal = 0.0
    var costoporKilo = 0.0
    var clientes = ArrayList<Cliente>()
    var idCliente = 0
    var direccion = ""
    var is_available: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrativo_update_nota)

        nota= gson.fromJson(intent.getStringExtra("nota"), Nota::class.java)
        Log.d("pASO 1", "${ nota }")

        sharedPref = SharedPref (this)
        getEmpleadoFromSesion()
        notaProvider  = NotasProviders(usuario?.sessionToken)
        clienteProviders = ClientesProviders(usuario?.sessionToken)

        toolbar =findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title = "Nota #${nota?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textViewIdNota = findViewById(R.id.num_orden)
        textViewFecha = findViewById(R.id.fecha_nota)
        textViewHora = findViewById(R.id.hora_nota)
        textViewOperarioNombre = findViewById(R.id.nombre_operario)
        textViewOperarioId= findViewById(R.id.id_empleado)
        textViewPacas = findViewById(R.id.num_pacas)
        textViewPesoXPaca = findViewById(R.id.peso_x_paca)
        textViewPesoTotal = findViewById(R.id.peso_total)
        textViewComentarios= findViewById(R.id.comentarios_operario)

        spinnerCliente =  findViewById(R.id.nombre_cliente)
        textViewDireccion = findViewById(R.id.direccion_cliente)
        editTextCostoPorKilo = findViewById(R.id.editText_costo_x_kilo)
        imageViewCheck = findViewById(R.id.imageview_check)
        textViewTotal = findViewById(R.id.costo_total)

        imgViewEliminar = findViewById(R.id.image_vew_eliminar)
        imgViewGuardar = findViewById(R.id.image_vew_guardar)
        imgViewBorrar= findViewById(R.id.image_view_borrar)
        imgViewEnviar= findViewById(R.id.image_view_aprobar)

        imgViewGuardar?.setOnClickListener { goToGuardarNota(buildNota()) }
        imgViewBorrar?.setOnClickListener { gotoReset() }
        imgViewEliminar?.setOnClickListener {  goToEliminarNota(nota?.id!!) }
        imgViewEnviar?.setOnClickListener { goToAprobarNota(buildNota()) }
        getValuesCampos()
        getToTotal()

        imageViewCheck?.setOnClickListener { getToTotal() }


    }

    private fun getToTotal() {
        costoporKilo =   editTextCostoPorKilo?.text.toString().toDouble()
        costoTotal = nota?.pesoTotal!! * costoporKilo
        textViewTotal?.text = "$${costoTotal}"
        Log.d("pASO 1", "${ costoporKilo }  ${ costoTotal }")

    }


    private fun getValuesCampos() {
        val fechaCompleta = "${nota?.fechaCreacion}"
        val ArrayFechaHora = fechaCompleta.split(" ")
        val fecha = ArrayFechaHora[0]
        val nuevoArray = fecha.split("-")
        val hora =ArrayFechaHora[1]
        val fechaFormato = " ${nuevoArray[2]}/${nuevoArray[1]}/${nuevoArray[0]} "
        getClientes()

        //val index =  clientes.indexOf(nota?.cliente)
        //val index =  0
        textViewFecha?.text = fechaFormato
        textViewHora?.text = hora
        textViewOperarioNombre?.text = "${nota?.operario?.nombre} ${nota?.operario?.apellidoPat} ${nota?.operario?.apellidoMat}"
        textViewOperarioId?.text = nota?.idOperario
        textViewPacas?.text = "${ nota?.numeroPacas }"
        textViewPesoXPaca?.text = "${ nota?.pesoPaca }"
        textViewPesoTotal?.text = "${nota?.pesoTotal}"
        textViewComentarios?.text= "${nota?.comentarios}"

        if(nota?.costoTotal == null){
            editTextCostoPorKilo?.setText("${costoporKilo }")
            textViewTotal?.text = "$${costoTotal}"
        } else {
            editTextCostoPorKilo?.setText("${nota?.costoKilo }")
            textViewTotal?.text = "$${nota?.costoTotal}"
        }

    }

    private fun getClientes(){

        clienteProviders?.getAll(is_available)?.enqueue(object : Callback<ArrayList<Cliente>>{
            override fun onResponse(
                call: Call<ArrayList<Cliente>>,
                response: Response<ArrayList<Cliente>>
            ) {
                if(response.body() != null){
                    Log.d(TAG, "Response:  ${response.body()}")
                    clientes = response.body()!!
                    val arrayAdapterClientes = ArrayAdapter<Cliente>(this@AdministrativoUpdateNotaActivity, android.R.layout.simple_dropdown_item_1line, clientes)
                    spinnerCliente?.adapter = arrayAdapterClientes

                    if(nota?.cliente != null){
                        for ((index, item) in clientes.withIndex()) {
                            if( nota?.cliente!!.razon_social == "${item}" ){
                                spinnerCliente?.setSelection(index)
                            }
                        }
                        textViewDireccion?.text = "${nota?.cliente?.direccion}"
                    }

                    spinnerCliente?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            idCliente = clientes[position].id!!
                            direccion = clientes[position].direccion
                            textViewDireccion?.text = direccion
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Cliente>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(this@AdministrativoUpdateNotaActivity, "Error ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }



    private  fun buildNota(): Nota?{

        Log.d("pASO 1", "${ costoporKilo }  ${ costoTotal }")
        nuevaNota = Nota(
            id = nota?.id,
            idOperario = nota?.idOperario,
            idAdministrativo = usuario?.numEmpleado,
            numeroPacas = nota?.numeroPacas!!,
            pesoPaca = nota?.pesoPaca!!,
            pesoTotal = nota?.pesoTotal!!,
            comentarios = nota?.comentarios,
            idCliente = idCliente,
            costoKilo = costoporKilo,
            costoTotal = costoTotal,
            estado = nota?.estado
        )

        Log.d(TAG,"pASO 1 ${nuevaNota}")

        return nuevaNota
    }

    private fun goToListNotas(){
        val i = Intent(this, AdministrativeHomeActivity::class.java)
        startActivity(i)
    }

    ////////////////////////////////////
    ///Botones provider
    private fun goToGuardarNota(nuevaNota: Nota?){
        Log.d(TAG, "Notaque se guardara: ${nuevaNota}")
        notaProvider?.updateNotaPendiente(nuevaNota!!)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(this@AdministrativoUpdateNotaActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@AdministrativoUpdateNotaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun gotoReset() {
        costoTotal = 0.0
        costoporKilo = 0.0

        spinnerCliente?.setSelection(0)
        editTextCostoPorKilo?.setText("${costoporKilo}")
        textViewTotal?.text =  "$${costoTotal}"
        //textViewPesoTotal?.text = ""
//        textViewComentarios?.setText("")
    }

    private fun goToEliminarNota(idNota: String){
        notaProvider?.deleteNota(idNota)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(this@AdministrativoUpdateNotaActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@AdministrativoUpdateNotaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun goToAprobarNota(nuevaNota: Nota?){
        Log.d(TAG, "Nota NUEVA:  $nuevaNota")
        notaProvider?.updateNotaAprobada(nuevaNota!!)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(this@AdministrativoUpdateNotaActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@AdministrativoUpdateNotaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }


    /////////////////////////////////////////////////////
    //// Empleado en sesion
    private fun getEmpleadoFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, " Empleado en fun getEmpleadoFromSesion: $usuario")
        }
    }




}
