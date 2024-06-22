package com.seminariomanufacturadigital.recycleapp.activities.soporte.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.models.Rol
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.providers.TiposRolesProvider
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SoporteNuevoRolActivity : AppCompatActivity() {

    var imageNuevoRol: ImageView? = null
    var editTextNombre: EditText? = null
    var buttonGuardar: Button? = null

    var TAG = "SoporteNuevoRolActivity"
    var sharedPref: SharedPref? = null
    val gson = Gson()
    var empleado: Empleado? = null

    private var imageFile: File? = null
    var empleadosProvider: EmpleadosProviders? = null
    var rolesProvider: TiposRolesProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soporte_nuevo_rol)

        sharedPref = SharedPref(this)
        getEmpleadoFromSesion()

        imageNuevoRol = findViewById(R.id.image_view_rol)
        editTextNombre= findViewById(R.id.edittxt_rol)
        buttonGuardar = findViewById(R.id.btn_create_rol)

        rolesProvider = TiposRolesProvider(empleado?.sessionToken!!)
        empleadosProvider =EmpleadosProviders(empleado?.sessionToken)

        imageNuevoRol?.setOnClickListener {  selectImage()  }
        buttonGuardar?.setOnClickListener { goToGuardarPerfil() }


    }

    private fun goToGuardarPerfil() {
        //SE CREAN VARIABLES PARA LOS VALORES QUE INGRESE EL USUARIO
        //var imgFoto = imgViewFoto?.text.toString()
        var nombre =  editTextNombre?.text.toString()

        if ( isValidForm(nombre = nombre) ){
            val rol = Rol( nombre = nombre)

            Log.d(TAG, "Empleado en fun updatedata: ${nombre} ")
            Log.d(TAG, "Empleado en fun updatedata: ${rol?.nombre} ")
            Log.d(TAG, "Imagen nueva: ${imageFile} ")

            rolesProvider?.addRol(imageFile!!, rol)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if(response.body()?.isSucces == true) {
                            //saveEmpleadoInSesion(response.body()?.data.toString())
                        resetForm()
                    }
                    Toast.makeText(this@SoporteNuevoRolActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response:  $response")
                    Log.d(TAG, "Body: ${response.body()}")
                    Log.d(TAG,  "Los datos son: ${rol}")
                }
                    //En caso de error
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                        Toast.makeText(this@SoporteNuevoRolActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }


    }
////Funcion para limpiar formulario

    private fun resetForm() {
        editTextNombre?.setText("")
        imageFile = null
        imageNuevoRol?.setImageResource(R.drawable.ic_add_image)
    }

    ////FUNCIONES PARA SELECCIONAR LA IMAGEN DESDE GALERIA
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == AppCompatActivity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                imageNuevoRol?.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }
/////////////////////////////////////////////////////////////////

    //FUNCIONES DE VALIDACION
    fun isValidForm(nombre: String): Boolean{
        if(nombre.isBlank()) {
            Toast.makeText(this,"Debes ingresar el nombre", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    ////////////////////////////////

    private fun getEmpleadoFromSesion(){
        if(!sharedPref?.getData("empleado").isNullOrBlank()) {
        //Si le usuario existe en sesion
        empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
        //Log.d(TAG, "Empleado: $empleado")
        }
    }


}