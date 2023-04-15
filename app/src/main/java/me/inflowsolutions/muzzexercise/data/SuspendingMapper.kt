package me.inflowsolutions.muzzexercise.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

// TODO: Inject dispatchers
abstract class SuspendingMapper<in T, out R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    abstract suspend fun T.toMappedEntity(): R

    suspend fun map(model: T): R =
        withContext(coroutineDispatcher) {
            model.toMappedEntity()
        }

    suspend fun mapCollection(model: Collection<T>): Collection<R> =
        withContext(coroutineDispatcher) {
            model.map {
                map(it)
            }
        }

    suspend fun mapList(model: List<T>): List<R> =
        withContext(coroutineDispatcher) {
            model.map {
                map(it)
            }
        }
}