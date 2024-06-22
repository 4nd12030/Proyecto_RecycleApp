package com.seminariomanufacturadigital.recycleapp.activities.operario.update

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.operario.home.OperarioHomeActivity
import com.seminariomanufacturadigital.recycleapp.models.Cliente
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.NotasProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OperarioUpdateNotaActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var TAG = "OperarioUpdateNotaActivity"
    var nota: Nota? = null
    var nuevaNota: Nota? = null
    var gson = Gson()
    var notaProvider: NotasProviders? = null
    var sharedPref: SharedPref? =null
    var usuario: Empleado? =null

    var textViewIdNota: TextView? = null
    var textViewOperarioNombre: TextView? = null
    var textViewtextViewOperarioId: TextView? = null
    var spinnerPesoXPaca: Spinner? = null
    var spinnerNumPacas: Spinner? = null
    var textViewPesoTotal: TextView? = null
    var editTextComentarios: EditText? = null

    var imageViewGuardar: ImageView? = null
    var imageViewEliminar: ImageView? = null
    var imageViewEnviar: ImageView? = null
    var imageViewBorrar: ImageView? = null

    var arrayNumerosPacas = ArrayList<Int>()
    var arrayKilosPacas = ArrayList<Int>()

    var cantidadPiezas = 0
    var cantidadKilos = 0
    var pesoTotal = 0
    var costoTotal = 0.0
    var costoKilo = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operario_update_nota)

        nota= gson.fromJson(intent.getStringExtra("nota"), Nota::class.java)
        Log.d(TAG, "${ nota }")

        sharedPref = SharedPref (this)
        getEmpleadoFromSesion()
        notaProvider  = NotasProviders(usuario?.sessionToken)

        toolbar =findViewById(R.id.toolbar)
        toolbar?.title = "Nota #${nota?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textViewIdNota = findViewById(R.id.num_orden)
        textViewOperarioNombre = findViewById(R.id.nombre_operario)
        textViewtextViewOperarioId= findViewById(R.id.id_empleado)
        spinnerPesoXPaca = findViewById(R.id.peso_x_paca)
        spinnerNumPacas = findViewById(R.id.num_pacas)
        textViewPesoTotal = findViewById(R.id.peso_total)

        editTextComentarios= findViewById(R.id.comentarios_operario)

        imageViewEliminar = findViewById(R.id.image_vew_eliminar)
        imageViewGuardar= findViewById(R.id.image_vew_guardar)
        imageViewEnviar= findViewById(R.id.image_vew_enviar)
        imageViewBorrar= findViewById(R.id.image_vew_borrar)

        getValuesCampos()
        imageViewGuardar?.setOnClickListener { goToGuardarNota(buildNota()) }
        imageViewEliminar?.setOnClickListener { goToEliminarNota("${nota?.id}") }
        imageViewEnviar?.setOnClickListener { goToEnviarNota(buildNota()) }
        imageViewBorrar?.setOnClickListener { goToReset() }


    }

    private fun getValuesCampos() {
        textViewOperarioNombre?.text = "${nota?.operario?.nombre} ${nota?.operario?.apellidoPat} ${nota?.operario?.apellidoMat}"
        textViewtextViewOperarioId?.text = nota?.idOperario
        editTextComentarios?.setText("${nota?.comentarios}")
        getSpinnerValues()
        spinnerNumPacas?.setSelection(nota!!.numeroPacas)
        spinnerPesoXPaca?.setSelection(nota!!.pesoPaca)

        Log.d(TAG, "Nombre empleado:  ${textViewOperarioNombre?.text}")
    }

    private fun getPesoTotal(){
        pesoTotal = cantidadPiezas * cantidadKilos
        textViewPesoTotal?.text = "${pesoTotal}"
        Log.d(TAG, "Cantidad total:  ${pesoTotal}")
    }

    private fun getSpinnerValues() {

        for(i in 0 .. 100) {
            arrayNumerosPacas.add(i)
            arrayKilosPacas.add(i)
        }

        val arrayAdapterNumPacas = ArrayAdapter(this@OperarioUpdateNotaActivity,android.R.layout.simple_dropdown_item_1line, arrayNumerosPacas)
        spinnerNumPacas?.adapter = arrayAdapterNumPacas
        spinnerNumPacas?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cantidadPiezas = arrayNumerosPacas[position]
                getPesoTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val arrayAdapterKilosPacas = ArrayAdapter(this@OperarioUpdateNotaActivity,android.R.layout.simple_dropdown_item_1line, arrayKilosPacas)
        spinnerPesoXPaca?.adapter = arrayAdapterKilosPacas
        spinnerPesoXPaca?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cantidadKilos = arrayKilosPacas[position]
                getPesoTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }


    private  fun buildNota(): Nota?{
        var comentarios = editTextComentarios?.text.toString()
        nuevaNota = Nota(
            id = nota?.id,
            idOperario = nota?.idOperario,
            numeroPacas = cantidadPiezas,
            pesoPaca = cantidadKilos,
            pesoTotal = pesoTotal,
            comentarios = comentarios,
            estado = nota?.estado
            )

        Log.d(TAG,"Orden nueva ${nuevaNota}")
        Log.d(TAG,"Coemntario nuevo ${editTextComentarios?.text}")

        return nuevaNota
    }

    private fun goToListNotas(){
        val i = Intent(this, OperarioHomeActivity::class.java)
        startActivity(i)
    }

    ////////////////////////////////////
    ///Acciones Botones

    private fun goToReset() {
        spinnerPesoXPaca?.setSelection(0)
        spinnerNumPacas?.setSelection(0)
        editTextComentarios?.setText("")
    }


    private fun goToGuardarNota(nuevaNota: Nota?){
        Log.d(TAG, "Nota que se guardara: ${nuevaNota}")
        notaProvider?.update(nuevaNota!!)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(this@OperarioUpdateNotaActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@OperarioUpdateNotaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun goToEliminarNota(idNota: String){
        notaProvider?.deleteNota(idNota)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(this@OperarioUpdateNotaActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@OperarioUpdateNotaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun goToEnviarNota(nuevaNota: Nota?){
        Log.d(TAG, "Nota que se guardara: ${nuevaNota}")
        notaProvider?.updateNotaEnviada(nuevaNota!!)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if(response.body()?.isSucces == true){
                    Toast.makeText(this@OperarioUpdateNotaActivity, "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(this@OperarioUpdateNotaActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
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