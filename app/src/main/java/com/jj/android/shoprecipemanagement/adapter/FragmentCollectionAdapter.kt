package com.jj.android.shoprecipemanagement.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jj.android.shoprecipemanagement.fragment.MaterialListFragment
import com.jj.android.shoprecipemanagement.fragment.ProcessingMaterialFragment
import com.jj.android.shoprecipemanagement.fragment.RecipeListFragment

class FragmentCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MaterialListFragment()
            1 -> ProcessingMaterialFragment()
            else -> RecipeListFragment()
        }
    }
}