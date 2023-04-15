package me.inflowsolutions.muzzexercise.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.inflowsolutions.muzzexercise.data.db.dao.MessagesDao
import me.inflowsolutions.muzzexercise.data.db.dao.UsersDao
import me.inflowsolutions.muzzexercise.data.db.entity.MessageDto
import me.inflowsolutions.muzzexercise.data.db.entity.UserDto


@Database(entities = [UserDto::class, MessageDto::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class MuzzExerciseDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
    abstract fun messagesDao(): MessagesDao

//    class RoomCallback @Inject constructor(
//        private val database: Provider<MuzzExerciseDatabase>,
//        @ApplicationScope private val applicationScope: CoroutineScope
//    ) : RoomDatabase.Callback() {
//
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//
//            val categoriesDao = database.get().categoriesDao()
//            val transactionsDao = database.get().transactionsDao()
//
//            applicationScope.launch {
//                categoriesDao.insertAll(
//                    CategoryDto(
//                        categoryId = 1,
//                        categoryName = "Car",
//                        categoryType = "expense"
//                    ),
//                    CategoryDto(
//                        categoryId = 2,
//                        categoryName = "Health",
//                        categoryType = "expense"
//                    ),
//                    CategoryDto(
//                        categoryId = 3,
//                        categoryName = "Salary",
//                        categoryType = "income"
//                    )
//                )
//
//                transactionsDao.insertAll(
//                    TransactionDto(
//                        transactionAmount = 650.0,
//                        transactionDate = Date(),
//                        transactionCategoryId = 3,
//                        transactionBalanceAfter = 650.0,
//                        transactionDescription = "T1"
//                    ),
//                    TransactionDto(
//                        transactionAmount = -50.0,
//                        transactionDate = Date(),
//                        transactionCategoryId = 2,
//                        transactionBalanceAfter = 600.0,
//                        transactionDescription = "T2"
//                    ),
//                    TransactionDto(
//                        transactionAmount = -100.0,
//                        transactionDate = Date(),
//                        transactionCategoryId = 1,
//                        transactionBalanceAfter = 500.0,
//                        transactionDescription = "T3"
//                    )
//                )
//            }
//        }
//    }
}
