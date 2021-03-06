package com.jj.android.shoprecipemanagement.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.adapter.MaterialListSpinnerAdapter
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.databinding.DialogProcessingMaterialProcessBinding
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.result.ProcessingMaterialDialogResult
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NumberFormatException

class MaterialSelectDialog(context: Context, val mode: Int, val dataDetail: ProcessingDetailListData? = null) :
    Dialog(context),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener,
    TextWatcher {

    private lateinit var binding : DialogProcessingMaterialProcessBinding
    lateinit var dialogResult : ProcessingMaterialDialogResult
    private var selectedPosition = -1
    var materialList = ArrayList<MaterialData>()
    var materialSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_processing_material_process, null, false)
        setContentView(binding.root)
        setCancelable(false)
        CoroutineScope(Dispatchers.Default).launch{
            materialList.addAll(DatabaseCallUtil.getMaterialList())
            materialSize = materialList.size
            DatabaseCallUtil.getProcessMaterialList().forEach {
                materialList.add(MaterialData(
                    it.name,
                    0,
                    0,
                    it.unitPricePerGram
                ))
            }
            withContext(Dispatchers.Main) {
                binding.materialListSpinner.adapter = MaterialListSpinnerAdapter(context, materialList)
                binding.materialListSpinner.onItemSelectedListener = this@MaterialSelectDialog
                setEvent()
                modeSetting()
            }
        }
    }

    private fun setEvent() {
        binding.cancelButton.setOnClickListener(this)
        binding.materialAddButton.setOnClickListener(this)
        binding.inputUsage.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.cancelButton -> dismiss()
            binding.materialAddButton -> {
                try {
                    val usage : Int = binding.inputUsage.text.toString().toInt()
                    val unitPricePerGram = materialList[selectedPosition].unitPricePerGram
                    dialogResult.finish(
                        ProcessingDetailListData(
                            id = dataDetail?.id ?: 0,
                            materialName = materialList[selectedPosition].name,
                            type = if(materialSize > selectedPosition) 1 else 2,
                            usage = usage,
                            unitPricePerGram = unitPricePerGram,
                            totalPrice = usage * unitPricePerGram,
                            index = 0
                        )
                    )

                } catch (e: NumberFormatException){
                    StyleableToast.makeText(context, "???????????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                }
                dismiss()
            }
        }
    }

    private fun modeSetting() {
        when(mode) {
            1 -> {//??????
                binding.titleText.text = "${context.getString(R.string.processingMaterial)} ${context.getString(R.string.add)}"
                binding.materialAddButton.text = "${context.getString(R.string.add)}"
            }
            2 -> {//??????
                if(dataDetail != null) {
                    for(index in 0 until materialList.size) {
                        if(materialList.get(index).name == dataDetail.materialName) {
                            selectedPosition = index
                            binding.materialListSpinner.setSelection(selectedPosition)
                            break
                        }
                    }
                    binding.inputUsage.setText(dataDetail.usage.toString())
                }
                binding.titleText.text = "${context.getString(R.string.processingMaterial)} ${context.getString(R.string.modify)}"
                binding.materialAddButton.text = "${context.getString(R.string.modify)}"

            }
        }
    }

    fun setResult(result : ProcessingMaterialDialogResult) { dialogResult = result }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedPosition = position
        calculatePrice()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        calculatePrice()
    }

    override fun afterTextChanged(s: Editable?) {}

    private fun calculatePrice() {
        try {
            binding.predictPrice.text = if (selectedPosition == -1) "0"
            else {
                String.format("%.2f", (materialList.get(selectedPosition).unitPricePerGram * binding.inputUsage.text.toString().toDouble()))
            }
        } catch (e: NumberFormatException) {
            binding.predictPrice.text = "0"
        }

    }
}