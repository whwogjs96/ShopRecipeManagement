package com.jj.android.shoprecipemanagement.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.DialogConfirmAlertBinding

class ContentsAlertDialog(context: Context) : AlertDialog(context), View.OnClickListener {

    private val binding by lazy { DataBindingUtil.inflate<DialogConfirmAlertBinding>(layoutInflater, R.layout.dialog_confirm_alert, null, false) }

    var doneEvent : ContentsAlertDialog.() -> Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.alertCancelButton.setOnClickListener(this)
        binding.alertCancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.alertDoneButton -> doneEvent()
        }
        dismiss()
    }

    fun setContent(content : String) {
        binding.contentText.text = content
    }
}