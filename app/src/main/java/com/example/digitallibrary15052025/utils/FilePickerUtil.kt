package com.logan.digitallibrary.utils

import android.content.Context
import android.content.Intent

fun Context.openFilePicker() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/epub+zip"
    }
    (this as? android.app.Activity)?.startActivityForResult(intent, 1)
}
