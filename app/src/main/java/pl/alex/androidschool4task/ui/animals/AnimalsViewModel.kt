package pl.alex.androidschool4task.ui.animals

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import pl.alex.androidschool4task.data.AnimalDao

class AnimalsViewModel @ViewModelInject constructor(private val animalDao: AnimalDao) :
    ViewModel() {
    var sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    private val animalFlow = sortOrder.flatMapLatest {
        animalDao.getAnimals(it)
    }
    val animals = animalFlow.asLiveData()
}

enum class SortOrder {
    BY_NAME,
    BY_AGE,
    BY_NICKNAME
}