package com.jj.android.shoprecipemanagement.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.DialogMaterialProcessBinding
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.eventbus.MaterialDeleteEvent
import com.jj.android.shoprecipemanagement.result.MaterialDialogResult
import com.muddzdev.styleabletoast.StyleableToast
import org.greenrobot.eventbus.EventBus
import java.lang.NumberFormatException
import kotlin.math.roundToInt

class MaterialPrecessDialog(context: Context, val mode: Int, val data: MaterialData? = null) : Dialog(context), View.OnClickListener {

    private lateinit var binding : DialogMaterialProcessBinding
    lateinit var dialogResult : MaterialDialogResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_material_process, null, false)
        setContentView(binding.root)
        setCancelable(false)
        binding.cancelButton.setOnClickListener(this)
        binding.materialAddButton.setOnClickListener(this)
        binding.materialDeleteButton.setOnClickListener(this)
        modeSetting()
    }

    override fun onClick(v: View?) {
        when(v){
            binding.cancelButton -> dismiss()
            binding.materialAddButton -> {
                try {
                    val name = binding.materialNameEditText.text.toString()
                    val unitPrice = binding.materialUnitPriceEditText.text.toString().toInt()
                    val weight = binding.materialWeightEditText.text.toString().toInt()
                    val unitPricePerGram: Double =
                        ((unitPrice.toDouble() / weight.toDouble() * 100).roundToInt().toDouble() / 100)
                    if(name.trim().isEmpty()) {
                        StyleableToast.makeText(context, "재료명을 입력해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle)
                            .show()
                    } else if(unitPrice == 0 || weight == 0) {
                        StyleableToast.makeText(context, "금액이나 무게는 0이 될 수 없습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                    } else {
                        val materialData = MaterialData(name,unitPrice,weight,unitPricePerGram)
                        dialogResult.finish(materialData)
                        dismiss()
                    }
                } catch (e: NumberFormatException) {
                    StyleableToast.makeText(context, "가격 및 용량에는 숫자만 입력할 수 있습니다.", Toast.LENGTH_SHORT,R.style.errorToastStyle).show()
                }
            }
            binding.materialDeleteButton -> {
                if(data != null) {
                    EventBus.getDefault().post(MaterialDeleteEvent(data))
                    dismiss()
                }
            }
        }
    }

    fun setResult(result : MaterialDialogResult) { dialogResult = result }

    private fun modeSetting() {
        when(mode) {
            1 -> {//추가
                binding.materialNameEditText.isEnabled = true
                binding.titleText.text = "${context.getString(R.string.material)} ${context.getString(R.string.add)}"
                binding.materialAddButton.text = "${context.getString(R.string.add)}"
                binding.materialDeleteButton.visibility = View.GONE
            }
            2 -> {//수정
                if(data != null) {
                    binding.materialNameEditText.setText(data.name)
                    binding.materialUnitPriceEditText.setText(data.unitPrice.toString())
                    binding.materialWeightEditText.setText(data.weight.toString())
                }
                binding.titleText.text = "${context.getString(R.string.material)} ${context.getString(R.string.modify)}"
                binding.materialAddButton.text = "${context.getString(R.string.modify)}"
                binding.materialNameEditText.isEnabled = false
                binding.materialDeleteButton.visibility = View.VISIBLE
            }
        }
    }
}