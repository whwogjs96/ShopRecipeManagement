package com.jj.android.shoprecipemanagement.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.jj.android.shoprecipemanagement.R
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFDataFormat
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

object ExcelDataOutputUtil {


    fun getSavedViewIntent(fileName : String) : Intent {
        val mimeType = fileName.substring(fileName.indexOf(".") + 1, fileName.length)
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/$mimeType"
        intent.putExtra(Intent.EXTRA_TITLE, fileName)
        return intent
    }

    fun saveMaterialData(context : Context, outputStream: OutputStream?) : Boolean {
        if (outputStream != null) {
            val list = DatabaseCallUtil.getMaterialList()
            val workBook: Workbook = XSSFWorkbook()
            val sheet: Sheet = workBook.createSheet()
            val cellStyleDouble : CellStyle = workBook.createCellStyle()
            cellStyleDouble.dataFormat = HSSFDataFormat.getBuiltinFormat("#,##0.00")
            val cellStyleInt : CellStyle = workBook.createCellStyle()
            cellStyleInt.dataFormat = HSSFDataFormat.getBuiltinFormat("#,##0")
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
                cell = row.createCell(0)
                cell.setCellValue(list[index].name)
                cell = row.createCell(1)
                cell.cellStyle = cellStyleInt
                cell.setCellValue(list[index].unitPrice.toDouble())
                cell = row.createCell(2)
                cell.cellStyle = cellStyleInt
                cell.setCellValue(list[index].weight.toDouble())
                cell = row.createCell(3)
                cell.cellStyle = cellStyleDouble
                cell.setCellValue(list[index].unitPricePerGram)
            }
            workBook.write(outputStream)
            outputStream.close()
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
            }
            return true
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "저장에 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
            }
            return false
        }
    }

    fun saveProcessingMaterialData(context : Context, outputStream: OutputStream?) : Boolean {
        if(outputStream != null) {
            val list = DatabaseCallUtil.getProcessMaterialList()
            val workBook: Workbook = XSSFWorkbook()
            val sheet: Sheet = workBook.createSheet()
            val cellStyleDouble : CellStyle = workBook.createCellStyle()
            cellStyleDouble.dataFormat = HSSFDataFormat.getBuiltinFormat("#,##0.00")
            val cellStyleInt : CellStyle = workBook.createCellStyle()
            cellStyleInt.dataFormat = HSSFDataFormat.getBuiltinFormat("#,##0")
            var row = sheet.createRow(0)
            var cell = row.createCell(0)

            cell.setCellValue("재료 이름")
            cell = row.createCell(1)
            cell.setCellValue("1g당 단가")
            cell = row.createCell(2)
            cell.setCellValue("사용량")
            cell = row.createCell(3)
            cell.setCellValue("가격")
            for (index in list.indices) {
                row = sheet.createRow(index + 1)
                cell = row.createCell(0)
                cell.setCellValue(list[index].name)
                cell = row.createCell(1)
                cell.cellStyle = cellStyleDouble
                cell.setCellValue(list[index].unitPricePerGram)
                cell = row.createCell(2)
                cell.cellStyle = cellStyleInt
                cell.setCellValue(list[index].usage.toDouble())
                cell = row.createCell(3)
                cell.cellStyle = cellStyleDouble
                cell.setCellValue(list[index].price)
            }
            workBook.write(outputStream)
            outputStream.close()
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
            }
            return true
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "저장에 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
            }
            return false
        }
    }

    fun saveRecipeData(context : Context, outputStream: OutputStream?) : Boolean {
        if(outputStream != null) {
            val list = RecipeDatabaseCallUtil.getRecipeList()
            val workBook: Workbook = XSSFWorkbook()
            val sheet: Sheet = workBook.createSheet()
            val cellStyleDouble : CellStyle = workBook.createCellStyle()
            cellStyleDouble.dataFormat = HSSFDataFormat.getBuiltinFormat("#,##0.00")
            val cellStyleInt : CellStyle = workBook.createCellStyle()
            cellStyleInt.dataFormat = HSSFDataFormat.getBuiltinFormat("#,##0")
            var row = sheet.createRow(0)
            var cell = row.createCell(0)

            cell.setCellValue("재료 이름")
            cell = row.createCell(1)
            cell.setCellValue("1g당 단가")
            cell = row.createCell(2)
            cell.setCellValue("사용량")
            cell = row.createCell(3)
            cell.setCellValue("사용된 재료 개수")
            cell = row.createCell(4)
            cell.setCellValue("가격")
            for (index in list.indices) {
                row = sheet.createRow(index + 1)
                cell = row.createCell(0)
                cell.setCellValue(list[index].name)
                cell = row.createCell(1)
                cell.cellStyle = cellStyleDouble
                cell.setCellValue(list[index].unitPricePerGram)
                cell = row.createCell(2)
                cell.cellStyle = cellStyleInt
                cell.setCellValue(list[index].usage.toDouble())
                cell = row.createCell(3)
                cell.cellStyle = cellStyleInt
                cell.setCellValue(list[index].materialCount.toDouble())
                cell = row.createCell(4)
                cell.cellStyle = cellStyleDouble
                cell.setCellValue(list[index].price)
            }
            workBook.write(outputStream)
            outputStream.close()
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
            }
            return true
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "저장에 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
            }
            return false
        }
    }
}