package com.example.todolistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val priority: Priority,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)