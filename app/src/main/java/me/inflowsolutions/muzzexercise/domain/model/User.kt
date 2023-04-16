package me.inflowsolutions.muzzexercise.domain.model

data class User(
    val id: Int,
    val name: String,
    val imageUrl: String
)

data class UserState(
    val currentUser: User,
    val otherUser: User
)