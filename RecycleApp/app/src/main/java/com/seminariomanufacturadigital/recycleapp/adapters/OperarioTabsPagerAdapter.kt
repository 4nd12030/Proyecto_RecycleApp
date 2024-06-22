package com.seminariomanufacturadigital.recycleapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seminariomanufacturadigital.recycleapp.fragments.operario.OperarioNotaStatusFragment

class OperarioTabsPagerAdapter (
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
                bundle.putString("status", "GUARDADA")
                val StatusFragment = OperarioNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENVIADA")
                val StatusFragment = OperarioNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "APROBADA")
                val StatusFragment = OperarioNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADA")
                val StatusFragment = OperarioNotaStatusFragment()
                StatusFragment.arguments = bundle
                return StatusFragment
            }
            else -> return OperarioNotaStatusFragment()
        }
    }
}