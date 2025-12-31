package com.example.a3_study_todolistplus.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// DAO = Database Access Object (데이터베이스 접근 객체)
@Dao
interface TodoDao {

    // 모든 할일 가져오기 (실시간 업데이트)
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<Todo>>

    // ID로 특정 할일 가져오기
    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    // 우선도별로 가져오기
    @Query("SELECT * FROM todos WHERE priority = :priority ORDER BY createdAt DESC")
    fun getTodosByPriority(priority: Priority): Flow<List<Todo>>

    // 할일 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    // 할일 수정
    @Update
    suspend fun updateTodo(todo: Todo)

    // 할일 삭제
    @Delete
    suspend fun deleteTodo(todo: Todo)

    // 완료 상태 토글
    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompleted(id: Int, isCompleted: Boolean)
}