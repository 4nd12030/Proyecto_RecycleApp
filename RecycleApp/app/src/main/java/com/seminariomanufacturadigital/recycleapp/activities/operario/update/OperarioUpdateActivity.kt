package com.seminariomanufacturadigital.recycleapp.activities.operario.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class OperarioUpdateActivity : AppCompatActivity() {

    var circleImageEmpleado: CircleImageView? = null
    var editTextNombre: EditText? = null
    var editTextApellidoPaterno: EditText? = null
    var editTextApellidoMaterno: EditText? = null
    var editTextTelefono: EditText? = null
    var buttonActualizar: Button? = null
    var TAG = "OperarioUpdateActivity"

    var sharedPref:SharedPref? = null
    val gson = Gson()
    var empleado:Empleado? = null
    private var imageFile: File? = null
    var empleadosProvider: EmpleadosProviders? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operario_update)

//        usuario = intent.getStringExtra("empleadoEditado")
//        Log.d(TAG, "Usuario a editar: ${usuario}")
        getEmpleadoFromSesion()

        sharedPref = SharedPref(this)

        circleImageEmpleado = findViewById(R.id.circle_image_empleado)
        editTextNombre = findViewById(R.id.edittxt_nombre)
        editTextApellidoPaterno = findViewById(R.id.edittxt_apellido_pat)
        editTextApellidoMaterno = findViewById(R.id.edittxt_apellido_mat)
        editTextTelefono = findViewById(R.id.edittxt_telelfono)
        buttonActualizar = findViewById(R.id.btn_update)


        Log.d(TAG, "Empleado antes del porvider y despues fun getEmpleadoFromSesion() $empleado")
        empleadosProvider = EmpleadosProviders(empleado?.sessionToken)
        Log.d(TAG, "Empleado despues de fun getEmpleadoFromSesion() $empleado")
        Log.d(TAG, "Session Token Empleado ${empleado?.sessionToken}")
        //Asigna los valores del empleado a los edit text
        editTextNombre?.setText(empleado?.nombre)
        editTextApellidoPaterno?.setText(empleado?.apellidoPat)
        editTextApellidoMaterno?.setText(empleado?.apellidoMat)
        editTextTelefono?.setText(empleado?.telefono)
        if(!empleado?.image.isNullOrBlank()){
            Glide.with(this).load(empleado?.image).into(circleImageEmpleado!!)
        }

        //Acciones Onclicklistener
        circleImageEmpleado?.setOnClickListener { selectImsge() }
        buttonActualizar?.setOnClickListener { updateData() }

    }

    //FUNCIONES PARA ELEGIR LA IMAGEN DEL USUARIO

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result:ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            Log.d(TAG, "Result code: $resultCode")
            Log.d(TAG, "data: $data")

            if(resultCode == AppCompatActivity.RESULT_OK){
                val fileUri = data?.data
                imageFile = File(fileUri?.path)
                circleImageEmpleado?.setImageURI(fileUri)

                Log.d(TAG, "Imagen actualizada: $imageFile")
                Log.d(TAG, "Imagen actualizada: ${circleImageEmpleado?.setImageURI(fileUri)}")

            } else if (resultCode == ImagePicker.RESULT_ERROR){
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Log.d(TAG, "Entro en : $imageFile")
                Toast.makeText(this, "Tarea se cancelo", Toast.LENGTH_LONG).show()
            }
        }

    private fun selectImsge() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent {  intent ->
                startImageForResult.launch(intent)
            }
    }
/////////////////////////////////////////////////////////////////

    // FUNCION QUE ACTUALIZA LOS DATOS DEL USUARIO
    private fun updateData() {
       var nombre = editTextNombre?.text.toString()
       var apellidoMaterno = editTextApellidoMaterno?.text.toString()
       var apellidoPaterno = editTextApellidoPaterno?.text.toString()
       var telefono = editTextTelefono?.text.toString()

        Log.d(TAG, "Empleado en fun updatedata: $empleado")

        empleado?.nombre = nombre
        empleado?.apellidoPat = apellidoPaterno
        empleado?.apellidoMat = apellidoMaterno
        empleado?.telefono = telefono

        Log.d(TAG, "Empleado en fun updatedata: ${empleado?.nombre} ")
        Log.d(TAG, "Imagen nueva: ${imageFile} ")
        Log.d(TAG, "Empleado nuevo: ${empleado} ")
        if(imageFile != null){
            empleadosProvider?.update(imageFile!!, empleado!!)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")
                    Toast.makeText(this@OperarioUpdateActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    if(response.body()?.isSucces == true){
                        saveEmpleadoInSesion(response.body()?.data.toString())
                    }
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@OperarioUpdateActivity, "Error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            empleadosProvider?.updateWithoutImage(empleado!!)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp> ) {
                    Log.d(TAG, "RESPONSE: $response")
                    Log.d(TAG, "BODY: ${response.body()}")
                    Toast.makeText(this@OperarioUpdateActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    if(response.body()?.isSucces == true){
                        saveEmpleadoInSesion(response.body()?.data.toString())
                    }
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@OperarioUpdateActivity, "Error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

    }


    //FUNCIONES DE SESION (SHARED PREFERENCES)
    private fun getEmpleadoFromSesion() {
        if (!sharedPref?.getData("empleado").isNullOrBlank()) {
            //Si el usuario existe en sesion
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, " Empleado en fun getEmpleadoActualFromSesion: $empleado")
        }
    }

    private fun saveEmpleadoInSesion(data: String){
        val empleado= gson.fromJson(data, Empleado::class.java)
        sharedPref?.save("empleado", empleado)
        Log.d(TAG, "Empleado guardado en sesion: $empleado")
    }

//    private fun getEmpleadoParaEditar() {
//        if (!sharedPref?.getData("empleadoEditado").isNullOrBlank()) {
//            //Si el usuario existe en sesion
//            empleadoAEditar = gson.fromJson(sharedPref?.getData("empleadoEditado"), Empleado::class.java)
//            Log.d(TAG, " Empleado en fun getEmpleadoParaEditar: $empleadoActual")
//        }
    }



