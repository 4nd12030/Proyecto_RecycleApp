package com.seminariomanufacturadigital.recycleapp.adapters

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.administrativo.home.AdministrativeHomeActivity
import com.seminariomanufacturadigital.recycleapp.activities.operario.home.OperarioHomeActivity
import com.seminariomanufacturadigital.recycleapp.activities.soporte.home.SoporteHomeActivity
import com.seminariomanufacturadigital.recycleapp.models.Rol
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref

class RolesAdapter (val context: AppCompatActivity, val roles: ArrayList<Rol>): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]
        holder.textViewRol.text = rol.nombre
        Glide.with(context).load(rol.imagen).into(holder.imageViewRol)

        holder.itemView.setOnClickListener { goToRol(rol) }

    }

    private fun goToRol(rol: Rol) {
        val typeActivity = when (rol.nombre) {
            "ADMINISTRATIVO" ->  AdministrativeHomeActivity::class.java
            "SOPORTE" -> SoporteHomeActivity::class.java
            "OPERARIO" -> OperarioHomeActivity::class.java
            else -> OperarioHomeActivity::class.java
        }
        sharedPref.save("rol", rol.nombre)

        val i = Intent(context, typeActivity)
        context.startActivity(i)
    }

    class RolesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewRol: TextView
        val imageViewRol: ImageView

        init {
            textViewRol = view.findViewById(R.id.textView_rol)
            imageViewRol = view.findViewById(R.id.imageView_rol)
        }
    }
}