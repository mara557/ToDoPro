package com.example.todopro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var description: String,
    var isCompleted: Boolean = false,
    var category: String = "General",
    var completionTime: Long? = null,
    var isSelected: Boolean = false // For selection in bulk actions
)