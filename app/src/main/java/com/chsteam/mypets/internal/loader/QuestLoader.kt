package com.chsteam.mypets.internal.loader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.io.IOException

object QuestLoader {

    private val JACKSON_YAML = ObjectMapper(YAMLFactory())

    private fun processYamlData(data: Map<String, Any>) {

    }

    fun openDirectoryPicker(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        activity.startActivityForResult(intent, requestCode)
    }

    fun traverseDirectory(context: Context, uri: Uri) {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        documentFile?.let { directory ->
            for (file in directory.listFiles()) {
                if (file.isDirectory) {
                    traverseDirectory(context, file.uri)
                } else if (file.isFile) {
                    if((file.name ?: "" ).endsWith(".yml")) {
                        processYmlFileWithJackson(context, file.uri)
                    }
                }
            }
        }
    }

    fun traverseAssets(context: Context, path: String = "") {
        try {
            val assets = context.assets.list(path) ?: arrayOf()
            if (assets.isEmpty()) {
                if(path.endsWith(".yml")) {
                    val inputStream = context.assets.open(path)
                    val data: Map<String, Any> = JACKSON_YAML.readValue(inputStream, Map::class.java) as Map<String, Any>
                    processYamlData(data)
                }
            } else {
                for (asset in assets) {
                    val assetPath = if (path.isEmpty()) asset else "$path/$asset"
                    traverseAssets(context, assetPath)
                }
            }
        } catch (_: IOException) {
        }
    }

    fun processYmlFileWithJackson(context: Context, uri: Uri) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val data: Map<String, Any> = JACKSON_YAML.readValue(inputStream, Map::class.java) as Map<String, Any>
                processYamlData(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}