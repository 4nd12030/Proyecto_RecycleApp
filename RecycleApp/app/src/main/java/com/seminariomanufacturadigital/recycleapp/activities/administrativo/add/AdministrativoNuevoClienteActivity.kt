package com.seminariomanufacturadigital.recycleapp.activities.administrativo.add

import android.content.Context
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
import com.seminariomanufacturadigital.recycleapp.models.Cliente
import com.seminariomanufacturadigital.recycleapp.models.Empleado
import com.seminariomanufacturadigital.recycleapp.models.ResponseHttp
import com.seminariomanufacturadigital.recycleapp.models.Rol
import com.seminariomanufacturadigital.recycleapp.providers.ClientesProviders
import com.seminariomanufacturadigital.recycleapp.providers.EmpleadosProviders
import com.seminariomanufacturadigital.recycleapp.providers.TiposRolesProvider
import com.seminariomanufacturadigital.recycleapp.utils.SharedPref
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AdministrativoNuevoClienteActivity : AppCompatActivity() {

    var editTextNombre: EditText? = null
    var editTextTelefono: EditText? = null
    var editTextDireccion: EditText? = null
    var buttonGuardar: Button? = null
    var TAG = "AdministrativoNuevoClienteActivity"

    var sharedPref: SharedPref? = null
    val gson = Gson()
    var empleado: Empleado? = null

    var empleadosProvider: EmpleadosProviders? = null
    var clientesProvider: ClientesProviders? = null

    var perfiles = ArrayList<Rol>()
    var idPerfil = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrativo_nuevo_cliente)

        sharedPref = SharedPref(this)
        getEmpleadoFromSesion()


        editTextNombre = findViewById(R.id.edittxt_nombre)
        editTextTelefono = findViewById(R.id.edittxt_telefono)
        editTextDireccion = findViewById(R.id.edittxt_direccion)

        buttonGuardar = findViewById(R.id.btn_create_cliente)

        buttonGuardar?.setOnClickListener { guardarCliente() }

        clientesProvider = ClientesProviders(empleado?.sessionToken!!)
        empleadosProvider =EmpleadosProviders(empleado?.sessionToken)

    }


/////////////////////////////////////////////////////////////////

    fun guardarCliente(){
        //SE CREAN VARIABLES PARA LOS VALORES QUE INGRESE EL USUARIO
        //var imgFoto = imgViewFoto?.text.toString()
        var nombre =  editTextNombre?.text.toString()
        var telefono = editTextTelefono?.text.toString()
        var direccion = editTextDireccion?.text.toString()
        //var rolEmpleado = idPerfil

       val cliente = Cliente(razon_social = nombre, telefono = telefono, direccion =direccion)

        clientesProvider?.addCliente(cliente)?.enqueue(object: Callback<ResponseHttp> {
                    override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                        if(response.body()?.isSucces == true) {
                            //saveEmpleadoInSesion(response.body()?.data.toString())
                            resetForm()
                        }

                        Toast.makeText(this@AdministrativoNuevoClienteActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                    //En caso de error
                    override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                        Log.d(TAG, "Se produjo un error ${t.message}")
                        Toast.makeText(this@AdministrativoNuevoClienteActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
    }

    ////Funcion para limpiar formulario
    private fun resetForm() {
        editTextNombre?.setText("")
       editTextTelefono?.setText("")
       editTextDireccion?.setText("")
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