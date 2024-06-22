package com.seminariomanufacturadigital.recycleapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seminariomanufacturadigital.recycleapp.fragments.administrativo.notas.AdministrativoNotaStatusFragment
import com.seminariomanufacturadigital.recycleapp.fragments.operario.OperarioNotaStatusFragment

class AdmnistrativoTabsPagerAdapter (
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
                val StatusFragment = AdministrativoNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "PENDIENTE")
                val StatusFragment = AdministrativoNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "APROBADA")
                val StatusFragment = AdministrativoNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADA")
                val StatusFragment = AdministrativoNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            else -> return AdministrativoNotaStatusFragment()
        }
    }
}