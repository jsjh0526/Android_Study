package com.example.a3_study_todolistplus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a3_study_todolistplus.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// AndroidViewModel = Application context가 필요한 ViewModel
class TodoViewModel(application: Application) : AndroidViewModel(application) {

    // Repository 초기화
    private val repository: TodoRepository

    init {
        val todoDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(todoDao)
    }

    // ===== 데이터 =====

    // 모든 할일 (실시간 업데이트)
    val allTodos: StateFlow<List<Todo>> = repository.allTodos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 우선도 높은 할일만
    val highPriorityTodos: StateFlow<List<Todo>> = repository.getTodosByPriority(Priority.HIGH)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 완료되지 않은 할일만
    val incompleteTodos: StateFlow<List<Todo>> = allTodos
        .map { todos -> todos.filter { !it.isCompleted } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ===== 기능 =====

    // 할일 추가
    fun addTodo(content: String, priority: Priority) {
        if (content.isBlank()) return

        viewModelScope.launch {
            val todo = Todo(
                content = content,
                priority = priority
            )
            repository.insert(todo)
        }
    }

    // 할일 수정
    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    // 할일 삭제
    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    // 완료 상태 토글
    fun toggleComplete(todo: Todo) {
        viewModelScope.launch {
            repository.toggleComplete(todo.id, !todo.isCompleted)
        }
    }

    // ID로 할일 찾기 (편집 화면용)
    suspend fun getTodoById(id: Int): Todo? {
        return repository.getTodoById(id)
    }
}