package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.adapter.FragmentCollectionAdapter
import com.jj.android.shoprecipemanagement.databinding.FragmentMainBinding

class MainFragment :CommonFragment<FragmentMainBinding>(R.layout.fragment_main), View.OnClickListener {


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
                val popup = PopupMenu(requireContext(), v)
                val menuLayout = if(binding.fragmentBodyPager.currentItem == 0) R.menu.cost_excel_menu
                else if(binding.fragmentBodyPager.currentItem == 1) R.menu.process_material_excel_menu
                else R.menu.recipe_excel_menu
                requireActivity().menuInflater.inflate(menuLayout, popup.menu)
                popup.show()
            }
        }
    }


}