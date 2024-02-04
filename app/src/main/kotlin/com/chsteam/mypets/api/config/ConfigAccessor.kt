package com.chsteam.mypets.api.config

import com.electronwill.nightconfig.core.Config
import java.io.File
import java.io.IOException


/**
 * This interface defines methods to load, get, save and delete a config file or a resource from a plugin jar.
 */
interface ConfigAccessor {

    /**
     * Gets the [Config] that was loaded by this [ConfigAccessor].
     *
     * @return the configuration.
     */
    val config: Config

    /**
     * Gets the [File], that is represented by this [ConfigAccessor].
     * Returns null, if the file is a resource from a plugin that is not saved as a file in the file system.
     *
     * @return the [File] if it exists
     */
    val resourceFile: String


    /**
     * Delete the file that is represented by this [ConfigAccessor].
     * This method does nothing if no configurationFile was provided in the constructor.
     *
     * @return Only returns true if the file was deleted and existed before.
     * @throws IOException thrown if the file could not be deleted.
     */
    @Throws(IOException::class)
    fun delete(): Boolean

    /**
     * Reloads from the file that is represented by this [ConfigAccessor].
     * This method does nothing if no configurationFile was provided in the constructor.
     *
     * @return Only returns true if the file was successfully reloaded.
     * @throws IOException thrown if the file could not be reloaded.
     */
    @Throws(IOException::class)
    fun reload(): Boolean
}