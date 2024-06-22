package com.seminariomanufacturadigital.recycleapp.utils

import android.content.Context
import android.content.SharedPreferences //para que es esta libreria?
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson

//nos ayuda a guardar la sesion del usuario con los datos que se encian en la peticiion al servidor
class SharedPref(activity: FragmentActivity) {
    private var prefs:  SharedPreferences? = null //paquete de android ,content

    init{
        prefs = activity.getSharedPreferences("com.seminariomanufacturadigital.recycleapp", Context.MODE_PRIVATE)//referencia del paquete principal
    }

    //METODO QUE GUARDA LA SESION (Convierte los datos del usuario a un objeto JSON)
    fun save(key: String, objeto: Any  ){
        try{
            val gson = Gson() //objeto de tipo Gson
            val json = gson.toJson(objeto) //Convierte el objeto que recibimos por parametro a un objeto JSON
            with(prefs?.edit()){
                this?.putString(key, json)
                this?.commit()
            }
        } catch (e: Exception) {
            Log.d("ERROR", "Err: ${e.message}")
        }
    }

    //METODO QUE OBTIENE LA DATA DEL METODO SAVE() Asi podemos obtener el objeto json que se crea en  el metodo save
    fun getData(key: String): String? {
        val data = prefs?.getString(key, "")
        return data
    }

    //funcion para cerrar sesion
    fun remove(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }
}




