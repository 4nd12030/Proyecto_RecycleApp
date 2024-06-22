package com.seminariomanufacturadigital.recycleapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.soporte.listas.usuarios.SoporteListaUsuariosActivity
import com.seminariomanufacturadigital.recycleapp.models.Rol

class TiposRolesAdapter(val context: FragmentActivity, val tiposRoles: ArrayList<Rol>): RecyclerView.Adapter<TiposRolesAdapter.TiposRolesViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiposRolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_tipos_roles, parent, false)
        return (TiposRolesViewHolder(view))
    }

    override fun getItemCount(): Int {
        return tiposRoles.size
    }

    override fun onBindViewHolder(holder: TiposRolesViewHolder, position: Int) {
        val tipoRol = tiposRoles[position]

        holder.textViewNombreRol.text = tipoRol.nombre
        Glide.with(context).load(tipoRol.imagen).into(holder.imageViewRol)
        holder.itemView.setOnClickListener { goToUsuarios( tipoRol) }
    }

    private fun goToUsuarios(tipoRol: Rol) {
        val i = Intent( context, SoporteListaUsuariosActivity::class.java)
        i.putExtra("idRol", tipoRol.id)
        i.putExtra("nombreRol", tipoRol.nombre)
        context.startActivity(i)
    }

    class TiposRolesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewNombreRol: TextView
        val imageViewRol: ImageView

        init {
            textViewNombreRol = view.findViewById(R.id.textview_nombre_rol)
            imageViewRol = view.findViewById(R.id.imageview_image_rol)

        }

    }

}