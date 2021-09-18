package pl.alex.androidschool4task.ui.animals

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.alex.androidschool4task.data.Animal
import pl.alex.androidschool4task.data.AnimalDao

class AnimalsViewModel @ViewModelInject constructor(
    private val animalDao: AnimalDao
) :
    ViewModel() {
    private val animalsEventChannel = Channel<AnimalEvent>()
    val animalEvent = animalsEventChannel.receiveAsFlow()


    fun onAnimalClicked(animal: Animal) {
        viewModelScope.launch {
            animalsEventChannel.send(AnimalEvent.NavigateToEdditTaskScreen(animal))
        }
    }

    fun onAnimalSwiped(animal: Animal) {
        viewModelScope.launch {
            animalDao.delete(animal)
            animalsEventChannel.send(AnimalEvent.ShowUndoDeleteAnimalMessage(animal))
        }
    }

    fun undoDeleteClick(animal: Animal) {
        viewModelScope.launch {
            animalDao.insert(animal)
        }
    }

    fun onAddTaskClick() {
        viewModelScope.launch {
            animalsEventChannel.send(AnimalEvent.NavigateToAddAnimalScreen)
        }
    }

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

sealed class AnimalEvent {
    object NavigateToAddAnimalScreen: AnimalEvent()
    data class NavigateToEdditTaskScreen(val animal: Animal): AnimalEvent()
    data class ShowUndoDeleteAnimalMessage(val animal: Animal) : AnimalEvent()
}