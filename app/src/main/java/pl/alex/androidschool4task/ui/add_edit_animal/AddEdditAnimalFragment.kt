package pl.alex.androidschool4task.ui.add_edit_animal

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_edit_animal.*
import kotlinx.coroutines.flow.collect
import pl.alex.androidschool4task.R
import pl.alex.androidschool4task.databinding.FragmentAddEditAnimalBinding

@AndroidEntryPoint
class AddEdditAnimalFragment : Fragment(R.layout.fragment_add_edit_animal) {
    private val viewModel: AddEdditViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddEditAnimalBinding.bind(view)
        binding.apply {
            textViewName.setText(viewModel.animalName)
            if (viewModel.animalAge >= 0) {
                textViewAge.setText(viewModel.animalAge.toString())
            }
            textviewNickname.setText(viewModel.animalNickname)

            textViewName.addTextChangedListener {
                viewModel.animalName = it.toString()
            }

            textViewAge.addTextChangedListener {
                if (it.toString() == "") {
                    viewModel.animalAge = -1
                } else
                    viewModel.animalAge = it.toString().toInt()
            }

            textviewNickname.addTextChangedListener {
                viewModel.animalNickname = it.toString()
            }

            fab_save_animal.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEdditViewModel.AddEditAnimalEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is AddEdditViewModel.AddEditAnimalEvent.GoBackEvent -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}