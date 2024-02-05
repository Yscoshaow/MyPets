package com.chsteam.mypets.core.config

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.api.Objective
import com.chsteam.mypets.api.Event
import com.chsteam.mypets.api.Variable
import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.Registries
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

        private val events: HashMap<EventID, Event> = HashMap()

        private val condition: HashMap<ConditionID, Condition> = HashMap()

        private val objective: HashMap<ObjectiveID, Objective> = HashMap()

        private val variable: HashMap<VariableID, Variable> = HashMap()

        fun getQuestPackage(name: String): QuestPackage? {
            return packages[name]
        }

        fun getConversation(conversationID: ConversationID): ConversationData? {
            return conversationData[conversationID]
        }

        fun getObjective(objectiveID: ObjectiveID): Objective? {
            return objective[objectiveID]
        }

        fun getEvent(eventID: EventID): Event? {
            return events[eventID]
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
                traverseAllData(pack, Uri.parse(path))
            } else {
                traverseAllData(pack, path)
            }
        }
    }

    private fun parserData(pack: QuestPackage, config: ConfigAccessorImpl) {
        val config = config.config

        val events: Config? = config.get<Config>("events")
        val conditions: Config? = config.get<Config>("conditions")
        val objectives: Config? = config.get<Config>("objectives")
        val conversations: Config? = config.get<Config>("conversations")

        events?.let {
            parserEvents(pack, it)
        }
        conditions?.let {
            parserConditions(pack, it)
        }
        objectives?.let {
            parserObjectives(pack, it)
        }
    }

    private fun parserConversations(pack: QuestPackage, config: Config) {
        
    }

    private fun parserEvents(pack: QuestPackage, config: Config) {
        config.valueMap().keys.forEach {
            val eventID = EventID(pack, it)
            val instruction = eventID.generateInstruction()
            val type = instruction.getPart(0) ?: return@forEach
            val eventClass = Registries.getEventClass(type) ?: return@forEach
            events[eventID] = eventClass.getConstructor(Instruction::class.java).newInstance(instruction)
        }
    }

    private fun parserConditions(pack: QuestPackage, config: Config) {
        config.valueMap().keys.forEach {
            val conditionID = ConditionID(pack, config.get(it))
            val instruction = conditionID.generateInstruction()
            val type = instruction.getPart(0) ?: return@forEach
            val conditionClass = Registries.getConditionClass(type) ?: return@forEach
            condition[conditionID] = conditionClass.getConstructor(Instruction::class.java).newInstance(instruction)
        }
    }

    private fun parserObjectives(pack: QuestPackage, config: Config) {
        config.valueMap().keys.forEach {
            val objectiveID = ObjectiveID(pack, config.get(it))
            val instruction = objectiveID.generateInstruction()
            val type = instruction.getPart(0) ?: return@forEach
            val objectiveClass= Registries.getObjectiveClass(type) ?: return@forEach
            objective[objectiveID] = objectiveClass.getConstructor(Instruction::class.java).newInstance(instruction)
        }
    }

    private fun traverseAllData(pack: QuestPackage, uri: Uri) {
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        documentFile?.let { directory ->
            for (file in directory.listFiles()) {
                if (file.isDirectory) {
                    traverseAllData(pack, file.uri)
                } else if (file.isFile) {
                    val name = file.name ?: ""
                    if(name.endsWith(".yml")) {
                        val config = ConfigAccessorImpl(context, uri.toString())
                        parserData(pack, config)
                    }
                }
            }
        }
    }

    private fun traverseAllData(pack: QuestPackage, path: String = "") {
        try {
            val assets = context.assets.list(path) ?: arrayOf()
            if (assets.isEmpty()) {
                if(path.endsWith(".yml")) {
                    val config = ConfigAccessorImpl(context, path)
                    parserData(pack, config)
                }
            } else {
                for (asset in assets) {
                    val assetPath = if (path.isEmpty()) asset else "$path/$asset"
                    traverseAllData(pack, assetPath)
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
                        packages[name] = QuestPackage(name, uri.toString().replace("/$PACK_FILE_NAME", ""))
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
                    packages[name] = QuestPackage(name, path.replace("/$PACK_FILE_NAME", ""))
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