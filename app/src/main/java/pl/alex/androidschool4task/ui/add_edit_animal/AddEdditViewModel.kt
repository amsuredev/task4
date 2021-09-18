package pl.alex.androidschool4task.ui.add_edit_animal

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.alex.androidschool4task.data.Animal
import pl.alex.androidschool4task.data.AnimalDao

class AddEdditViewModel @ViewModelInject constructor(
    private val animalDao: AnimalDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val animal = state.get<Animal>("animal")
    var animalName = state.get<String>("animal_name") ?: animal?.name ?: ""
    set(value){
        field = value
        state.set("animal_name", value)
    }
    var animalAge = state.get<Int>("animal_age") ?: animal?.age ?: -1
        set(value){
            field = value
            state.set("animal_age", value)
        }

    var animalNickname = state.get<String>("animal_nickname") ?: animal?.nickName ?: ""
        set(value){
            field = value
            state.set("animal_nickname", value)
        }

    private val addEditAnimalChannel = Channel<AddEditAnimalEvent>()
    val addEditTaskEvent = addEditAnimalChannel.receiveAsFlow()

    fun onSaveClick(){
        if (animalName.isBlank() || animalNickname.isBlank() || animalAge == -1){
            showInvalidInputMessage("Incorrect input")
        }
        else if (animal != null){
            val updateAnimal = animal.copy(name=animalName, age = animalAge, nickName = animalNickname)
            updateAnimal(updateAnimal)
        } else {
            val newAnimal = Animal(animalName, animalAge, animalNickname)
            createAnimal(newAnimal)
        }
    }

    private fun showInvalidInputMessage(message: String) {
        viewModelScope.launch {
            addEditAnimalChannel.send(AddEditAnimalEvent.ShowInvalidInputMessage(message))
        }
    }

    private fun createAnimal(newAnimal: Animal) {
        viewModelScope.launch {
            animalDao.insert(newAnimal)
            addEditAnimalChannel.send(AddEditAnimalEvent.GoBackEvent)
        }
    }

    private fun updateAnimal(updateAnimal: Animal) {
        viewModelScope.launch {
            animalDao.insert(updateAnimal)
            addEditAnimalChannel.send(AddEditAnimalEvent.GoBackEvent)
        }
    }

    sealed class AddEditAnimalEvent{
        data class ShowInvalidInputMessage(val msg: String): AddEditAnimalEvent()
        object GoBackEvent : AddEditAnimalEvent()
    }
}