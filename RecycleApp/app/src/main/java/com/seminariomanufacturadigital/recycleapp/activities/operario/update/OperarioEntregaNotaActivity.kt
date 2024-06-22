package com.seminariomanufacturadigital.recycleapp.activities.operario.update

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.NotasProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OperarioEntregaNotaActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    var TAG = "OperarioEntregaNotaActivity"
    var nota: Nota? = null
    var nuevaNota: Nota? = null
    var gson = Gson()
    var notaProvider: NotasProviders? = null
    var sharedPref: SharedPref? = null
    var usuario: Empleado? = null

    var textViewIdNota: TextView? = null
    var textViewOperarioNombre: TextView? = null
    var textViewtextViewOperarioId: TextView? = null
    var textViewPacas: TextView? = null
    var textViewPesoXPaca: TextView? = null
    var textViewPesoTotal: TextView? = null
    var textViewComentarios: TextView? = null

    var btnCancelarNota: Button? = null
    var btnNotaEntregada: Button? = null
    var textViewCliente: TextView? = null
    var textViewDireccion: TextView? = null
    var textViewCostoPorKilo: TextView? = null
    var textViewTotal: TextView? = null
    var textViewAsigno: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operario_entrega_nota)

        nota = gson.fromJson(intent.getStringExtra("nota"), Nota::class.java)
        Log.d(TAG, "nota recibida ${nota}")

        sharedPref = SharedPref(this)
        getEmpleadoFromSesion()
        notaProvider = NotasProviders(usuario?.sessionToken)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "Nota #${nota?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textViewIdNota = findViewById(R.id.num_orden)
        textViewOperarioNombre = findViewById(R.id.nombre_operario)
        textViewtextViewOperarioId = findViewById(R.id.id_empleado)
        textViewPacas = findViewById(R.id.num_pacas)
        textViewPesoXPaca = findViewById(R.id.peso_paca)
        textViewPesoTotal = findViewById(R.id.peso_total)
        textViewComentarios = findViewById(R.id.comentarios_operario)
        textViewCliente = findViewById(R.id.nombre_cliente)
        textViewDireccion= findViewById(R.id.direccion_cliente)
        textViewCostoPorKilo = findViewById(R.id.costo_x_kilo)
        textViewTotal = findViewById(R.id.costo_total)
        textViewAsigno = findViewById(R.id.asigna_cliente)

        btnCancelarNota = findViewById(R.id.btn_cancelar)
        btnNotaEntregada = findViewById(R.id.btn_entrega)

        getValuesCampos()

        btnCancelarNota?.setOnClickListener { goToEliminarNota("${nota?.id}") }
        btnNotaEntregada?.setOnClickListener { goToNotaEntregada("${nota?.id}") }

    }

    private fun getValuesCampos() {
        textViewOperarioNombre?.text = "${nota?.operario?.nombre} ${nota?.operario?.apellidoPat} ${nota?.operario?.apellidoMat}"
        textViewtextViewOperarioId?.text = nota?.idOperario
        textViewPacas?.text = "${nota!!.numeroPacas} unidad(s)"
        textViewPesoXPaca?.text = "${nota!!.pesoPaca} kilo(s)"
        Log.d(TAG, "textViewPesoXPaca:  ${nota!!.pesoPaca}")
        textViewPesoTotal?.text = "${nota?.pesoTotal} kilo(s)"
        textViewComentarios?.text = "${nota?.comentarios}"
        Log.d(TAG, "Nombre empleado:  ${textViewOperarioNombre?.text}")
        textViewCliente?.text = nota?.cliente?.razon_social
        textViewDireccion?.text = nota?.cliente?.direccion
        textViewCostoPorKilo?.text = "$${ nota?.costoKilo }"
        textViewTotal?.text = "$${ nota?.costoTotal }"
        textViewAsigno?.text ="${nota?.administrativo?.nombre} ${nota?.administrativo?.apellidoPat} ${nota?.administrativo?.apellidoMat}"
    }


    private fun goToListNotas() {
        val i = Intent(this, OperarioHomeActivity::class.java)
        startActivity(i)
    }

    ////////////////////////////////////
    ///Acciones Botones
    private fun goToNotaEntregada(idNota: String) {

        notaProvider?.updateNotaEntregada(idNota)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body()?.isSucces == true) {
                    Toast.makeText(
                        this@OperarioEntregaNotaActivity,
                        "${response.body()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(
                    this@OperarioEntregaNotaActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }


    private fun goToEliminarNota(idNota: String) {
        notaProvider?.deleteNota(idNota)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body()?.isSucces == true) {
                    Toast.makeText(
                        this@OperarioEntregaNotaActivity,
                        "${response.body()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    goToListNotas()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Toast.makeText(
                    this@OperarioEntregaNotaActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
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