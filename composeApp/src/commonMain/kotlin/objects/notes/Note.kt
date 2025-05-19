package objects.notes

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    var text: String = ""
)