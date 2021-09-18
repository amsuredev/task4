package pl.alex.androidschool4task.ui.animals

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.alex.androidschool4task.R
import pl.alex.androidschool4task.databinding.FragmentAnimalsBinding
@AndroidEntryPoint
class AnimalsFragment : Fragment(R.layout.fragment_animals) {
    private val viewModel: AnimalsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAnimalsBinding.bind(view)
        val animalAdapter = AnimalAdapter()
        binding.apply {
            recyclerViewAnimals.apply {
                adapter = animalAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        viewModel.animals.observe(viewLifecycleOwner) {
            animalAdapter.submitList(it)
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

}