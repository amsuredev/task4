package pl.alex.androidschool4task.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.alex.androidschool4task.ui.animals.SortOrder

@Dao
interface AnimalDao {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(animal: Animal)

    @Update
    suspend fun update(animal: Animal)

    @Delete
    suspend fun delete(animal: Animal)

    fun getAnimals(sortOrder: SortOrder): Flow<List<Animal>>{
        return when (sortOrder){
            SortOrder.BY_NAME -> getAnimalsSortedByName()
            SortOrder.BY_AGE -> getAnimalsSortedByAge()
            SortOrder.BY_NICKNAME -> getAnimalsSortedByNickname()
        }
    }

    @Query("select * from animal_table ORDER BY name ASC")
    fun getAnimalsSortedByName(): Flow<List<Animal>>

    @Query("select * from animal_table ORDER BY age ASC")
    fun getAnimalsSortedByAge(): Flow<List<Animal>>

    @Query("select * from animal_table ORDER BY nickName ASC")
    fun getAnimalsSortedByNickname(): Flow<List<Animal>>
}