package com.seminariomanufacturadigital.recycleapp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.soporte.updateUsuarios.SoporteUpdateUsuariosActivity
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaUsuariosAdapter(val context: FragmentActivity, val usuarios: ArrayList<Empleado>, val is_available: Boolean): RecyclerView.Adapter<ListaUsuariosAdapter.UsuariosViewHolder>() {

    val sharedPref = SharedPref(context)
    var empeladoProviders : EmpleadosProviders? = null
    var TAG = "ListaUsuariosAdapter"
    var empleadoActual: Empleado? = null
    var empleado: Empleado? = null
    val gson = Gson()
    var fecha : String? = null
    var fechaFormato: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_usuarios_edit, parent, false)
        getEmpleadoFromSesion()
        Log.d(TAG," ${empleadoActual}")
        empeladoProviders = EmpleadosProviders(empleadoActual?.sessionToken)
        return UsuariosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  usuarios.size
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        val usuario = usuarios[position]
        Log.d(TAG, "Usuarios: $usuario")
        //Log.d(TAG, "ArrayUsuarios: $usuarios")
        //saveEmpleadoEditar()

        holder.txtViewNombre.text = "${usuario.nombre} ${usuario.apellidoPat} ${usuario.apellidoMat}"
        holder.txtViewId.text = usuario.numEmpleado
        holder.txtViewTelefono.text = usuario.telefono
        if(!usuario.image.isNullOrBlank()){
            Glide.with(context).load(usuario.image).into(holder.imageViewUsuario)
        }
        holder.imageViewEdit.setOnClickListener { goToUpdate( usuario) }
        holder.imageViewDelete.setOnClickListener { desactivarUsuario(usuario) }

        if(is_available == false){
            holder.imageViewRestore.setOnClickListener { reactivarUsuario(usuario) }
            holder.imageViewRestore.visibility = View.VISIBLE
            holder.datosFechaDesactivacion.visibility = View.VISIBLE
            holder.imageViewEdit.visibility = View.GONE
            holder.imageViewDelete.visibility = View.GONE
            convertirFecha("${usuario.fechaDesactivacion}")
            holder.textViewFechaDesactivacion.text = "Desde: ${fechaFormato}"
        }

        //holder.itemView.setOnClickListener { goToUpdate(usuario) }

    }

    private fun convertirFecha(fechaCompleta: String){
        Log.d(TAG, fechaCompleta)
        val ArrayFechaHora = fechaCompleta.split("T")
        fecha = ArrayFechaHora[0]
        val nuevoArray = fecha!!.split("-")
        fechaFormato = " ${nuevoArray[2]}/${nuevoArray[1]}/${nuevoArray[0]} "
        Log.d(TAG, "${fechaFormato}")
    }


    private fun desactivarUsuario(usuario:Empleado) {
        Log.d(TAG, "Dentro de desactivar usuario ${usuario} ")
        empeladoProviders?.desactivarEmpleado(usuario)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "RESPONSE: $response")
                Log.d(TAG, "BODY: ${response.body()}")
                if(response.body()?.isSucces == true){
                    //goToListaUsuarios()
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun reactivarUsuario(usuario:Empleado) {
        Log.d(TAG, "Dentro de desactivar usuario ${usuario} ")
        empeladoProviders?.reactivarEmpleado(usuario)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "RESPONSE: $response")
                Log.d(TAG, "BODY: ${response.body()}")
                if(response.body()?.isSucces == true){
                    //goToListaUsuarios()
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun goToUpdate(usuario: Empleado) {
        val i = Intent(context, SoporteUpdateUsuariosActivity::class.java)
        i.putExtra("empleadoEditado", usuario.toJson())
        Log.d(TAG, "${ i.putExtra("empleadoEditado", usuario.toJson()) }")
        Log.d(TAG, usuario.toJson())
        context.startActivity(i)
    }

    private fun getEmpleadoFromSesion() {
        if (!sharedPref.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            empleadoActual = gson.fromJson(sharedPref.getData("empleado"), Empleado::class.java)
            Log.d(TAG, " getEmpleadoFromSesion: $empleadoActual")
        }
    }



    class UsuariosViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtViewNombre: TextView
        val txtViewId: TextView
        val txtViewTelefono: TextView
        val imageViewUsuario: ImageView
        val imageViewDelete: ImageView
        val imageViewEdit: ImageView
        val imageViewRestore: ImageView
        val textViewFechaDesactivacion: TextView
        val datosFechaDesactivacion: View

        init {
            txtViewNombre = view.findViewById(R.id.txtview_nombre)
            txtViewId = view.findViewById(R.id.txtview_id)
            txtViewTelefono = view.findViewById(R.id.txtview_telefono)
            imageViewUsuario = view.findViewById(R.id.imageview_usuario)
            imageViewDelete = view.findViewById(R.id.image_delete)
            imageViewEdit = view.findViewById(R.id.image_edit)
            imageViewRestore = view.findViewById(R.id.image_restore)
            datosFechaDesactivacion = view.findViewById(R.id.fecha_desactivacion)
            textViewFechaDesactivacion = view.findViewById(R.id.txtview_fecha_desactivacion)
        }
    }


}