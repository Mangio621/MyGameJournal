package com.example.my_game_journal

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
    fun getRatingToHundred(): Int = (rating * 10).toInt()
}