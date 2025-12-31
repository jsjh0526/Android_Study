package com.example.a3_study_todolistplus.data

import kotlinx.coroutines.flow.Flow

// Repository = DAO와 ViewModel 사이의 중간 계층
class TodoRepository(private val todoDao: TodoDao) {

    // 모든 할일 가져오기
    val allTodos: Flow<List<Todo>> = todoDao.getAllTodos()

    // 우선도별 할일
    fun getTodosByPriority(priority: Priority): Flow<List<Todo>> {
        return todoDao.getTodosByPriority(priority)
    }

    // ID로 할일 찾기
    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }

    // 할일 추가
    suspend fun insert(todo: Todo) {
        todoDao.insertTodo(todo)
    }

    // 할일 수정
    suspend fun update(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    // 할일 삭제
    suspend fun delete(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    // 완료 상태 변경
    suspend fun toggleComplete(id: Int, isCompleted: Boolean) {
        todoDao.updateCompleted(id, isCompleted)
    }
}