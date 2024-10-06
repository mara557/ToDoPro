package com.example.todopro

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todopro.adapter.TodoAdapter
import com.example.todopro.data.Todo
import com.example.todopro.data.TodoDatabase
import com.example.todopro.databinding.ActivityMainBinding
import com.example.todopro.databinding.DialogAddTaskBinding
import com.example.todopro.repository.TodoRepository
import com.example.todopro.viewmodel.TodoViewModel
import com.example.todopro.viewmodel.TodoViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), TodoAdapter.OnItemClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter

    // Move selectedCompletionTime to class level
    private var selectedCompletionTime: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TodoAdapter(this)
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        val todoDao = TodoDatabase.getDatabase(application).todoDao()
        val repository = TodoRepository(todoDao)
        todoViewModel = ViewModelProvider(this, TodoViewModelFactory(repository)).get(TodoViewModel::class.java)
        todoViewModel.allTodos.observe(this, Observer { todos ->
            todos?.let {
                adapter.submitList(it)
            }
        })

        ArrayAdapter.createFromResource(
            this,
            R.array.categories_with_all,
            android.R.layout.simple_spinner_item
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.filterSpinner.adapter = spinnerAdapter
        }

        binding.addTaskButton.setOnClickListener {
            showAddTaskDialog()
        }

        binding.optionButton.setOnClickListener {
            showOptionMenu(it)
        }

        binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                if (selectedCategory == "All") {
                    todoViewModel.allTodos.observe(this@MainActivity, Observer { todos ->
                        todos?.let { adapter.submitList(it) }
                    })
                } else {
                    todoViewModel.getTodosByCategory(selectedCategory).observe(this@MainActivity, Observer { todos ->
                        todos?.let { adapter.submitList(it) }
                    })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(todo: Todo) {
        todo.isCompleted = !todo.isCompleted
        todoViewModel.update(todo)
        Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsClick(todo: Todo, position: Int, view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.item_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    showEditDialog(todo)
                    true
                }
                R.id.menu_delete -> {
                    todoViewModel.delete(todo)
                    Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
        ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.dialogCategorySpinner.adapter = spinnerAdapter
        }

        dialogBinding.buttonSelectCompletionTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                    calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                    selectedCompletionTime = calendar.timeInMillis
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    dialogBinding.textViewCompletionTime.text = "Due: ${sdf.format(calendar.time)}"
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        AlertDialog.Builder(this)
            .setTitle("Add New Task")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val title = dialogBinding.dialogTitleEditText.text.toString().trim()
                val description = dialogBinding.dialogDescriptionEditText.text.toString().trim()
                val category = dialogBinding.dialogCategorySpinner.selectedItem.toString()

                if (title.isNotEmpty()) {
                    val todo = Todo(title = title, description = description, category = category, completionTime = selectedCompletionTime)
                    todoViewModel.insert(todo)
                    Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showEditDialog(todo: Todo) {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
        ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.dialogCategorySpinner.adapter = spinnerAdapter
        }

        dialogBinding.dialogTitleEditText.setText(todo.title)
        dialogBinding.dialogDescriptionEditText.setText(todo.description)
        dialogBinding.dialogCategorySpinner.setSelection(getCategoryIndex(todo.category))

        selectedCompletionTime = todo.completionTime

        if (todo.completionTime != null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = Date(todo.completionTime!!)
            dialogBinding.textViewCompletionTime.text = "Due: ${sdf.format(date)}"
        } else {
            dialogBinding.textViewCompletionTime.text = "Due: Not set"
        }

        dialogBinding.buttonSelectCompletionTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (selectedCompletionTime != null) {
                calendar.timeInMillis = selectedCompletionTime!!
            }
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                    calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                    selectedCompletionTime = calendar.timeInMillis
                    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    dialogBinding.textViewCompletionTime.text = "Due: ${sdf.format(calendar.time)}"
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = dialogBinding.dialogTitleEditText.text.toString().trim()
                val newDescription = dialogBinding.dialogDescriptionEditText.text.toString().trim()
                val newCategory = dialogBinding.dialogCategorySpinner.selectedItem.toString()

                if (newTitle.isNotEmpty()) {
                    todo.title = newTitle
                    todo.description = newDescription
                    todo.category = newCategory
                    todo.completionTime = selectedCompletionTime
                    todoViewModel.update(todo)
                    Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getCategoryIndex(category: String): Int {
        val categories = resources.getStringArray(R.array.categories)
        val index = categories.indexOf(category)
        return if (index != -1) index else 0
    }

    private fun showOptionMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_main)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_simulate_inserts -> {
                    todoViewModel.simulateConcurrentInserts()
                    Toast.makeText(this, "Simulating Concurrent Inserts", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_select_all -> {
                    todoViewModel.selectAll()
                    adapter.notifyDataSetChanged()
                    true
                }
                R.id.menu_deselect_all -> {
                    todoViewModel.deselectAll()
                    adapter.notifyDataSetChanged()
                    true
                }
                R.id.menu_delete_selected -> {
                    todoViewModel.deleteSelectedTodos()
                    Toast.makeText(this, "Selected Tasks Deleted", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
