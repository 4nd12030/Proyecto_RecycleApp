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
import com.seminariomanufacturadigital.recycleapp.activities.administrativo.home.AdministrativeHomeActivity
import com.seminariomanufacturadigital.recycleapp.activities.soporte.updateUsuarios.SoporteUpdateUsuariosActivity
import com.seminariomanufacturadigital.recycleapp.fragments.administrativo.clientes.AdministrativoListClientesFragment
import com.seminariomanufacturadigital.recycleapp.models.Cliente
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.ClientesProviders
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientesAdapter   (val context: FragmentActivity, val clientes: ArrayList<Cliente>, val is_available: Boolean): RecyclerView.Adapter<ClientesAdapter.ClientesViewHolder>() {


    var sharedPref = SharedPref(context)
    var clienteProviders : ClientesProviders? = null
    var empleadoProviders : EmpleadosProviders? = null
    var TAG = "ClientesAdapter"
    var empleadoActual: Empleado? = null
    val gson = Gson()
    var fecha : String? = null
    var fechaFormato: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_clientes, parent, false)
        getEmpleadoFromSesion()
        Log.d(TAG," ${empleadoActual}")
        empleadoProviders = EmpleadosProviders(empleadoActual?.sessionToken)
        clienteProviders = ClientesProviders(empleadoActual?.sessionToken)
        return ClientesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  clientes.size
    }

    override fun onBindViewHolder(holder: ClientesViewHolder, position: Int) {
        val cliente = clientes[position]
        Log.d(TAG, "Cliente: $cliente")
        //Log.d(TAG, "ArrayUsuarios: $usuarios")
        //saveEmpleadoEditar()

        holder.txtViewNombre.text = cliente.razon_social
        holder.txtViewDireccion.text = cliente.direccion
        holder.txtViewTelefono.text = cliente.telefono
        if(is_available == false){
            holder.imageViewReactivar.visibility = View.VISIBLE
            holder.viewFechaDesactivacion.visibility= View.VISIBLE
            holder.imageViewEdit.visibility= View.GONE
            holder.imageViewDelete.visibility= View.GONE
            convertirFecha("${cliente.fecha_desactivacion}")
            holder.textViewFechaDesactivacion.text = "Desactivado desde: ${fechaFormato}"
            holder.imageViewReactivar.setOnClickListener { goToReactive( cliente) }
        }

        holder.imageViewEdit.setOnClickListener { goToUpdate( cliente) }
        holder.imageViewDelete.setOnClickListener { desactivarUsuario(cliente) }
        //holder.itemView.setOnClickListener { goToUpdate(usuario) }

    }

    private fun convertirFecha(fechaCompleta: String){
        val ArrayFechaHora = fechaCompleta.split("T")
        fecha = ArrayFechaHora[0]
        val nuevoArray = fecha!!.split("-")
        fechaFormato = " ${nuevoArray[2]}/${nuevoArray[1]}/${nuevoArray[0]} "
    }

    private fun desactivarUsuario(cliente: Cliente) {
        Log.d(TAG, "Dentro de desactivar cliente ${cliente} ")
        clienteProviders?.desactivarCliente(cliente)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "RESPONSE: $response")
                Log.d(TAG, "BODY: ${response.body()}")
                if(response.body()?.isSucces == true){
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun goToReactive(cliente: Cliente) {
        Log.d(TAG, "Dentro de desactivar cliente ${cliente} ")
        clienteProviders?.reactivarCliente(cliente)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                Log.d(TAG, "RESPONSE: $response")
                Log.d(TAG, "BODY: ${response.body()}")
                if(response.body()?.isSucces == true){
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun goToUpdate(cliente: Cliente) {
//        val i = Intent(context, SoporteUpdateUsuariosActivity::class.java)
//        i.putExtra("cliente", cliente.toJson())
//        Log.d(TAG, "${ i.putExtra("clienteEditado", cliente.toJson()) }")
//        Log.d(TAG, cliente.toJson())
//        context.startActivity(i)
    }

    private fun getEmpleadoFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            empleadoActual = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, " getEmpleadoFromSesion: $empleadoActual")
        }
    }



    class ClientesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtViewNombre: TextView
        val txtViewDireccion: TextView
        val txtViewTelefono: TextView
        val imageViewDelete: ImageView
        val imageViewEdit: ImageView
        val textViewFechaDesactivacion: TextView
        val viewFechaDesactivacion: View
        val imageViewReactivar: ImageView

        init {
            txtViewNombre = view.findViewById(R.id.txtview_nombre_cliente)
            txtViewDireccion = view.findViewById(R.id.txtview_direccion)
            txtViewTelefono = view.findViewById(R.id.txtview_telefono_cliente)
            imageViewDelete = view.findViewById(R.id.image_delete)
            imageViewEdit = view.findViewById(R.id.image_edit)
            textViewFechaDesactivacion = view.findViewById(R.id.txtview_fecha_desactivacion)
            viewFechaDesactivacion = view.findViewById(R.id.fecha_desactivacion)
            imageViewReactivar = view.findViewById(R.id.image_reactivar)
        }
    }


}