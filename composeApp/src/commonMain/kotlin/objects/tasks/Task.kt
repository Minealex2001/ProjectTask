package objects.tasks

import objects.notes.Note
import java.sql.Time
import java.util.*

class Task {
    var name: String
    var description: String
    var isCompleted: Boolean = false
    lateinit var date: Date
    lateinit var time: Time
    var notes: MutableList<Note> = mutableListOf()
    var estimatedTime: Time? = null

    var isProgramed: Boolean = false

    lateinit var currentTime: Time

    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun removeNote(note: Note) {
        notes.remove(note)
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

    fun addEstimatedTime(time: Time): Boolean {
        try {
            estimatedTime = time
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun removeEstimatedTime(time: Time): Boolean {
        try {
            estimatedTime = null
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun addCurrentTime(time: Time): Boolean {
        try {
            currentTime = time
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun addDate(date: Date): Boolean {
        try {
            this.date = date
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun addTime(time: Time): Boolean {
        try {
            this.time = time
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun removeDate(date: Date): Boolean {
        try {
            this.date = date
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun removeTime(time: Time): Boolean {
        try {
            this.time = time
            return true
        } catch (e: Exception) {
            return false
        }
    }


//    fun checkIfIsProgramed(): Boolean{
//
//    }
}