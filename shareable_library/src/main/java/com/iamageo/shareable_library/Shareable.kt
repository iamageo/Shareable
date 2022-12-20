package com.iamageo.shareable_library

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun AppCompatActivity.shareableFromView(view: View, fileName: String, shareImageTitle: String) {

    val bitmap = Bitmap.createBitmap(
        view.width,
        view.height, Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    view.draw(canvas)

    val dirPath: String = applicationContext.cacheDir.path
    val dir = File(dirPath)
    if (!dir.exists()) dir.mkdirs()
    val fileName = "${fileName}.png"
    val file = File(dirPath, fileName)

    try {
        val fOut = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()
    } catch (e: java.lang.Exception) {
        e.message?.let { Log.i("Shareable", it) }
    }

    val bitmapUri: Uri = FileProvider.getUriForFile(this, applicationContext.packageName, file)

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, bitmapUri)
        type = "image/png"
    }

    try {
        val shareIntent = Intent.createChooser(sendIntent, shareImageTitle)
        startActivity(shareIntent)
    } catch (e: Exception) {
        e.message?.let { Log.i("Shareable", it) }
    }
}

