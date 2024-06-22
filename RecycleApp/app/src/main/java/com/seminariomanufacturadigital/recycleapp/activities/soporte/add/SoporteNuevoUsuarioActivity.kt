package com.seminariomanufacturadigital.recycleapp.activities.soporte.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SoporteNuevoUsuarioActivity : AppCompatActivity() {

    var circleImageNuevoUusario: CircleImageView? = null
    var editTextNombre: EditText? = null
    var editTextApellidoPaterno: EditText? = null
    var editTextApellidoMaterno: EditText? = null
    var editTextTelefono: EditText? = null
    var editTextIdEmpleado: EditText? = null
    var editTextContrasena: EditText? = null
    var buttonGuardar: Button? = null
    var spinnerPerfil: Spinner? = null
    var TAG = "SoporteNuevoUsuarioActivity"

    var sharedPref: SharedPref? = null
    val gson = Gson()
    var empleado: Empleado? = null

    private var imageFile: File? = null
    var empleadosProvider: EmpleadosProviders? = null
    var perfilesProvider: TiposRolesProvider? = null

    var perfiles = ArrayList<Rol>()
    var idPerfil = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soporte_nuevo_usuario)

        sharedPref = SharedPref(this)
        getEmpleadoFromSesion()

        circleImageNuevoUusario = findViewById(R.id.image_nuevo_usuario)
        editTextNombre = findViewById(R.id.edittxt_nombre)
        editTextApellidoPaterno = findViewById(R.id.edittxt_apellido_pat)
        editTextApellidoMaterno = findViewById(R.id.edittxt_apellido_mat)
        editTextTelefono = findViewById(R.id.edittxt_telefono)
        editTextContrasena = findViewById(R.id.edittxt_password)
        spinnerPerfil = findViewById(R.id.spinner_perfil)
        buttonGuardar = findViewById(R.id.btn_create_usuario)

        circleImageNuevoUusario?.setOnClickListener {  selectImage()  }
        buttonGuardar?.setOnClickListener { guardarUsuario() }

        perfilesProvider = TiposRolesProvider(empleado?.sessionToken!!)
        empleadosProvider =EmpleadosProviders(empleado?.sessionToken)
        getPerfil()
    }

    private fun getPerfil() {
        perfilesProvider?.getAll()?.enqueue(object : Callback<ArrayList<Rol>> {
            override fun onResponse(
                call: Call<ArrayList<Rol>>,
                response: Response<ArrayList<Rol>>
            ) {
                if(response.body() != null){
                    //perfiles = response.body()!!
                    perfiles = response.body()!!

                    Log.d(TAG,  "arrayAdapter : $perfiles}")
                    val arrayAdapter = ArrayAdapter<Rol>(this@SoporteNuevoUsuarioActivity, android.R.layout.simple_dropdown_item_1line, perfiles)
                    Log.d(TAG,  "Array adapter : $arrayAdapter")
                    spinnerPerfil?.adapter = arrayAdapter
                    spinnerPerfil?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            idPerfil = perfiles[position].id!!
                            Log.d(TAG,  "Perfiles : $perfiles")
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Rol>>, t: Throwable) {
                Log.d(TAG, "Error: ${t.message}")
                Toast.makeText(this@SoporteNuevoUsuarioActivity, "Error ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    ////FUNCIONES PARA SELECCIONAR LA IMAGEN DESDE GALERIA
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == AppCompatActivity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //el archivo que se guarda como imagen en el servidor
                circleImageNuevoUusario?.setImageURI(fileUri)
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
    fun isValidForm(
        nombre: String,
        apellidoPat: String,
        apellidoMat: String,
        telefono: String,
        numEmpleado: String,
        contrasena: String): Boolean{
        if(nombre.isBlank()) {
            Toast.makeText(this,"Debes ingresar el nombre", Toast.LENGTH_LONG).show()
            return false
        }
        if(apellidoPat.isBlank()) {
            Toast.makeText(this,"Debes ingresar tu apellido paterno", Toast.LENGTH_LONG).show()
            return false
        }
        if(apellidoMat.isBlank()) {
            Toast.makeText(this,"Debes ingresartu apellido maternol", Toast.LENGTH_LONG).show()
            return false
        }
        if(telefono.isBlank()) {
            Toast.makeText(this,"Debes ingresar el telefono", Toast.LENGTH_LONG).show()
            return false
        }
        if(numEmpleado.isBlank()) {
            Toast.makeText(this,"Debes ingresar tu numerode empleado", Toast.LENGTH_LONG).show()
            return false
        }
        if(contrasena.isBlank()) {
            Toast.makeText(this,"Debes  introducir una contraseña", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    ////////////////////////////////

    fun guardarUsuario(){
        //SE CREAN VARIABLES PARA LOS VALORES QUE INGRESE EL USUARIO
        //var imgFoto = imgViewFoto?.text.toString()
        var nombre =  editTextNombre?.text.toString()
        var apellidoPat = editTextApellidoPaterno?.text.toString()
        var apellidoMat = editTextApellidoMaterno?.text.toString()
        var telefono = editTextTelefono?.text.toString()
        var numEmpleado = editTextIdEmpleado?.text.toString()
        var contrasena = editTextContrasena?.text.toString()
        //var rolEmpleado = idPerfil

        if(isValidForm(nombre = nombre,
                apellidoPat = apellidoPat,
                apellidoMat = apellidoMat,
                telefono = telefono,
                numEmpleado =numEmpleado,
                contrasena = contrasena)) {

            val empleado = Empleado(
                nombre = nombre,
                apellidoPat = apellidoPat,
                apellidoMat = apellidoMat,
                telefono = telefono,
                numEmpleado =numEmpleado,
                contrasena = contrasena,
                isAvailable = true)
            Log.d(TAG, "Empleado  enviado ${empleado}")
            Log.d(TAG, "NumEmpleado  enviado ${empleado.numEmpleado}")
            Log.d(TAG, "ImageEmpleado  enviado ${imageFile}")

            if(imageFile != null){
                empleadosProvider?.addUser(idPerfil, imageFile!!, empleado)?.enqueue(object: Callback<ResponseHttp> {
                    override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                        if(response.body()?.isSucces == true) {
                            //saveEmpleadoInSesion(response.body()?.data.toString())
                            resetForm()
                        }

                        Toast.makeText(this@SoporteNuevoUsuarioActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Response:  $response")
                        Log.d(TAG, "Body: ${response.body()}")
                        Log.d(TAG,  "Los datos son: ${empleado}")
                    }
                    //En caso de error
                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(TAG, "Se produjo un error ${t.message}")
                        Toast.makeText(this@SoporteNuevoUsuarioActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                empleadosProvider?.addUserWithoutImage(idPerfil, empleado)?.enqueue(object: Callback<ResponseHttp> {
                    override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                        if(response.body()?.isSucces == true) {
                            //saveEmpleadoInSesion(response.body()?.data.toString())
                            resetForm()
                        }

                        Toast.makeText(this@SoporteNuevoUsuarioActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Response:  $response")
                        Log.d(TAG, "Body: ${response.body()}")
                        Log.d(TAG,  "Los datos son: ${empleado}")
                    }
                    //En caso de error
                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(TAG, "Se produjo un error ${t.message}")
                        Toast.makeText(this@SoporteNuevoUsuarioActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }


        }
    }

    ////Funcion para limpiar formulario
    private fun resetForm() {
        editTextNombre?.setText("")
        editTextApellidoPaterno?.setText("")
        editTextApellidoMaterno?.setText("")
       editTextTelefono?.setText("")
       editTextIdEmpleado?.setText("")
        editTextContrasena?.setText("")
        editTextNombre?.setText("")
        imageFile = null
        circleImageNuevoUusario?.setImageResource(R.drawable.ic_person_brown_300)
    }


    /////////////////////////////////////////////////////////////////////////
    private fun getEmpleadoFromSesion(){
        if(!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si le usuario existe en sesion
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, "EmpleadoACTUAL: $empleado")
        }
    }




}