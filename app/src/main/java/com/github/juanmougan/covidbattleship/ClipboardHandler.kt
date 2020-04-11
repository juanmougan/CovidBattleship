package com.github.juanmougan.covidbattleship

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardHandler(var context: Context) {

    /**
     * Copies a provided text to the clipboard, using a default label
     */
    fun copy(text: CharSequence) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(DEFAULT_LABEL, text)
        clipboard.primaryClip = clip
    }

    companion object {
        const val DEFAULT_LABEL = "label"
    }
}
