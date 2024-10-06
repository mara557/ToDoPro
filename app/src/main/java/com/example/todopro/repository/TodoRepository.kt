package com.example.todopro.repository

import androidx.lifecycle.LiveData
import com.example.todopro.data.Todo
import com.example.todopro.data.TodoDao
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class TodoRepository(private val todoDao: TodoDao) {

    // Semaphore to limit concurrent background tasks to 4
    private val semaphore = Semaphore(4)

    suspend fun insert(todo: Todo) {
        semaphore.withPermit {
            todoDao.insert(todo)
        }
    }

    suspend fun update(todo: Todo) {
        semaphore.withPermit {
            todoDao.update(todo)
        }
    }

    suspend fun delete(todo: Todo) {
        semaphore.withPermit {
            todoDao.delete(todo)
        }
    }

    fun getAllTodos(): LiveData<List<Todo>> = todoDao.getAllTodos()

    fun getTodosByCategory(category: String): LiveData<List<Todo>> = todoDao.getTodosByCategory(category)

    suspend fun getSelectedTodos(): List<Todo> = todoDao.getSelectedTodos()

    suspend fun selectAll() {
        semaphore.withPermit {
            todoDao.selectAll()
        }
    }

    suspend fun deselectAll() {
        semaphore.withPermit {
            todoDao.deselectAll()
        }
    }

    suspend fun updateSelection(id: Int, isSelected: Boolean) {
        semaphore.withPermit {
            todoDao.updateSelection(id, isSelected)
        }
    }

    // Optional: Method to simulate concurrent inserts for testing
    suspend fun simulateConcurrentInserts(todos: List<Todo>) {
        todos.forEach { todo ->
            semaphore.withPermit {
                todoDao.insert(todo)
            }
        }
    }
}
