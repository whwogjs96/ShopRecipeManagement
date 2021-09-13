package com.jj.android.shoprecipemanagement.eventbus

import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dto.MaterialData

class DummyEvent()

class MaterialModifyEvent(var data : MaterialData, val position: Int)

class MaterialDeleteEvent(var data : MaterialData)

class ProcessingMaterialModifyEvent(var dataDetail : ProcessingDetailListData, val position: Int)