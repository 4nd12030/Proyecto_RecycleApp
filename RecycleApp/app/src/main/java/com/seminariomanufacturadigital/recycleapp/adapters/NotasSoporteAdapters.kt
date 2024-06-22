package com.seminariomanufacturadigital.recycleapp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.administrativo.update.AdministrativoUpdateNotaActivity
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.Nota
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.providers.NotasProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotasSoporteAdapters(val context: FragmentActivity, val notas:ArrayList<Nota>):  RecyclerView.Adapter<NotasSoporteAdapters.NotasViewHolder>() {
    val TAG = "NotasSoporteAdapters"
    var sharedPref: SharedPref? =null
    val gson = Gson()
    var usuario: Empleado? = null
    var notaProvider: NotasProviders? = null
    var notaStatus = ""
    var notaOperario = ""
    var notaAdministrativo = ""
    var fecha : String? = null
    var fechaFormato: String? = null
    var hora : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_nota_soporte,  parent, false) ///instacias la vista en la que se esta trabajando

        sharedPref = SharedPref(context)
        getEmpleadoFromSesion()
        notaProvider = NotasProviders(usuario?.sessionToken)

        return NotasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        val nota = notas[position]

        val fechaCompleta = "${nota.fechaCreacion}"
        val ArrayFechaHora = fechaCompleta.split(" ")

        val fecha = ArrayFechaHora[0]
        val nuevoArray = fecha.split("-")
        val fechaFormato = " ${nuevoArray[2]}/${nuevoArray[1]}/${nuevoArray[0]} "

        val hora =ArrayFechaHora[1]
        Log.d(TAG, "Nota recibida ${nota}")

        holder.textViewIdNota.text = "Orden #${nota.id}"
        holder.textViewFecha.text = "${fechaFormato}"
        holder.textViewHora.text = "${hora}"
        holder.textViewOperarioNombre.text =  "${nota.operario?.nombre}  ${nota.operario?.apellidoPat} ${nota.operario?.apellidoMat}"
        holder.textViewtextViewOperarioId.text = "${nota.operario?.numEmpleado} "
        holder.textViewPacas.text = "${nota.numeroPacas} unidad(s)"
        holder.textViewPesoXPaca.text = "${nota.pesoPaca} kg(s)"
        holder.textViewPesoTotal.text = "${nota.pesoTotal} kg(s)"
        holder.textViewComentarios.text = "${nota.comentarios}"


        if(nota.estado == "PENDIENTE" || nota.estado == "APROBADA" || nota.estado == "ENTREGADA" ){
            holder.datosCliente.visibility = View.VISIBLE
        }

        if(nota!!.estado == "ENTREGADA"){
            holder.area_fecha_entrega.visibility = View.VISIBLE
            convertirFecha("${nota?.fechaEntrega}")
            holder.fecha_entrega.text = fechaFormato
            holder.hora_entrega.text = hora
        }


        holder.textViewNombreCliente.text = "${nota.cliente?.razon_social}"
        holder.textViewDireccionCliente.text = "${nota.cliente?.direccion}"
        holder.textViewCostoKilo.text = "$${nota.costoKilo}"
        holder.textViewTotal.text = "$${nota.costoTotal}"
        holder.textViewAdministrativooNombre.text  = "${nota.administrativo?.nombre} ${nota.administrativo?.apellidoPat} ${nota.administrativo?.apellidoMat}"

        if(nota.estado == "APROBADA"){
            holder.imageViewDescarga.visibility = View.VISIBLE
            notaStatus = "${nota.estado}"
            notaOperario = "${nota.operario?.numEmpleado}"
            notaAdministrativo = "${ nota.administrativo?.numEmpleado}"
            holder.imageViewDescarga.setOnClickListener { descargarNota()  }
        }

    }

    private fun convertirFecha(fechaCompleta: String){
        val ArrayFechaHora = fechaCompleta.split(" ")
        fecha = ArrayFechaHora[0]

        val nuevoArray = fecha!!.split("-")
        fechaFormato = " ${nuevoArray[2]}/${nuevoArray[1]}/${nuevoArray[0]} "
        hora =ArrayFechaHora[1]

    }


    private fun  descargarNota(){
        Log.d(TAG, "Datos enviados: ${notaOperario}   ${notaAdministrativo} ${notaStatus } ")
        notaProvider?.descargarArchivo(notaOperario, notaAdministrativo, notaStatus)?.enqueue(object: Callback<ResponseHttp>{
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body()?.isSucces == true) {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Datos recibidos: ${response.body()}")
                } else {
                    Toast.makeText(context,"Los datos son incorrectos", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                Log.d(TAG, "Hubo un error: ${t.message}")
                Toast.makeText(context, "Hubo un error: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }


    private fun getEmpleadoFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            usuario = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, " Empleado actual en fun getEmpleadoFromSesion: $usuario")
        }
    }


    class NotasViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewIdNota:TextView
        val textViewFecha: TextView
        val textViewHora: TextView
        val textViewOperarioNombre: TextView
        val textViewtextViewOperarioId: TextView
        val textViewPacas: TextView
        val textViewPesoXPaca: TextView
        val textViewPesoTotal: TextView
        val textViewComentarios: TextView

        val datosCliente: View
        val textViewNombreCliente: TextView
        val textViewDireccionCliente: TextView
        val textViewCostoKilo: TextView
        val textViewTotal: TextView
        val textViewAdministrativooNombre: TextView
        val imageViewDescarga: ImageView
        val textViewMensaje: TextView

        val fecha_entrega: TextView
        val hora_entrega: TextView
        val area_fecha_entrega: View


        init{
            textViewIdNota = view.findViewById(R.id.txt_id_nota)
            textViewFecha = view.findViewById(R.id.txt_fecha)
            textViewHora= view.findViewById(R.id.txt_hora)
            textViewOperarioNombre= view.findViewById(R.id.txt_nombre_operario)
            textViewtextViewOperarioId= view.findViewById(R.id.txt_id_operario)
            textViewPacas= view.findViewById(R.id.txt_cantidad_pacas)
            textViewPesoXPaca= view.findViewById(R.id.txt_peso_paca)
            textViewPesoTotal= view.findViewById(R.id.txt_peso_total)
            textViewComentarios= view.findViewById(R.id.txt_comentarios)

            datosCliente = view.findViewById(R.id.datos_clientes)
            textViewNombreCliente = view.findViewById(R.id.nombre_cliente)
            textViewDireccionCliente = view.findViewById(R.id.direccion_cliente)
            textViewCostoKilo = view.findViewById(R.id.text_costo_x_kilo)
            textViewTotal = view.findViewById(R.id.costo_total)
            textViewAdministrativooNombre = view.findViewById(R.id.id_administrativo)

            imageViewDescarga =  view.findViewById(R.id.image_descargar)
            textViewMensaje = view.findViewById(R.id.mensaje_operador)
            fecha_entrega  = view.findViewById(R.id.fecha_entrega)
            hora_entrega =  view.findViewById(R.id.hora_entrega)
            area_fecha_entrega = view.findViewById(R.id.area_fecha_entrega)
        }

    }
}