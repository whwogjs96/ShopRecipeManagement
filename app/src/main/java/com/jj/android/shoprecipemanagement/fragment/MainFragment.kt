package com.jj.android.shoprecipemanagement.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.forEach
import com.google.android.material.tabs.TabLayoutMediator
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.adapter.FragmentCollectionAdapter
import com.jj.android.shoprecipemanagement.databinding.FragmentMainBinding
import com.jj.android.shoprecipemanagement.util.ExcelDataOutputUtil
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*

class MainFragment : CommonFragment<FragmentMainBinding>(R.layout.fragment_main), View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private var tabNameList: ArrayList<String> = ArrayList<String>().apply {
        add("원가표")
        add("혼합 재료")
        add("레시피")
    }
    private val WRITE_REQUEST_CODE = 43

    private lateinit var costExcelSaveLauncher: ActivityResultLauncher<Intent>
    private lateinit var processCostExcelSaveLauncher: ActivityResultLauncher<Intent>
    private lateinit var recipeExcelSave: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentBodyPager.adapter = FragmentCollectionAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.fragmentBodyPager) { tab, position ->
            tab.text = tabNameList[position]
        }.attach()
        setActivityResultLauncher()
        binding.sideMenuButton.setOnClickListener(this)
    }

    private fun setActivityResultLauncher() {
        costExcelSaveLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.also { data ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val outputStream = data.data?.let { uri -> activity?.contentResolver?.openOutputStream(uri) }
                            ExcelDataOutputUtil.saveMaterialData(requireContext(), outputStream)
                        } catch (e: Exception) {
                            CoroutineScope(Dispatchers.Main).launch {
                                StyleableToast.makeText(requireContext(), "파일 확장자가 제대로 지정되었는지 확인해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                            }
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        processCostExcelSaveLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK){
                it.data?.also { data ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val outputStream = data.data?.let { uri -> activity?.contentResolver?.openOutputStream(uri) }
                            //ExcelDataOutputUtil.saveMaterialData(requireContext(), outputStream)
                            //여기 코드 추가
                        } catch (e: Exception) {
                            CoroutineScope(Dispatchers.Main).launch {
                                StyleableToast.makeText(requireContext(), "파일 확장자가 제대로 지정되었는지 확인해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                            }
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        recipeExcelSave = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK){
                it.data?.also { data ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val outputStream = data.data?.let { uri -> activity?.contentResolver?.openOutputStream(uri) }
                            //ExcelDataOutputUtil.saveMaterialData(requireContext(), outputStream)
                            //여기 코드 추가
                        } catch (e: Exception) {
                            CoroutineScope(Dispatchers.Main).launch {
                                StyleableToast.makeText(requireContext(), "파일 확장자가 제대로 지정되었는지 확인해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                            }
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.sideMenuButton -> {
                //이걸 이용해서 엑셀 관련 팝업창 띄워주면 될 듯
                binding.fragmentBodyPager.currentItem
                val popup = PopupMenu(requireContext(), v)
                val menuLayout = getCurrentPopUpMenu()
                requireActivity().menuInflater.inflate(menuLayout, popup.menu)
                popup.menu.forEach {
                    it.setOnMenuItemClickListener(this)
                }
                popup.show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.costExcelLoad -> {

            }
            R.id.costExcelSave -> {
                costExcelSaveLauncher.launch(ExcelDataOutputUtil.getSavedViewIntent())
            }
            R.id.processCostExcelLoad -> {

            }
            R.id.processCostExcelSave -> {
                processCostExcelSaveLauncher.launch(ExcelDataOutputUtil.getSavedViewIntent())
            }
            R.id.recipeExcelLoad -> {

            }
            R.id.recipeExcelSave -> {
                recipeExcelSave.launch(ExcelDataOutputUtil.getSavedViewIntent())
            }
        }
        return false
    }

    private fun getCurrentPopUpMenu() : Int {
        return when (binding.fragmentBodyPager.currentItem) {
            0 -> R.menu.cost_excel_menu
            1 -> R.menu.process_material_excel_menu
            else -> R.menu.recipe_excel_menu
        }
    }
}