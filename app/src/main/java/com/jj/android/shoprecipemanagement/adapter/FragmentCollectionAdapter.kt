package com.jj.android.shoprecipemanagement.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jj.android.shoprecipemanagement.fragment.MaterialListFragment
import com.jj.android.shoprecipemanagement.fragment.ProcessingMaterialFragment

class FragmentCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment
        when(position) {
            0 -> fragment = MaterialListFragment()
            1 -> fragment = ProcessingMaterialFragment()
            else -> fragment = MaterialListFragment()
        }

        return fragment
    }
}