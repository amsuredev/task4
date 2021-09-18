package pl.alex.androidschool4task.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.alex.androidschool4task.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Animal::class], version = 1)
abstract class AnimalDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao
    class CallBack @Inject constructor(
        private val database: Provider<AnimalDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().animalDao()
            applicationScope.launch {
                dao.insert(Animal("cat", 4, "Vasilisa"))
                dao.insert(Animal("cat", 22, "Tatsiana"))
            }
        }
    }
}