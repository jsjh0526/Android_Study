package com.example.todolistapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.data.Priority
import com.example.todolistapp.data.Todo
import com.example.todolistapp.viewmodel.TodoViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun HomeScreen(viewModel: TodoViewModel) {
    val highPriorityTodos by viewModel.highPriorityTodos.collectAsState()
    var currentQuote by remember { mutableStateOf("작은 발걸음도 앞으로 나아가는 것입니다.") }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            QuoteCard(
                quote = currentQuote,
                onRefreshClick = {
                    currentQuote = listOf(
                        "작은 발걸음도 앞으로 나아가는 것입니다.",
                        "오늘 할 일을 내일로 미루지 마세요.",
                        "시작이 반이다.",
                        "천 리 길도 한 걸음부터.",
                        "완벽함보다 실행이 중요합니다."
                    ).random()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    null,
                    tint = Color(0xFF7C4DFF),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("할 일 관리", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("오늘의 할 일을 관리해보세요", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ← key() 추가로 독립!
            key("addTodoCard") {
                AddTodoCard(viewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            HighPriorityCard(
                todos = highPriorityTodos,
                onToggleComplete = { viewModel.toggleComplete(it) }
            )
        }
    }
}

@Composable
fun QuoteCard(quote: String, onRefreshClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(
                Brush.horizontalGradient(listOf(Color(0xFF9C27B0), Color(0xFF7C4DFF)))
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "\" $quote \"",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onRefreshClick) {
                    Icon(Icons.Default.Refresh, "새로고침", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun AddTodoCard(viewModel: TodoViewModel) {
    var text by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("새로운 할 일 추가", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "해야 할 일을 입력하고 추가 버튼을 클릭하세요",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("할 일을 입력하세요...", fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (text.isNotBlank()) {
                            viewModel.addTodo(text, selectedPriority)
                            text = ""
                            selectedPriority = Priority.MEDIUM
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616161))
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("추가")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("우선도:", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                PriorityButton("높음", Priority.HIGH, selectedPriority == Priority.HIGH) {
                    selectedPriority = Priority.HIGH
                }
                Spacer(modifier = Modifier.width(8.dp))
                PriorityButton("보통", Priority.MEDIUM, selectedPriority == Priority.MEDIUM) {
                    selectedPriority = Priority.MEDIUM
                }
                Spacer(modifier = Modifier.width(8.dp))
                PriorityButton("낮음", Priority.LOW, selectedPriority == Priority.LOW) {
                    selectedPriority = Priority.LOW
                }
            }
        }
    }
}

@Composable
fun PriorityButton(text: String, priority: Priority, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF7C4DFF) else Color(0xFFE0E0E0)
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        modifier = Modifier.height(32.dp)
    ) {
        Text(text, fontSize = 12.sp, color = if (selected) Color.White else Color.Black)
    }
}

@Composable
fun HighPriorityCard(todos: List<Todo>, onToggleComplete: (Todo) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("우선순위 높은 할 일", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("${todos.size}개의 중요한 할 일", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            if (todos.isEmpty()) {
                Text(
                    "우선순위 높은 할일이 없습니다",
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                // ← LazyColumn 추가 + heightIn으로 최대 높이 제한!
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
                ) {
                    items(todos.size) { index ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(todos[index].isCompleted, { onToggleComplete(todos[index]) })
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {  // ← weight 추가!
                                Text(
                                    todos[index].content,
                                    fontSize = 14.sp,
                                    textDecoration = if (todos[index].isCompleted) TextDecoration.LineThrough else null,
                                    maxLines = 2,  // ← 최대 2줄!
                                    overflow = TextOverflow.Ellipsis  // ← 넘으면 ... 표시!
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("높음", fontSize = 12.sp, color = Color(0xFFFF5252))
                            }
                        }
                        if (index < todos.size - 1) {
                            HorizontalDivider(
                                color = Color(0xFFEEEEEE),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}