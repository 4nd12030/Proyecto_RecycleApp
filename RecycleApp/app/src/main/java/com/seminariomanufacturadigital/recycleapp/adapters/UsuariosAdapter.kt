package com.seminariomanufacturadigital.recycleapp.adapters

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.operario.update.OperarioUpdateActivity
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref

class UsuariosAdapter(val context: AppCompatActivity, val usuarios: ArrayList<Empleado>): RecyclerView.Adapter<UsuariosAdapter.UsuariosViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_usuarios, parent, false)
        return UsuariosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  usuarios.size
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        val usuario = usuarios[position]
        Log.d("UsuariosAdapter", "Usuarios: $usuario")

        holder.txtViewNombre.text = "${usuario.nombre} ${usuario.apellidoPat} ${usuario.apellidoMat}"
        holder.txtViewId.text = usuario.numEmpleado
        holder.txtViewTelefono.text = usuario.telefono
        if(!usuario?.image.isNullOrBlank()){
            Glide.with(context).load(usuario.image).into(holder.imageViewUsuario)
        }
//        holder.itemView.setOnClickListener { goToUpdate(usuario) }

    }

    private fun goToUpdate(usuario: Empleado) {
        val i = Intent(context, OperarioUpdateActivity::class.java)
        i.putExtra("empleado", usuario.toJson())
        context.startActivity(i)
    }

    class UsuariosViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtViewNombre: TextView
        val txtViewId: TextView
        val txtViewTelefono: TextView
        val imageViewUsuario: ImageView

        init {
            txtViewNombre = view.findViewById(R.id.txtview_nombre)
            txtViewId = view.findViewById(R.id.txtview_id)
            txtViewTelefono = view.findViewById(R.id.txtview_telefono)
            imageViewUsuario = view.findViewById(R.id.imageview_usuario)
        }
    }


}