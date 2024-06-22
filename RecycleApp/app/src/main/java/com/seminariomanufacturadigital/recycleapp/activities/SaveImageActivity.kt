package com.seminariomanufacturadigital.recycleapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.operario.home.OperarioHomeActivity
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity : AppCompatActivity() {
    val TAG = "SaveImageActivity"
    var circleImageEmployee : CircleImageView? = null
    var btnConfirma : Button? = null
    val gson = Gson()
    var btnNext : Button? = null

    private var imageFile :  File? = null
    var empleadoProvider: EmpleadosProviders? = null
    var empleado: Empleado? = null
    var sharedPref: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPref = SharedPref(this)
        getEmpleadoFromSesion()
        empleadoProvider = EmpleadosProviders(empleado?.sessionToken)

        circleImageEmployee = findViewById(R.id.circle_image_empleado)
        btnConfirma = findViewById(R.id.btn_confirmar)
        btnNext = findViewById(R.id.btn_next)

        circleImageEmployee?.setOnClickListener { selectImage()  }
        btnNext?.setOnClickListener{ goToEmployeHome() }
        btnConfirma?.setOnClickListener { saveImage() }
    }


    private val  startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if(resultCode == AppCompatActivity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) //archivo que se guarda en el servidor
                circleImageEmployee?.setImageURI(fileUri)

            } else if(resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "Tarea se cancelo", Toast.LENGTH_LONG ).show()
            }
        }

    private fun selectImage(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch( intent )
            }
    }

    private fun saveImage(){
        //Log.d(TAG, "imagenfile: $imageFile")
        //Log.d(TAG, "empleado: $empleado")
        if(imageFile != null && empleado != null){
            Log.d(TAG, "${imageFile != null && empleado != null}")
            Log.d(TAG, "imagenfile: $imageFile")
            Log.d(TAG, "empleado: $empleado")
            empleadoProvider?.update(imageFile!!, empleado!!)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d(TAG, "Response: $response")
                    Log.d(TAG, "Body: ${response.body()}")
                    saveEmpleadoInSesion(response.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }
        else{
            Toast.makeText(this, "La imagen no puede ser nula ni los datos de sesion", Toast.LENGTH_LONG).show()
        }

    }

    private fun saveEmpleadoInSesion(data: String) {
        Log.d("DAtos del registro", "$data")
        val empleado = gson.fromJson(data, Empleado::class.java)
        sharedPref?.save("empleado", empleado)
        Log.d("Accediendo con-", "Objeto es: ${empleado}")
        goToEmployeHome()
    }

    private fun getEmpleadoFromSesion() {
        //Log.d(TAG, "Antes de sentencia if")
        if(!sharedPref?.getData("empleado").isNullOrBlank()){
            //Si el usuario existe en sesion
            Log.d(TAG, "Empleado antes de Json:  ${sharedPref?.getData("empleado")}" )
            empleado = gson.fromJson(sharedPref?.getData("empleado"), Empleado::class.java)
            Log.d(TAG, "Empleado: $empleado")
        }
    }
    private fun goToEmployeHome() {
        val i = Intent(this, OperarioHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK ///ELIMINA EL HISTORIAL DE PANTALLAS
        startActivity(i)
    }
}