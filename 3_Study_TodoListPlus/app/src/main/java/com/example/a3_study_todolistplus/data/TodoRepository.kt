package com.example.todolistapp.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: Flow<List<Todo>> = todoDao.getAllTodos()

    fun getTodosByPriority(priority: Priority): Flow<List<Todo>> {
        return todoDao.getTodosByPriority(priority)
    }

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun toggleComplete(todo: Todo) {
        todoDao.updateCompleted(todo.id, !todo.isCompleted)
    }
}