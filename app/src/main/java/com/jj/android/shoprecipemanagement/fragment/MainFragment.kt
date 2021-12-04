package com.jj.android.shoprecipemanagement.fragment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.adapter.FragmentCollectionAdapter
import com.jj.android.shoprecipemanagement.databinding.FragmentMainBinding
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*

class MainFragment : CommonFragment<FragmentMainBinding>(R.layout.fragment_main), View.OnClickListener, MenuItem.OnMenuItemClickListener {


    private val tabList: MutableList<Fragment> = ArrayList()
    private var tabNameList: ArrayList<String> = ArrayList<String>().apply {
        add("원가표")
        add("혼합 재료")
        add("레시피")
    }
    private val WRITE_REQUEST_CODE = 43

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentBodyPager.adapter = FragmentCollectionAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.fragmentBodyPager) { tab, position ->
            tab.text = tabNameList[position]
        }.attach()

        binding.sideMenuButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.sideMenuButton -> {
                //이걸 이용해서 엑셀 관련 팝업창 띄워주면 될 듯
                binding.fragmentBodyPager.currentItem
                val popup = PopupMenu(requireContext(), v)
                val menuLayout = if (binding.fragmentBodyPager.currentItem == 0) R.menu.cost_excel_menu
                else if (binding.fragmentBodyPager.currentItem == 1) R.menu.process_material_excel_menu
                else R.menu.recipe_excel_menu
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
                CoroutineScope(Dispatchers.IO).launch {
                    val list = DatabaseCallUtil.getMaterialList()
                    val workBook: Workbook = XSSFWorkbook()
                    val sheet: Sheet = workBook.createSheet()
                    var row = sheet.createRow(0)
                    var cell = row.createCell(0)

                    cell.setCellValue("재료 이름")
                    cell = row.createCell(1)
                    cell.setCellValue("단가")
                    cell = row.createCell(2)
                    cell.setCellValue("중량")
                    cell = row.createCell(3)
                    cell.setCellValue("1g당 단가")
                    for (index in list.indices) {
                        row = sheet.createRow(index + 1)
                        cell.setCellValue(list[index].name)
                        cell = row.createCell(1)
                        cell.setCellValue(list[index].unitPrice.toString())
                        cell = row.createCell(2)
                        cell.setCellValue(list[index].weight.toString())
                        cell = row.createCell(3)
                        cell.setCellValue(list[index].unitPricePerGram.toString())
                    }
                    try {
                        val fileName = "test.xlsx" // download url
                        val mimeType = fileName.substring(fileName.indexOf(".") + 1, fileName.length)
                        var intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.type = "*/$mimeType"
                        intent.putExtra(Intent.EXTRA_TITLE, fileName)
                        startActivityForResult(intent, WRITE_REQUEST_CODE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            R.id.processCostExcelLoad -> {

            }
            R.id.processCostExcelSave -> {

            }
            R.id.recipeExcelLoad -> {

            }
            R.id.recipeExcelSave -> {

            }
        }
        return false
    }

}