package me.inflowsolutions.muzzexercise.domain.model

data class User(
    val id: Int? = null,
    val name: String,
    val imageUrl: String
)

// TODO: Extract
data class UserState(
    val currentUser: User? = null,
    val otherUser: User? = null
)