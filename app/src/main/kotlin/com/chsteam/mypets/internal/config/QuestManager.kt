package com.chsteam.mypets.internal.config

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.internal.conversation.ConversationData
import com.chsteam.mypets.internal.id.ConversationID
import com.chsteam.mypets.internal.permission.PermissionManager.getUriFromSharedPreferences
import com.chsteam.mypets.internal.permission.PermissionManager.hasPersistableUriPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class QuestManager(private val context: Context) {

    companion object {
        private const val PACK_FILE_NAME = "package.yml"

        private val packages: HashMap<String, QuestPackage> = HashMap()

        private val conversationData: HashMap<ConversationID, ConversationData> = HashMap()

        fun getQuestPackage(name: String): QuestPackage? {
            return packages[name]
        }

        fun getConversation(conversationID: ConversationID): ConversationData? {
            return conversationData[conversationID]
        }
    }


    init {
        CoroutineScope(Dispatchers.IO).launch {
            searchForPackages()
        }
    }

    suspend fun searchForPackages() {
        withContext(Dispatchers.IO) {
            val uri = getUriFromSharedPreferences(context)
            val permission = hasPersistableUriPermission(context, uri)
            if(permission && uri != null) {
                traverseDirectory(uri)
            }
            val sharedPreferences = context.getSharedPreferences("mypets_settings", Context.MODE_PRIVATE)
            if(sharedPreferences.getBoolean("default_quest", false)) {
                traverseAssets()
            }
        }
    }

    private fun traverseDirectory(uri: Uri) {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        documentFile?.let { directory ->
            for (file in directory.listFiles()) {
                if (file.isDirectory) {
                    traverseDirectory(file.uri)
                } else if (file.isFile) {
                    val name = file.name ?: ""
                    if (name == "") {
                        val config = ConfigAccessorImpl(context, uri.toString())
                        val name = config.config.get<String>("package.name")
                        packages[name] = QuestPackage(name, uri.toString())
                    }
                }
            }
        }
    }

    private fun traverseAssets(path: String = "") {
        try {
            val assets = context.assets.list(path) ?: arrayOf()
            if (assets.isEmpty()) {
                if(path.contains(PACK_FILE_NAME)) {
                    val config = ConfigAccessorImpl(context, path)
                    val name = config.config.get<String>("package.name")
                    packages[name] = QuestPackage(name, path)
                }
            } else {
                for (asset in assets) {
                    val assetPath = if (path.isEmpty()) asset else "$path/$asset"
                    traverseAssets(assetPath)
                }
            }
        } catch (_: IOException) {

        }
    }

}