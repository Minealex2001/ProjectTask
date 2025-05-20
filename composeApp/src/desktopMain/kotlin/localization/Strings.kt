package org.queststudios.projecttask.localization

import java.util.*
import java.io.InputStream

object Strings {
    private var bundle: ResourceBundle? = null

    fun load(language: String) {
        val locale = Locale(language)
        bundle = ResourceBundle.getBundle("org.queststudios.projecttask.resources.strings", locale)
    }

    fun get(key: String, vararg args: Any?): String {
        val value = bundle?.getString(key) ?: key
        return if (args.isNotEmpty()) value.format(*args) else value
    }
}
