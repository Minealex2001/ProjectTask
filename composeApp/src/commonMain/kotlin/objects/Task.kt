package objects

import java.sql.Time
import java.util.*

class Task {
    var name: String
    var description: String
    var completed: Boolean = false
    var date: Date
    var time: Time
    var notes: MutableList<Note> = mutableListOf()

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
}