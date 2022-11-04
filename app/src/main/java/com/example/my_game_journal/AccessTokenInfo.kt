package com.example.my_game_journal

/**
 * This data class contains the token/authorization information to reach the IGDB API endpoints
 */
data class AccessTokenInfo(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)
