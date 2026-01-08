package com.example.todolistapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.data.Priority
import com.example.todolistapp.data.Todo
import com.example.todolistapp.viewmodel.TodoViewModel

@Composable
fun ListScreen(viewModel: TodoViewModel) {
    val allTodos by viewModel.allTodos.collectAsState()
    var sortBy by remember { mutableStateOf("최신순") }
    var showSortMenu by remember { mutableStateOf(false) }

    val sortedTodos = when (sortBy) {
        "우선도순" -> allTodos.sortedByDescending {
            when (it.priority) {
                Priority.HIGH -> 3
                Priority.MEDIUM -> 2
                Priority.LOW -> 1
            }
        }
        "오래된순" -> allTodos.sortedBy { it.createdAt }
        else -> allTodos.sortedByDescending { it.createdAt }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("할 일 목록", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("모든 할 일을 관리하세요", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),  // ← weight 추가!
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp).fillMaxHeight()) {  // ← fillMaxHeight 추가!
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("할 일 목록", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                "${sortedTodos.size}개의 할 일, ${sortedTodos.count { it.isCompleted }}개 완료",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Box {
                            IconButton(onClick = { showSortMenu = true }) {  // ← TextButton → IconButton
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.FilterList,
                                        "정렬",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(sortBy, fontSize = 12.sp)
                                }
                            }
                            DropdownMenu(showSortMenu, { showSortMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text("최신순") },
                                    onClick = {
                                        sortBy = "최신순"
                                        showSortMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("오래된순") },
                                    onClick = {
                                        sortBy = "오래된순"
                                        showSortMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("우선도순") },
                                    onClick = {
                                        sortBy = "우선도순"
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (sortedTodos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),  // ← weight 추가
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    null,
                                    tint = Color(0xFFEEEEEE),
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("등록된 할일이 없습니다", color = Color.Gray)
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {  // ← weight 추가!
                            items(sortedTodos.size) { index ->
                                TodoListItem(
                                    sortedTodos[index],
                                    { viewModel.toggleComplete(sortedTodos[index]) },
                                    { viewModel.deleteTodo(sortedTodos[index]) }
                                )
                                if (index < sortedTodos.size - 1) {
                                    HorizontalDivider(color = Color(0xFFEEEEEE))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoListItem(todo: Todo, onToggleComplete: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(todo.isCompleted, { onToggleComplete() })
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    todo.content,
                    fontSize = 14.sp,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                    color = if (todo.isCompleted) Color.Gray else Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (priorityText, priorityColor) = when (todo.priority) {
                        Priority.HIGH -> "높음" to Color(0xFFFF5252)
                        Priority.MEDIUM -> "보통" to Color(0xFFFFA726)
                        Priority.LOW -> "낮음" to Color(0xFF66BB6A)
                    }
                    Canvas(modifier = Modifier.size(8.dp)) { drawCircle(priorityColor) }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(priorityText, fontSize = 12.sp, color = priorityColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(formatDate(todo.createdAt), fontSize = 12.sp, color = Color.Gray)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "삭제", tint = Color.LightGray)
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < 60_000 -> "방금"
        diff < 3_600_000 -> "${diff / 60_000}분 전"
        diff < 86_400_000 -> "${diff / 3_600_000}시간 전"
        else -> java.text.SimpleDateFormat(
            "MM월 dd일",
            java.util.Locale.KOREAN
        ).format(java.util.Date(timestamp))
    }
}