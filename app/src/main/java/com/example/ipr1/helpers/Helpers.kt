package com.example.ipr1.helpers

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast

object Helpers {
    fun copyTextToClipboard(
        context: Context,
        text: String,
        textLabel: String? = "text label",
        successMessage: String? = "Copied"
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData
                .newPlainText(textLabel, text)
            clipboardManager.setPrimaryClip(clipData)
        } else {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            @Suppress("DEPRECATION")
            clipboardManager.text = text
        }
        if (successMessage != null) {
            Toast.makeText(
                context, successMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}