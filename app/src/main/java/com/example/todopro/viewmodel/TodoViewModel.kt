package com.example.todopro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todopro.data.Todo
import com.example.todopro.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    val allTodos: LiveData<List<Todo>> = repository.getAllTodos()

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun update(todo: Todo) = viewModelScope.launch {
        repository.update(todo)
    }

    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun getTodosByCategory(category: String): LiveData<List<Todo>> {
        return repository.getTodosByCategory(category)
    }

    fun selectAll() = viewModelScope.launch {
        repository.selectAll()
    }

    fun deselectAll() = viewModelScope.launch {
        repository.deselectAll()
    }

    fun toggleSelection(todo: Todo) = viewModelScope.launch {
        repository.updateSelection(todo.id, !todo.isSelected)
    }

    fun deleteSelectedTodos() = viewModelScope.launch {
        val selectedTodos = repository.getSelectedTodos()
        selectedTodos.forEach { repository.delete(it) }
    }

    // Optional: Method to simulate concurrent inserts for testing
    fun simulateConcurrentInserts() = viewModelScope.launch {
        val todos = listOf(
            Todo(title = "Task 1", description = "Description 1", category = "Work"),
            Todo(title = "Task 2", description = "Description 2", category = "Personal"),
            Todo(title = "Task 3", description = "Description 3", category = "Home"),
            Todo(title = "Task 4", description = "Description 4", category = "Fitness"),
            Todo(title = "Task 5", description = "Description 5", category = "Shopping")
        )
        repository.simulateConcurrentInserts(todos)
    }
}
