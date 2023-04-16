package me.inflowsolutions.muzzexercise.data.db.mapper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.inflowsolutions.muzzexercise.data.SuspendingMapper
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto
import me.inflowsolutions.muzzexercise.domain.model.User

class UserDtoToUserMapper(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SuspendingMapper<UserDto, User>(coroutineDispatcher) {
    override suspend fun UserDto.toMappedEntity(): User = User(id, name, imageUrl)
}