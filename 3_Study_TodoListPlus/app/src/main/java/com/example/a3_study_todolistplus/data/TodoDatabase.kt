package com.example.a3_study_todolistplus.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

// Priority enum을 Int로 변환 (데이터베이스 저장용)
class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): Int {
        return priority.ordinal  // HIGH=0, MEDIUM=1, LOW=2
    }

    @TypeConverter
    fun toPriority(ordinal: Int): Priority {
        return Priority.values()[ordinal]
    }
}

// 데이터베이스 클래스
@Database(
    entities = [Todo::class],  // 사용할 테이블들
    version = 1,               // 데이터베이스 버전
    exportSchema = false
)
@TypeConverters(Converters::class)  // enum 변환기 등록
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        // 싱글톤 패턴 (데이터베이스는 하나만 만들기)
        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"  // 데이터베이스 파일 이름
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}