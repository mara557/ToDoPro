package com.example.todopro.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE category = :category ORDER BY id ASC")
    fun getTodosByCategory(category: String): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE isSelected = 1")
    suspend fun getSelectedTodos(): List<Todo>

    @Query("UPDATE todo_table SET isSelected = :isSelected WHERE id = :id")
    suspend fun updateSelection(id: Int, isSelected: Boolean)

    @Query("UPDATE todo_table SET isSelected = 1")
    suspend fun selectAll()

    @Query("UPDATE todo_table SET isSelected = 0")
    suspend fun deselectAll()
}
