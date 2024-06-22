package com.seminariomanufacturadigital.recycleapp.fragments.soporte.notas

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.seminariomanufacturadigital.recycleapp.R
import com.seminariomanufacturadigital.recycleapp.adapters.SoporteTabsPagerAdapter

class SoporteNotasFragment : Fragment() {

    var  myView: View? = null
    var viewpage: ViewPager2? = null
    var tabLayout: TabLayout? = null
    var TAG = "SoporteNotasFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_soporte_notas, container, false)

        viewpage = myView?.findViewById(R.id.viewpager)
        tabLayout = myView?.findViewById(R.id.tab_layout)

        tabLayout?.setSelectedTabIndicatorColor(Color.BLACK)
        tabLayout?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        tabLayout?.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)

        val numberOfTabs =  5
        Log.d(TAG, "Elemetosdel adapter ${lifecycle}   ${numberOfTabs}")

        val adapter = SoporteTabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, numberOfTabs)
        viewpage?.adapter = adapter
        viewpage?.isUserInputEnabled = true

        TabLayoutMediator(tabLayout!!, viewpage!!) {tab, position ->
            when(position){
                0 -> {
                    tab.text = "Enviada"
                }
                1 -> {
                    tab.text =  "Guardada"
                }
                2 -> {
                    tab.text = "Pendiente"
                }
                3 -> {
                    tab.text = "Aprobada"
                }
               4 -> {
                    tab.text = "Entregada"
                }
            }

        }.attach()

        return myView
    }

}