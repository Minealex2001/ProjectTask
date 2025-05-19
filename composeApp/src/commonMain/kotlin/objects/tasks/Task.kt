package objects.tasks

import kotlinx.serialization.Serializable
import objects.notes.Note

@Serializable
data class Task(
    var name: String = "",
    var description: String = "",
    var isCompleted: Boolean = false,
    var date: String = "", // ISO 8601 string (yyyy-MM-dd)
    var time: String = "", // HH:mm:ss
    var notes: List<Note> = emptyList(),
    var estimatedTime: String? = null, // HH:mm:ss
    var isProgramed: Boolean = false,
    var currentTime: String = "" // HH:mm:ss
) {
    fun addNote(note: Note) {
        notes += note
    }

    fun removeNote(note: Note) {
        notes -= note
    }

    fun getNote(index: Int): Note {
        return notes[index]
    }

    fun taskCompleted() {
        isCompleted = true
    }

    fun revertTaskCompleted() {
        isCompleted = false
    }

    fun addEstimatedTime(time: String): Boolean {
        return try {
            estimatedTime = time
            true
        } catch (e: Exception) {
            false
        }
    }

    fun removeEstimatedTime(): Boolean {
        return try {
            estimatedTime = null
            true
        } catch (e: Exception) {
            false
        }
    }

    fun addCurrentTime(time: String): Boolean {
        return try {
            currentTime = time
            true
        } catch (e: Exception) {
            false
        }
    }

    fun addDate(date: String): Boolean {
        return try {
            this.date = date
            true
        } catch (e: Exception) {
            false
        }
    }

    fun addTime(time: String): Boolean {
        return try {
            this.time = time
            true
        } catch (e: Exception) {
            false
        }
    }

    fun removeDate(): Boolean {
        return try {
            this.date = ""
            true
        } catch (e: Exception) {
            false
        }
    }

    fun removeTime(): Boolean {
        return try {
            this.time = ""
            true
        } catch (e: Exception) {
            false
        }
    }

//    fun checkIfIsProgramed(): Boolean{
//
//    }
}