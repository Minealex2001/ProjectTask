package objects.tasks

import objects.notes.Note
import java.sql.Time
import java.util.*

class Task {
    var name: String
    var description: String
    var isCompleted: Boolean = false
    var date: Date
    var time: Time
    var notes: MutableList<Note> = mutableListOf()
    var estimatedTime: Time? = null

    var isProgramed: Boolean = false

    lateinit var currentTime: Time

    constructor(name: String, description: String, date: Date, time: Time) {
        this.name = name
        this.description = description
        this.date = date
        this.time = time
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

//    fun checkIfIsProgramed(): Boolean{
//
//    }
}