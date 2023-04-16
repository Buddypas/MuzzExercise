package me.inflowsolutions.muzzexercise.domain.model

data class User(
    val id: Int,
    val name: String,
)

data class UserState(
    val currentUser: User? = null,
    val otherUser: User? = null
)