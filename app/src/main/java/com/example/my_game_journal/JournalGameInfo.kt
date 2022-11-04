package com.example.my_game_journal

/**
 * This data class holds values for each detail in a game journal (saved by the user)
 */
data class JournalGameInfo(
    val id: Int = 0,
    val name: String = "",
    val cover_url: String? = null,
    val date_added: String = "",
    val completed: Boolean = false,
    val rating: Float = 0f,
    val journal_content: String = "",
    val note_content: String = ""
) {
    // Simply converts a rating of 9.5 to 95
    fun getRatingToHundred(): Int = (rating * 10).toInt()

    // If the name is too long to display, simply remove a bit of it and add a ... at the end
    fun getShortenedName(): String {
        var newName = name.toString()
        val maxLength = 25;
        if(newName.length >= maxLength) {
            newName = newName.removeRange(maxLength, newName.length)
            newName += "..."
        }
        return newName
    }
}