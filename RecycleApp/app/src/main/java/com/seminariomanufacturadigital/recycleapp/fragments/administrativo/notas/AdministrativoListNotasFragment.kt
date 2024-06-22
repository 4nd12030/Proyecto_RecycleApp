package com.seminariomanufacturadigital.recycleapp.fragments.administrativo.notas

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
import com.seminariomanufacturadigital.recycleapp.adapters.AdmnistrativoTabsPagerAdapter
import com.seminariomanufacturadigital.recycleapp.adapters.OperarioTabsPagerAdapter


class AdministrativoListNotasFragment : Fragment() {

    var  myView: View? = null
    var viewpage: ViewPager2? = null
    var tabLayout: TabLayout? = null
    var TAG = "AdministrativoListNotasFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_operario_list_notas, container, false)

        viewpage = myView?.findViewById(R.id.viewpager)
        tabLayout = myView?.findViewById(R.id.tab_layout)

        tabLayout?.setSelectedTabIndicatorColor(Color.BLACK)
        tabLayout?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        tabLayout?.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)

        val numberOfTabs =  4
        Log.d(TAG, "Elemetos del adapter ${lifecycle}   ${numberOfTabs}")

        val adapter = AdmnistrativoTabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, numberOfTabs)
        viewpage?.adapter = adapter
        viewpage?.isUserInputEnabled = true

        TabLayoutMediator(tabLayout!!, viewpage!!) {tab, position ->
            when(position){
                0 -> {
                    tab.text = "EN ESPERA"
                }
                1 -> {
                    tab.text =  "PENDIENTE"
                }
                2 -> {
                    tab.text = "APROBADA"
                }
                3 -> {
                    tab.text = "ENTREGADA"
                }
            }

        }.attach()

        return myView
    }

}