package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.adapter.FragmentCollectionAdapter
import com.jj.android.shoprecipemanagement.databinding.FragmentMainBinding

class MainFragment :CommonFragment<FragmentMainBinding>(R.layout.fragment_main) {

    companion object{
        const val RECIPE_ADD = 0
        const val COST_ADD = 1
        const val PR_COST_ADD = 2
        const val EXCEL_SAVE = 3
        const val EXCEL_ROAD = 4
    }

    private val tabList: MutableList<Fragment> = ArrayList()
    private var tabNameList : ArrayList<String> = ArrayList<String>().apply {
        add("원가표")
        add("합성 재료")
        add("레시피")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentBodyPager.adapter = FragmentCollectionAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.fragmentBodyPager) {tab, position ->
            tab.text = tabNameList[position]
        }.attach()
    }
}