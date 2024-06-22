package com.seminariomanufacturadigital.recycleapp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.operario.update.OperarioEntregaNotaActivity
import com.seminariomanufacturadigital.recycleapp.activities.operario.update.OperarioUpdateNotaActivity
import com.seminariomanufacturadigital.recycleapp.models.Nota

class NotasOperarioAdapters(val context: FragmentActivity, val notas:ArrayList<Nota>):  RecyclerView.Adapter<NotasOperarioAdapters.NotasViewHolder>() {
    val TAG = "NotasOperarioAdapters"
    var nota: Nota? = null

    var fecha : String? = null
    var fechaFormato: String? = null
    var hora : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_nota_operario,  parent, false) ///instacias la vista en la que se esta trabajando
        return NotasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        nota = notas[position]
        Log.d(TAG, "${nota}")

        convertirFecha("${nota?.fechaCreacion}")
        holder.textViewIdNota.text = "Orden #${nota!!.id}"
        holder.textViewFecha.text = fechaFormato
        holder.textViewHora.text = hora
        holder.textViewOperarioNombre.text =  "${nota!!.operario?.nombre}  ${nota!!.operario?.apellidoPat} ${nota!!.operario?.apellidoMat}"
        holder.textViewtextViewOperarioId.text = "${nota!!.operario?.numEmpleado} "
        holder.textViewPacas.text = "${nota!!.numeroPacas} unidad(s)"
        holder.textViewPesoXPaca.text = "${nota!!.pesoPaca} kg(s)"
        holder.textViewPesoTotal.text = "${nota!!.pesoTotal} kg(s)"
        holder.textViewComentarios.text = "${nota!!.comentarios}"

        if(nota!!.estado == "GUARDADA"){
            holder.itemView.setOnClickListener{goToEditNota(nota!!)}
        }
        if(nota!!.estado == "APROBADA" || nota!!.estado == "ENTREGADA"){
            holder.datosCliente.visibility = View.VISIBLE
            holder.textViewNombreCliente.text = "${nota!!.cliente?.razon_social}"
            holder.textViewDireccionCliente.text = "${nota!!.cliente?.direccion}"
            holder.textViewCostoKilo.text = "$${nota!!.costoKilo}"
            holder.textViewTotal.text = "$${nota!!.costoTotal}"
            holder.textViewAdministrativooNombre.text  = "${nota!!.administrativo?.nombre} ${nota!!.administrativo?.apellidoPat} ${nota!!.administrativo?.apellidoMat}"
            Log.d(TAG,"${nota!!.administrativo?.nombre} ${nota!!.administrativo?.apellidoPat} ${nota!!.administrativo?.apellidoMat}")
        }
        if(nota!!.estado == "APROBADA"){
            holder.textViewMensaje.visibility = View.VISIBLE
            holder.itemView.setOnClickListener{ goToEntregar(nota!!) }
        }

        if(nota!!.estado == "ENTREGADA"){
            holder.area_fecha_entrega.visibility = View.VISIBLE
            convertirFecha("${nota?.fechaEntrega}")
            holder.fecha_entrega.text = fechaFormato
            holder.hora_entrega.text = hora
        }

    }

    private fun convertirFecha(fechaCompleta: String){
        val ArrayFechaHora = fechaCompleta.split(" ")
        fecha = ArrayFechaHora[0]

        val nuevoArray = fecha!!.split("-")
        fechaFormato = " ${nuevoArray[2]}/${nuevoArray[1]}/${nuevoArray[0]} "
        hora =ArrayFechaHora[1]

    }

    private fun goToEntregar(nota: Nota) {
        Log.d(TAG, "${ nota }")
        val i = Intent(context, OperarioEntregaNotaActivity::class.java)
        i.putExtra("nota", nota.toJson())
        context.startActivity(i)

    }

    private fun goToEditNota(nota: Nota) {
        Log.d(TAG, "${ nota }")
        val i = Intent(context, OperarioUpdateNotaActivity::class.java)
        i.putExtra("nota", nota.toJson())
        context.startActivity(i)
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

            textViewMensaje = view.findViewById(R.id.mensaje_operador)
            fecha_entrega  = view.findViewById(R.id.fecha_entrega)
            hora_entrega =  view.findViewById(R.id.hora_entrega)
            area_fecha_entrega = view.findViewById(R.id.area_fecha_entrega)
        }

    }
}