package org.queststudios.projecttask.storage

import objects.tasks.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Paths
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun getPasswordFromSettings(): String {
    val userHome = System.getProperty("user.home")
    val dir = Paths.get(userHome, "Documents", "ProjectTask").toFile()
    val settingsFile = File(dir, "settings.json")
    if (!settingsFile.exists()) return "projecttask2024" // valor por defecto
    val json = settingsFile.readText()
    // Usa kotlinx.serialization para mayor robustez
    return try {
        kotlinx.serialization.json.Json.parseToJsonElement(json).jsonObject["password"]?.jsonPrimitive?.content ?: "projecttask2024"
    } catch (e: Exception) {
        "projecttask2024"
    }
}

fun saveTasksEncrypted(tasks: List<Task>, password: String = getPasswordFromSettings()) {
    val json = Json.encodeToString(ListSerializer(Task.serializer()), tasks)
    val key = password.padEnd(16, '0').substring(0, 16).toByteArray()
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encrypted = cipher.doFinal(json.toByteArray(Charsets.UTF_8))
    val base64 = Base64.getEncoder().encodeToString(encrypted)
    val userHome = System.getProperty("user.home")
    val dir = Paths.get(userHome, "Documents", "ProjectTask").toFile()
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "tasks.json.enc")
    file.writeText(base64)
}

fun loadTasksEncrypted(password: String = getPasswordFromSettings()): List<Task> {
    val userHome = System.getProperty("user.home")
    val file = Paths.get(userHome, "Documents", "ProjectTask", "tasks.json.enc").toFile()
    if (!file.exists()) return emptyList()
    val base64 = file.readText()
    val encrypted = Base64.getDecoder().decode(base64)
    val key = password.padEnd(16, '0').substring(0, 16).toByteArray()
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val json = cipher.doFinal(encrypted).toString(Charsets.UTF_8)
    return Json.decodeFromString(ListSerializer(Task.serializer()), json)
}
