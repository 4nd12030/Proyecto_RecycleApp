package com.seminariomanufacturadigital.recycleapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seminariomanufacturadigital.recycleapp.fragments.operario.OperarioNotaStatusFragment
import com.seminariomanufacturadigital.recycleapp.fragments.soporte.notas.SoporteNotaStatusFragment

class SoporteTabsPagerAdapter (
    FragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var numberOfTabs: Int
): FragmentStateAdapter(FragmentManager, lifecycle)
{
    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENVIADA")
                val StatusFragment = SoporteNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "GUARDADA")
                val StatusFragment = SoporteNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "PENDIENTE")
                val StatusFragment = SoporteNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
           3 -> {
                val bundle = Bundle()
                bundle.putString("status", "APROBADA")
                val StatusFragment = SoporteNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            4-> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADA")
                val StatusFragment = SoporteNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            else -> return SoporteNotaStatusFragment()
        }
    }
}