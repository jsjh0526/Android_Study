package com.example.a3_study_todolistplus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// 우선도 enum
enum class Priority {
    HIGH,    // 높음
    MEDIUM,  // 보통
    LOW      // 낮음
}

// Todo 데이터 클래스
@Entity(tableName = "todos")  // Room 테이블로 만들기
data class Todo(
    @PrimaryKey(autoGenerate = true)  // ID 자동 생성
    val id: Int = 0,
    val content: String,              // 할일 내용
    val priority: Priority,           // 우선도
    val isCompleted: Boolean = false, // 완료 여부
    val createdAt: Long = System.currentTimeMillis()  // 생성 시간
)