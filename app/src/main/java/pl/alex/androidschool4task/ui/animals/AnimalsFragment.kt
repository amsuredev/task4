package pl.alex.androidschool4task.ui.animals

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.alex.androidschool4task.R
import pl.alex.androidschool4task.data.Animal
import pl.alex.androidschool4task.databinding.FragmentAnimalsBinding
@AndroidEntryPoint
class AnimalsFragment : Fragment(R.layout.fragment_animals), AnimalAdapter.OnAnimalClickListener {
    private val viewModel: AnimalsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAnimalsBinding.bind(view)
        val animalAdapter = AnimalAdapter(this)
        binding.apply {
            recyclerViewAnimals.apply {
                adapter = animalAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val animal = animalAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onAnimalSwiped(animal)
                }

            }).attachToRecyclerView(recyclerViewAnimals)
        }
        viewModel.animals.observe(viewLifecycleOwner) {
            animalAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.animalEvent.collect { event ->
                when (event){
                    is AnimalEvent.ShowUndoDeleteAnimalMessage -> {
                        Snackbar.make(requireView(), "Animal deleted", Snackbar.LENGTH_LONG).setAction("UNDO"){
                            viewModel.undoDeleteClick(event.animal)
                        }.show()
                    }
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_animals_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*return super.onOptionsItemSelected(item)*/
        return when(item.itemId){
            R.id.action_sort_by_name -> {
                viewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }
            R.id.action_sort_by_age -> {
                viewModel.sortOrder.value = SortOrder.BY_AGE
                true
            }
            R.id.action_sort_by_nickname -> {
                viewModel.sortOrder.value = SortOrder.BY_NICKNAME
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(animal: Animal) {
        viewModel.onAnimalClicked(animal)
    }

}