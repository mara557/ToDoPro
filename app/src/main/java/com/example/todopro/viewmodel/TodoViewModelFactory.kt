package com.example.todopro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todopro.repository.TodoRepository

/**
 * Factory class to instantiate TodoViewModel with a TodoRepository.
 */
class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the given ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A newly created ViewModel.
     * @throws IllegalArgumentException if the modelClass is not assignable from TodoViewModel.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
