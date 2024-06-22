package com.seminariomanufacturadigital.recycleapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.models.Empleado

import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class RegisterActivity : AppCompatActivity() {

    var TAG= "RegisterActivity"
    var editTxtNombre : EditText? = null
    var editTxtApePaterno: EditText? = null
    var editTxtApeMaterno: EditText? = null
    var editTxtTelefonoe : EditText? = null
    var editTxtIdEmpleado: EditText? = null
    var editTxtContrasena: EditText? = null
   //var radioButtonRol: RadioButton? = null
    var btnRegistro: Button? = null
    var imageViewGoToLogin: ImageView? = null

    //
    var empleadosProviders = EmpleadosProviders()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        editTxtNombre = findViewById(R.id.editTextNombre)
        editTxtApePaterno = findViewById(R.id.editTextApePaterno)
        editTxtApeMaterno = findViewById(R.id.editTextApeMaterno)
        editTxtTelefonoe = findViewById(R.id.editTextTelefono)
        editTxtIdEmpleado = findViewById(R.id.editTextIdEmpleado)
        editTxtContrasena = findViewById(R.id.editTextContrasena)
        btnRegistro = findViewById(R.id.btn_registro)
        imageViewGoToLogin = findViewById(R.id.imgview_gologin)

        //Evento que se disparan cuando se hace click sobre el objeto
        btnRegistro?.setOnClickListener{ registro() }
        imageViewGoToLogin?.setOnClickListener{ goToLogin() }
    }
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

    fun registro(){
        //SE CREAN VARIABLES PARA LOS VALORES QUE INGRESE EL USUARIO
        //var imgFoto = imgViewFoto?.text.toString()
        var nombre =  editTxtNombre?.text.toString()
        var apellidoPat = editTxtApePaterno?.text.toString()
        var apellidoMat = editTxtApeMaterno?.text.toString()
        var telefono = editTxtTelefonoe?.text.toString()
        var numEmpleado = editTxtIdEmpleado?.text.toString()
        var contrasena = editTxtContrasena?.text.toString()
        //var rolEmpleado = radioButtonRol?.text.toString()

        if(isValidForm(nombre = nombre,
                apellidoPat = apellidoPat,
                apellidoMat = apellidoMat,
                telefono = telefono,
                numEmpleado =numEmpleado,
                contrasena = contrasena)){
            val empleado = Empleado(
                nombre = nombre,
                apellidoPat = apellidoPat,
                apellidoMat = apellidoMat,
                telefono = telefono,
                numEmpleado =numEmpleado,
                contrasena = contrasena,
                isAvailable = true)

            empleadosProviders.register(empleado)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>,response: Response<ResponseHttp>) {

                    if(response.body()?.isSucces == true) {
                        saveEmpleadoInSesion(response.body()?.data.toString())
                        goToEmployeHome()
                    }

                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response:  $response")
                    Log.d(TAG, "Body: ${response.body()}")
                    Log.d(TAG,  "Losdatos son: ${empleado}")
                }

                //En caso de error
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    //FUNCIONES PARA GUARDAR LA SESION
    private fun saveEmpleadoInSesion(data: String) {
        Log.d("DAtos del registro", "$data")
        val sharedProferences = SharedPref(this)
        val gson = Gson()
        val empleado = gson.fromJson(data, Empleado::class.java)
        sharedProferences.save("empleado", empleado)
        Log.d("Accediendo con-", "Objeto es: ${empleado}")
    }

    //FUNCIONES  INTENT
    //INICIO DEL EMPLEADO OPERARIO
    fun goToEmployeHome(){
        val i = Intent(this, SaveImageActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK ///ELIMINA EL HISTORIAL DE PANTALLAS
        startActivity(i)
    }

    //REGRESA AL INICIO
    fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
     }

}


