package com.seminariomanufacturadigital.recycleapp.activities


import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.activities.administrativo.home.AdministrativeHomeActivity
import com.seminariomanufacturadigital.recycleapp.activities.operario.home.OperarioHomeActivity
import com.seminariomanufacturadigital.recycleapp.activities.soporte.home.SoporteHomeActivity
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    //DECALARACION DE VARIABLES de cada objeto del activity
    var editTxtIdEmpleado: EditText? = null
    var editTxTContrasena: EditText? = null
    var btnLogin: Button? = null
    var imageViewGotoRegister: ImageView? = null

    var empleadoProvider = EmpleadosProviders()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //ASIGANCION a las VARIABLES
        editTxtIdEmpleado = findViewById(R.id.edittxt_idempleado)
        editTxTContrasena = findViewById(R.id.edittxt_contrasena)
        btnLogin = findViewById(R.id.btn_login)
        imageViewGotoRegister = findViewById(R.id.imgview_goregister)

        //LLAMADO AL METODO POR MEDIO DEL EVENTO ONCLICKLISTENER
        imageViewGotoRegister?.setOnClickListener{ goToRegister() }
        btnLogin?.setOnClickListener { login() }

        getEmployeFromSession()
    }

    //FUNCION QUE REALIZA EL BOTON DE  INICIO DE SESSION  RECIBIENDO LOS DATOS DEL USUARIOS
    private fun login(){
        val numEmpleado = editTxtIdEmpleado?.text.toString()
        val contrasena = editTxTContrasena?.text.toString()

        if(isValidForm(numEmpleado, contrasena)){
            empleadoProvider.login(numEmpleado, contrasena)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp> ) {
                    Log.d("MainActivity", "Respuesta: ${response.body()} ")
                    Log.d("MainActivity", "Datos enviados: ${numEmpleado}")
                    Log.d("MainActivity", "Datos enviados: ${contrasena}")
                    
                    if(response.body()?.isSucces == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        Log.d("MainActivity", "Datos recibidos: ${response.body()?.data.toString()}")
                        saveEmpleadoInSesion(response.body()?.data.toString())


                        //goToEmployeHome()
                    } else {
                        Toast.makeText(this@MainActivity,"Los datos son incorrectos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d("MainActivity", "Hubo un error: ${t.message}")
                    Toast.makeText(this@MainActivity, "Hubo un error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        } else {
            Toast.makeText(this@MainActivity, "Datos no validos", Toast.LENGTH_LONG).show()
        }
    }

    //FUNCION QUE NOS DIRIGE AL ACTIVITY del empleado logeado
    private fun goToEmployeHome() {
        val i = Intent(this, OperarioHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK ///ELIMINA EL HISTORIAL DE PANTALLAS
        startActivity(i)
    }
    private fun goToAdministrativoHome() {
        val i = Intent(this, AdministrativeHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK ///ELIMINA EL HISTORIAL DE PANTALLAS
        startActivity(i)
    }
    private fun goToSoporteHome() {
        val i = Intent(this, SoporteHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK ///ELIMINA EL HISTORIAL DE PANTALLAS
        startActivity(i)
    }
    private fun goToSelectRol() {
        val i = Intent(this, SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK ///ELIMINA EL HISTORIAL DE PANTALLAS
        startActivity(i)
    }

    //FUNCION QUE NOS DIRIGE AL ACTIVITY DE REGISTRO
    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FUNCIONES PARA EL INICIO DE SESION
    private fun saveEmpleadoInSesion(data: String) {
        val sharedProferences = SharedPref(this)
        val gson = Gson()
        val empleado = gson.fromJson(data, Empleado::class.java)
        sharedProferences.save("empleado", empleado)

        //Log.d("Accediendo con-", "Objeto es: ${empleado}")
        //Log.d("Tamano de array", "Roles: ${empleado.roles?.size}")
        if (empleado.roles?.size!! > 1 ) { //tiene mas de un rol
            Log.d("Dentro del if", "resultado: ${ empleado.roles?.size!! > 1 }")
            goToSelectRol()
        } else {
            goToEmployeHome()
        }
    }

    private fun getEmployeFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("empleado").isNullOrBlank()) {
            //Si le usuario existe en sesion
            val empleado = gson.fromJson(sharedPref.getData("empleado"), Empleado::class.java)
            Log.d("MainAcyivity", "Empleado: $empleado")
            Log.d("MainAcyivity", "Rol: ${sharedPref.getData("rol")}")
            if(!sharedPref.getData("rol").isNullOrBlank()) {
                //SI EL USUARIO SELECCIONO EL ROL
                val rol = sharedPref.getData("rol")?.replace("\"",  "")
                Log.d("MainAcyivity", "ROL $rol")

                when(rol) {
                    "ADMINISTRATIVO" -> goToAdministrativoHome()
                    "SOPORTE" -> goToSoporteHome()
                    "OPERARIO" -> goToEmployeHome()
                    else -> goToEmployeHome()
                }
            }

        }

    }

    //////////////////////////////////////////////////////////////

    //FUNCIONES DE VALIDACION
    fun isValidForm(idEmpleado: String, contrasena: String): Boolean {
        if(idEmpleado.isBlank()) {
            Toast.makeText(this, "Ingresa tu numero de  empleado", Toast.LENGTH_LONG).show()
            return false
        }
        if(contrasena.isBlank()){
            Toast.makeText(this, "Ingresa la contrtasena", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    //////////////////////////////////

}