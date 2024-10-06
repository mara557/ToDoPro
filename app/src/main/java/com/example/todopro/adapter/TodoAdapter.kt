package com.example.todopro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todopro.R
import com.example.todopro.data.Todo
import com.example.todopro.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * RecyclerView Adapter for displaying Todo items.
 *
 * @param listener The listener for handling item click events.
 */
class TodoAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Todo, TodoAdapter.TodoViewHolder>(TodosComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    /**
     * Interface to handle click events from the adapter.
     */
    interface OnItemClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param todo The Todo item that was clicked.
         */
        fun onItemClick(todo: Todo)

        /**
         * Called when the options menu is clicked.
         *
         * @param todo The Todo item associated with the options menu.
         * @param position The position of the item in the list.
         * @param view The view that was clicked.
         */
        fun onOptionsClick(todo: Todo, position: Int, view: View)
    }

    /**
     * ViewHolder class for Todo items.
     */
    class TodoViewHolder(private val binding: ItemTodoBinding, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.taskTextView.text = todo.title
            binding.descriptionTextView.text = todo.description
            binding.categoryTextView.text = todo.category

            // Change appearance if the task is completed
            if (todo.isCompleted) {
                binding.taskTextView.paintFlags =
                    binding.taskTextView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                binding.descriptionTextView.paintFlags =
                    binding.descriptionTextView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.taskTextView.paintFlags =
                    binding.taskTextView.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.descriptionTextView.paintFlags =
                    binding.descriptionTextView.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            if (todo.completionTime != null) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val date = Date(todo.completionTime!!)
                binding.completionTimeTextView.text = "Due: ${sdf.format(date)}"
            } else {
                binding.completionTimeTextView.text = "Due: Not set"
            }

            binding.completionTimeTextView.text = if (todo.completionTime != null) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                "Due: ${sdf.format(Date(todo.completionTime!!))}"
            } else {
                "Due: Not set"
            }


            // Handle click on the entire item
            binding.root.setOnClickListener {
                listener.onItemClick(todo)
            }

            // Handle click on the options menu
            binding.optionsImageView.setOnClickListener {
                listener.onOptionsClick(todo, adapterPosition, binding.optionsImageView)
            }

            // Handle selection
            binding.checkbox.isChecked = todo.isSelected
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                listener.onItemClick(todo.apply { isSelected = isChecked })
            }
        }
    }

    /**
     * Comparator class for efficient RecyclerView updates.
     */
    class TodosComparator : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }
}
