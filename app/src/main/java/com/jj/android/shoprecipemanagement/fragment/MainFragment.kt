package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.adapter.FragmentCollectionAdapter
import com.jj.android.shoprecipemanagement.databinding.FragmentMainBinding

class MainFragment :CommonFragment<FragmentMainBinding>(R.layout.fragment_main), View.OnClickListener {

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
        add("혼합 재료")
        add("레시피")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentBodyPager.adapter = FragmentCollectionAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.fragmentBodyPager) {tab, position ->
            tab.text = tabNameList[position]
        }.attach()

        binding.sideMenuButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.sideMenuButton -> {
                //이걸 이용해서 엑셀 관련 팝업창 띄워주면 될 듯
                binding.fragmentBodyPager.currentItem
            }
        }
    }


}