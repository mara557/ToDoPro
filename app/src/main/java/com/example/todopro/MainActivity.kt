package com.example.todopro

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), TodoAdapter.OnItemClickListener {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.editText)
        addButton = findViewById(R.id.button)

        recyclerView = findViewById(R.id.list)
        adapter = TodoAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.allTodos.observe(this, Observer { todos ->
            todos?.let {
                adapter.setTodos(it)
            }
        })

        addButton.setOnClickListener {
            val task = editText.text.toString()
            if (!TextUtils.isEmpty(task)) {
                val todo = Todo(task = task)
                todoViewModel.insert(todo)
                editText.setText("")
            } else {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(todo: Todo) {
        // Optionally handle item click
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
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showEditDialog(todo: Todo) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Edit Task")
        val editText = EditText(this)
        editText.setText(todo.task)
        dialogBuilder.setView(editText)

        dialogBuilder.setPositiveButton("Save") { _, _ ->
            val newTask = editText.text.toString()
            if (!TextUtils.isEmpty(newTask)) {
                todo.task = newTask
                todoViewModel.update(todo)
            } else {
                Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        dialogBuilder.show()
    }
}
