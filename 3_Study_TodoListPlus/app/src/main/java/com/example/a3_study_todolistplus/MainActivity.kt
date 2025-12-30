package com.example.study_todo_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // 배경색을 디자인처럼 약간 회색으로 설정
                Surface(color = Color(0xFFF8F9FA)) {
                    TodoScreen()
                }
            }
        }
    }
}

// 메인 함수
@Composable
fun TodoScreen() {
    // 상태
    var currentQuote by remember { mutableStateOf("명언1") }
    var todoText by remember { mutableStateOf("") }
    var todoList by remember { mutableStateOf(listOf<String>()) } // 초기엔 빈 리스트

    // 변수에 담은 로직
    // 명언 바꾸기
    val onRefreshQuote = {
        val quotes = listOf("명언2", "명언3", "명언4", "명언5")
        currentQuote = quotes.random()
    }
    // 리스트 추가로직
    val onAddTask = {
        if (todoText.isNotBlank()) {
            todoList = todoList + todoText
            todoText = ""
        }
    }
    // 리스트 삭제로직
    val onDeleteTask = { task: String ->
        todoList = todoList - task
    }

    // 최상위 배치
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. 상단 헤더 (명언 + 타이틀)
        HeaderSection(quote = currentQuote, onRefreshClick = onRefreshQuote) // 명언 텍스트와 새로고침 버튼 = 새로고침 로직변수
        // 객체간 간격
        Spacer(modifier = Modifier.height(16.dp))
        // 2. 입력 칸
        InputSection(
            text = todoText, // 텍스트창 넘기기
            onTextChanged = { todoText = it }, // 텍스트에 입력들어오면 보여주기
            onAddClick = onAddTask // 버튼 클릭에 추가로직 변수
        )

        // 간격
        Spacer(modifier = Modifier.height(16.dp))

        // 3. 목록 칸 (화면 남은 공간 차지)
        ListSection(
            tasks = todoList, // string리스트
            onDeleteClick = onDeleteTask, // 삭제로직을 클릭에
            modifier = Modifier.weight(1f) // 남은 공간 전부 할당 비율
        )
    }
}

// [직원 1] 상단 헤더 (명언 + 타이틀)
@Composable
fun HeaderSection(quote: String, onRefreshClick: () -> Unit) { // 매개변수: 명언, 새로고침 변수 = 로직함수 변수 받기
    // 구조
    // Column { card { Row { Text / IconButton } } / Icon / Text / Text }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // (1) 보라색 명언 카드
        Card( //card 컴포저블 > 꾸며진 박스
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF7C4DFF)), // 딥 퍼플 색 설정 .cardColors()
            shape = RoundedCornerShape(12.dp) // 카드의 모양
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically // 중앙 정렬
            ) {
                Text( // 카드안의 텍스트 배치
                    text = "\" $quote \"",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f) // 가득채우기
                )
                IconButton(onClick = onRefreshClick) { // 아이콘만 담는 버튼 컴포저블
                    Icon(Icons.Default.Refresh, contentDescription = "새로고침", tint = Color.White) // 아이콘
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp)) // 간격설정

        Icon( // 목록 빈칸
            imageVector = Icons.Default.CheckCircle, // 체크 아이콘
            contentDescription = null,
            tint = Color(0xFF7C4DFF), // 색 설정
            modifier = Modifier.size(32.dp) // 크기 설정
        )
        Spacer(modifier = Modifier.height(8.dp)) // 간격설정
        Text(
            text = "할 일 관리",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Text(
            text = "오늘의 할 일을 관리해보세요",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

// 입력 섹션 (흰색 카드)
@Composable
fun InputSection(text: String, onTextChanged: (String) -> Unit, onAddClick: () -> Unit) { // 입력 텍스트 받기, 바뀐 텍스트 받는 로직, 클릭 추가 로직
    Card( // card 꾸며진 컴포저블
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 간격
    ) {
        Column(modifier = Modifier.padding(20.dp)) { // 새로로 배열
            Text("새로운 할 일 추가", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "해야 할 일을 입력하고 추가 버튼을 클릭하세요",
                fontSize = 12.sp,
                color = Color.Gray
            )
            // 간격
            Spacer(modifier = Modifier.height(16.dp))
            // 입력창 + 버튼 가로 배치
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField( // 텍스트 필드 = 입력창
                    value = text, // 텍스트 입력
                    onValueChange = onTextChanged, // 바꿔진 텍스트
                    placeholder = { Text("할 일을 입력하세요...", fontSize = 14.sp) }, // 힌트 같은거
                    modifier = Modifier.weight(1f), // 남은거 다쓰기
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
                // 버튼 생성
                Button(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF616161)), // 짙은 회색
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp) // 버튼 크기
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("추가")
                }
            }
        }
    }
}

// 목록 섹션 (흰색 카드 + Empty State 포함)
@Composable
fun ListSection(
    tasks: List<String>, // 받을 목록
    onDeleteClick: (String) -> Unit, // 삭제 로직
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "할 일 목록",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "${tasks.size}개의 할 일",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (tasks.isEmpty()) {
                // 할 일이 없을 때 (Empty State)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // 큰 회색 체크 아이콘
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFFEEEEEE),
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("아직 할 일이 없습니다.", color = Color.Gray, fontSize = 14.sp)
                        Text("위에서 새로운 할 일을 추가해보세요!", color = Color.LightGray, fontSize = 12.sp)
                    }
                }
            } else {
                // 할 일이 있을 때 (LazyColumn)
                LazyColumn { // 스크롤 가능한 column
                    items(tasks) { task -> // 아이템 나열
                        TodoItem(task, onDeleteClick) // 요소요소 추가
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(task: String, onDeleteClick: (String) -> Unit) { // 문자열과 삭제로직
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF7C4DFF),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = task, modifier = Modifier.weight(1f), fontSize = 14.sp)
        IconButton(onClick = { onDeleteClick(task) }) {
            Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.LightGray)
        }
    }
    Divider(color = Color(0xFFEEEEEE))
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoApp() {
    MaterialTheme {
        TodoScreen()
    }
}