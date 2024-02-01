package com.chsteam.mypets.internal.loader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.IOException

object QuestLoader {

    fun openDirectoryPicker(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        activity.startActivityForResult(intent, requestCode)
    }

    fun traverseDirectory(context: Context, uri: Uri) {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        documentFile?.let { directory ->
            for (file in directory.listFiles()) {
                if (file.isDirectory) {
                    traverseDirectory(context, file.uri) // 递归遍历子目录
                } else if (file.isFile) {
                }
            }
        }
    }

    fun traverseAssets(context: Context, path: String = "") {
        try {
            val assets = context.assets.list(path) ?: arrayOf() // 获取当前路径下的所有文件和文件夹
            if (assets.isEmpty()) {
                // 如果是文件或空目录，可以在这里处理文件
            } else {
                for (asset in assets) {
                    val assetPath = if (path.isEmpty()) asset else "$path/$asset"
                    traverseAssets(context, assetPath) // 递归遍历子目录或文件
                }
            }
        } catch (e: IOException) {
            // 处理异常，可能是无法读取目录
        }
    }
}