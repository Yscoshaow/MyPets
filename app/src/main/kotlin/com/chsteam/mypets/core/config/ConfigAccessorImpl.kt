package com.chsteam.mypets.core.config

import android.content.Context
import android.net.Uri
import com.chsteam.mypets.api.config.ConfigAccessor
import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.io.ConfigParser
import com.electronwill.nightconfig.yaml.YamlFormat
import com.electronwill.nightconfig.yaml.YamlParser
import java.io.InputStream


/**
 * Represents a [YamlConfiguration] that is a file or a resource from a plugin.
 */
class ConfigAccessorImpl(private val context: Context, override val resourceFile: String) : ConfigAccessor {

    companion object {
        private val YAML_PARSER: ConfigParser<Config> = YamlParser(YamlFormat.defaultInstance())
        private fun loadFromYaml(inputStream: InputStream) : Config{
            val config: Config
            inputStream.use { inputStream ->
                config = YAML_PARSER.parse(inputStream)
            }
            return config
        }
    }

    override var config: Config = if(resourceFile.contains("://")) {
        val uri = Uri.parse(resourceFile)
        context.contentResolver.openInputStream(uri)!!.use { inputStream ->
            loadFromYaml(inputStream)
        }
    } else {
        loadFromYaml(context.assets.open(resourceFile))
    }

    override fun delete(): Boolean {
        return if(resourceFile.contains("://")) {
            val uri = Uri.parse(resourceFile)
            context.contentResolver.delete(uri, null)
            true
        } else {
            false
        }
    }

    override fun reload(): Boolean {
        if(resourceFile.contains("://")) {
            val uri = Uri.parse(resourceFile)
            context.contentResolver.openInputStream(uri)!!.use { inputStream ->
                config = loadFromYaml(inputStream)
            }
        } else {
            config = loadFromYaml(context.assets.open(resourceFile))
        }
        return true
    }

}