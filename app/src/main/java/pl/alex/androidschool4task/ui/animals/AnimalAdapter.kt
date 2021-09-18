package pl.alex.androidschool4task.ui.animals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.alex.androidschool4task.data.Animal
import pl.alex.androidschool4task.databinding.ItemAnimalBinding

class AnimalAdapter(private val listener: OnAnimalClickListener) : ListAdapter<Animal, AnimalAdapter.AnimalViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding = ItemAnimalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class AnimalViewHolder(private val binding: ItemAnimalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val animal = getItem(position)
                        listener.onItemClick(animal)
                    }
                }
            }
        }

        fun bind(animal: Animal) {
            binding.apply {
                binding.textviewAge.text = animal.age.toString()
                binding.textviewName.text = animal.name
                binding.textviewSex.text = animal.nickName
            }
        }
    }

    interface OnAnimalClickListener{
        fun onItemClick(animal: Animal)
    }

    class DiffCallback : DiffUtil.ItemCallback<Animal>(){
        override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem == newItem
        }

    }
}