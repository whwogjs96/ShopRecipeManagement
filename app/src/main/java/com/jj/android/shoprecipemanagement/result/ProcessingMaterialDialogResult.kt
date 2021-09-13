package com.jj.android.shoprecipemanagement.result

import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData

interface ProcessingMaterialDialogResult {

    fun finish (dataDetail : ProcessingDetailListData)
}