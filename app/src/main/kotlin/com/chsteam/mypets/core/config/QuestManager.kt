package com.chsteam.mypets.core.config

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.api.Objective
import com.chsteam.mypets.api.QuestEvent
import com.chsteam.mypets.api.Variable
import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.conversation.ConversationData
import com.chsteam.mypets.core.id.ConditionID
import com.chsteam.mypets.core.id.ConversationID
import com.chsteam.mypets.core.id.EventID
import com.chsteam.mypets.core.id.ObjectiveID
import com.chsteam.mypets.core.id.VariableID
import com.chsteam.mypets.core.permission.PermissionManager.getUriFromSharedPreferences
import com.chsteam.mypets.core.permission.PermissionManager.hasPersistableUriPermission
import com.electronwill.nightconfig.core.Config
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

        private val events: HashMap<EventID, QuestEvent> = HashMap()

        private val condition: HashMap<ConditionID, Condition> = HashMap()

        private val objective: HashMap<ObjectiveID, Objective> = HashMap()

        private val variable: HashMap<VariableID, Variable> = HashMap()

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

            packages.values.forEach {
                loadAllDataFromPackage(it)
            }
        }
    }

    private suspend fun loadAllDataFromPackage(pack: QuestPackage) {
        withContext(Dispatchers.IO) {
            val path = pack.questPath
            if(path.contains(":///")) {
                traverseAllData(Uri.parse(path))
            } else {
                traverseAllData(path)
            }
        }
    }

    private fun parserData(config: ConfigAccessorImpl) {
        val config = config.config

        val events = config.get<Map<String, String>>("events")
        val conditions = config.get<Map<String, String>>("conditions")
        val objectives = config.get<Map<String, String>>("objectives")
        val conversations = config.get<Config>("conversations")
    }

    private fun traverseAllData(uri: Uri) {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        documentFile?.let { directory ->
            for (file in directory.listFiles()) {
                if (file.isDirectory) {
                    traverseAllData(file.uri)
                } else if (file.isFile) {
                    val name = file.name ?: ""
                    if(name.endsWith(".yml")) {
                        val config = ConfigAccessorImpl(context, uri.toString())
                        parserData(config)
                    }
                }
            }
        }
    }

    private fun traverseAllData(path: String = "") {
        try {
            val assets = context.assets.list(path) ?: arrayOf()
            if (assets.isEmpty()) {
                Toast.makeText(context, path, Toast.LENGTH_SHORT)
                if(path.endsWith(".yml")) {
                    val config = ConfigAccessorImpl(context, path)
                    parserData(config)
                }
            } else {
                for (asset in assets) {
                    val assetPath = if (path.isEmpty()) asset else "$path/$asset"
                    traverseAllData(assetPath)
                }
            }
        } catch (_: IOException) {

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