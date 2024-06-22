package com.seminariomanufacturadigital.recycleapp.fragments.operario

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.MainActivity
import com.seminariomanufacturadigital.recycleapp.activities.SelectRolesActivity
import com.seminariomanufacturadigital.recycleapp.activities.operario.update.OperarioUpdateActivity
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView

class OperarioProfileFragment : Fragment() {
    private val TAG = "OperarioProfileFragment"

    var myView: View? = null
    var buttonSeleccionarRol: Button? = null
    var buttonUpdateProfile: Button? = null
    var circleImageEmpleado: CircleImageView? = null
    var textViewNombre: TextView? = null
    var textViewNoEmpleado: TextView? = null
    var textViewTelefono: TextView? = null
    var imageViewLogout: ImageView? = null

    var sharedPref: SharedPref? = null
    var empleado: Empleado? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_operario_profile, container, false)

        sharedPref = SharedPref(requireActivity())

        buttonSeleccionarRol = myView?.findViewById(R.id.btn_seleccionar_rol)
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        textViewNombre = myView?.findViewById(R.id.textview_nombre)
        textViewNoEmpleado = myView?.findViewById(R.id.textview_no_empleadol)
        textViewTelefono= myView?.findViewById(R.id.textview_telefono)
        circleImageEmpleado = myView?.findViewById(R.id.circle_image_empleado)
        imageViewLogout = myView?.findViewById(R.id.imagview_logout)

        buttonSeleccionarRol?.setOnClickListener { goToRolSelect() }
        buttonUpdateProfile?.setOnClickListener { goToUpdate() }
        imageViewLogout?.setOnClickListener{ logout() }

        getEmployeeFromSesion()
        //asigna los datos del usuario logeado a los imageview
        textViewNombre?.text = "${empleado?.nombre} ${empleado?.apellidoPat} ${empleado?.apellidoMat}"
        textViewNoEmpleado?.text = empleado?.numEmpleado
        textViewTelefono?.text = empleado?.telefono
        Log.d(TAG, "Empleado imagen ${empleado?.image}")
        if(!empleado?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(empleado?.image).into(circleImageEmpleado!!)
        }

        return myView
    }

    private fun getEmployeeFromSesion(){
        val gson = Gson()
        if(!sharedPref?.getData("empleado").isNullOrBlank()){
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, "Empleado: $empleado")
        }
    }

    private fun goToUpdate(){
        val i = Intent(requireContext(), OperarioUpdateActivity::class.java)
        startActivity(i)
    }

    private  fun logout(){
        sharedPref?.remove("empleado")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

    private fun goToRolSelect() {
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }



}