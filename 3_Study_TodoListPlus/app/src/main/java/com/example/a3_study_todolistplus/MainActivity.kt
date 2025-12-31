@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.a3_study_todolistplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.a3_study_todolistplus.data.Priority
import com.example.a3_study_todolistplus.data.Todo
import com.example.a3_study_todolistplus.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush

// ===== MainActivity =====
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainApp()
                }
            }
        }
    }
}

// ===== 메인 앱 구조 =====
@Composable
fun MainApp() {
    val navController = rememberNavController()  // Navigation 컨트롤러
    val viewModel: TodoViewModel = viewModel()    // ViewModel (공유)

    // Scaffold = 앱 전체 레이아웃 (TopBar + BottomNav + Content)
    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        // Navigation 설정
        NavHost(
            navController = navController,
            startDestination = "home",  // 시작 화면
            modifier = Modifier.padding(paddingValues)
        ) {
            // 홈 탭
            composable("home") {
                HomeScreen(navController, viewModel)
            }

            // 리스트 탭
            composable("list") {
                ListScreen(navController, viewModel)
            }

            // 설정 탭
            composable("settings") {
                SettingsScreen()
            }

            // 편집 화면 (ID 전달!)
            composable(
                route = "edit/{todoId}",
                arguments = listOf(
                    navArgument("todoId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
                EditScreen(
                    navController = navController,
                    viewModel = viewModel,
                    todoId = todoId
                )
            }
        }
    }
}

// ===== BottomNavigationBar =====
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // 탭 목록
    val items = listOf(
        BottomNavItem("home", "홈", Icons.Default.Home),
        BottomNavItem("list", "리스트", Icons.Default.List),
        BottomNavItem("settings", "설정", Icons.Default.Settings)
    )

    // 현재 화면 확인
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // 백스택 정리
                        popUpTo("home") {  // 시작 화면 route로 직접 지정
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

// BottomNav 아이템 데이터 클래스
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// ===== HomeScreen (홈 탭) =====
@Composable
fun HomeScreen(navController: NavHostController, viewModel: TodoViewModel) {
    // ViewModel에서 데이터 가져오기
    val highPriorityTodos by viewModel.highPriorityTodos.collectAsState()

    // 로컬 상태 (명언, 입력창)
    var currentQuote by remember { mutableStateOf("작은 발걸음도 앞으로 나아가는 것입니다.") }
    var todoText by rememberSaveable { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    // 명언 새로고침
    val onRefreshQuote = {
        val quotes = listOf(
            "작은 발걸음도 앞으로 나아가는 것입니다.",
            "오늘 할 일을 내일로 미루지 마세요.",
            "시작이 반이다.",
            "천 리 길도 한 걸음부터.",
            "완벽함보다 실행이 중요합니다."
        )
        currentQuote = quotes.random()
    }

    // 할일 추가
    val onAddTask = {
        if (todoText.isNotBlank()) {
            viewModel.addTodo(todoText, selectedPriority)
            todoText = ""
            selectedPriority = Priority.MEDIUM
        }
    }

    // UI 구성
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 1. 명언 카드
            QuoteCard(quote = currentQuote, onRefreshClick = onRefreshQuote)

            Spacer(modifier = Modifier.height(16.dp))

            // 2. 타이틀
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF7C4DFF),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "할 일 관리",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "오늘의 할 일을 관리해보세요",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. 입력 섹션
            AddTodoCard(
                text = todoText,
                onTextChanged = { todoText = it },
                selectedPriority = selectedPriority,
                onPrioritySelected = { selectedPriority = it },
                onAddClick = onAddTask
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. 우선순위 높은 할일
            HighPriorityCard(
                todos = highPriorityTodos,
                onTodoClick = { todo ->
                    navController.navigate("edit/${todo.id}")
                },
                onToggleComplete = { todo ->
                    viewModel.toggleComplete(todo)
                }
            )
        }
    }
}

// ===== 명언 카드 =====
@Composable
fun QuoteCard(quote: String, onRefreshClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent  // ← 투명으로!
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF9C27B0),  // 진한 보라
                            Color(0xFF7C4DFF)   // 연한 보라
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\" $quote \"",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onRefreshClick) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "새로고침",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// ===== 할일 추가 카드 =====
@Composable
fun AddTodoCard(
    text: String,
    onTextChanged: (String) -> Unit,
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "새로운 할 일 추가",
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "해야 할 일을 입력하고 추가 버튼을 클릭하세요",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 입력창 + 버튼
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                TextField(
                    value = text,
                    onValueChange = onTextChanged,
                    placeholder = { Text("할 일을 입력하세요...", fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFFF5F5F5),
                        focusedContainerColor = androidx.compose.ui.graphics.Color(0xFFF5F5F5),
                        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                        focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                        unfocusedTextColor = androidx.compose.ui.graphics.Color.Black,
                        cursorColor = androidx.compose.ui.graphics.Color.Black
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onAddClick,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFF616161)
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("추가")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 우선도 선택
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("우선도:", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))

                PriorityButton(
                    text = "높음",
                    priority = Priority.HIGH,
                    selected = selectedPriority == Priority.HIGH,
                    onClick = { onPrioritySelected(Priority.HIGH) }
                )
                Spacer(modifier = Modifier.width(8.dp))

                PriorityButton(
                    text = "보통",
                    priority = Priority.MEDIUM,
                    selected = selectedPriority == Priority.MEDIUM,
                    onClick = { onPrioritySelected(Priority.MEDIUM) }
                )
                Spacer(modifier = Modifier.width(8.dp))

                PriorityButton(
                    text = "낮음",
                    priority = Priority.LOW,
                    selected = selectedPriority == Priority.LOW,
                    onClick = { onPrioritySelected(Priority.LOW) }
                )
            }
        }
    }
}

// ===== 우선도 버튼 =====
@Composable
fun PriorityButton(
    text: String,
    priority: Priority,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) {
                Color(0xFF7C4DFF)
            } else {
                Color(0xFFE0E0E0)
            }
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),  // ← 줄임!
        modifier = Modifier.height(32.dp)  // ← 높이 제한!
    ) {
        Text(
            text = text,
            fontSize = 12.sp,  // ← 폰트 줄임!
            color = if (selected) {
                Color.White
            } else {
                Color.Black
            }
        )
    }
}

// ===== 우선순위 높은 할일 카드 =====
@Composable
fun HighPriorityCard(
    todos: List<Todo>,
    onTodoClick: (Todo) -> Unit,
    onToggleComplete: (Todo) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "우선순위 높은 할 일",
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "${todos.size}개의 중요한 할 일",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (todos.isEmpty()) {
                Text(
                    "우선순위 높은 할일이 없습니다",
                    color = androidx.compose.ui.graphics.Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                todos.take(3).forEach { todo ->  // 최대 3개만
                    HighPriorityItem(
                        todo = todo,
                        onClick = { onTodoClick(todo) },
                        onToggleComplete = { onToggleComplete(todo) }
                    )
                }
            }
        }
    }
}

// ===== 우선순위 높은 할일 아이템 =====
@Composable
fun HighPriorityItem(
    todo: Todo,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isCompleted,
            onCheckedChange = { onToggleComplete() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = todo.content,
                fontSize = 14.sp,
                textDecoration = if (todo.isCompleted) {
                    androidx.compose.ui.text.style.TextDecoration.LineThrough
                } else null
            )
            Text(
                text = "높음",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color(0xFFFF5252)
            )
        }
    }
}

// ===== ListScreen (리스트 탭) =====
@Composable
fun ListScreen(navController: NavHostController, viewModel: TodoViewModel) {
    // ViewModel에서 모든 할일 가져오기
    val allTodos by viewModel.allTodos.collectAsState()

    // 정렬 상태
    var sortBy by remember { mutableStateOf("최신순") }
    var showSortMenu by remember { mutableStateOf(false) }

    // 정렬된 리스트
    val sortedTodos = when (sortBy) {
        "우선도순" -> allTodos.sortedByDescending {
            when (it.priority) {
                Priority.HIGH -> 3
                Priority.MEDIUM -> 2
                Priority.LOW -> 1
            }
        }
        "오래된순" -> allTodos.sortedBy { it.createdAt }
        else -> allTodos.sortedByDescending { it.createdAt }  // 최신순
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 헤더
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "할 일 목록",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "모든 할 일을 관리하세요",
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 리스트 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color.White
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // 제목 + 정렬
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "할 일 목록",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                "${sortedTodos.size}개의 할 일, ${sortedTodos.count { it.isCompleted }}개 완료",
                                fontSize = 12.sp,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                        }

                        // 정렬 버튼
                        Box {
                            TextButton(onClick = { showSortMenu = true }) {
                                Icon(
                                    Icons.Default.FilterList,
                                    contentDescription = "정렬",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(sortBy, fontSize = 12.sp)
                            }

                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false }
                            ) {
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

                    // 할일 리스트
                    if (sortedTodos.isEmpty()) {
                        // Empty State
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = androidx.compose.ui.graphics.Color(0xFFEEEEEE),
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "등록된 할일이 없습니다",
                                    color = androidx.compose.ui.graphics.Color.Gray
                                )
                            }
                        }
                    } else {
                        // LazyColumn으로 리스트 표시
                        androidx.compose.foundation.lazy.LazyColumn {
                            items(sortedTodos.size) { index ->
                                val todo = sortedTodos[index]
                                TodoListItem(
                                    todo = todo,
                                    onClick = { navController.navigate("edit/${todo.id}") },
                                    onToggleComplete = { viewModel.toggleComplete(todo) },
                                    onDelete = { viewModel.deleteTodo(todo) }
                                )
                                if (index < sortedTodos.size - 1) {
                                    HorizontalDivider(
                                        color = androidx.compose.ui.graphics.Color(0xFFEEEEEE)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ===== 리스트 아이템 =====
@Composable
fun TodoListItem(
    todo: Todo,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            // 체크박스
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 할일 내용
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.content,
                    fontSize = 14.sp,
                    textDecoration = if (todo.isCompleted) {
                        androidx.compose.ui.text.style.TextDecoration.LineThrough
                    } else null,
                    color = if (todo.isCompleted) {
                        androidx.compose.ui.graphics.Color.Gray
                    } else {
                        androidx.compose.ui.graphics.Color.Black
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 우선도 + 날짜
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    // 우선도 표시
                    val (priorityText, priorityColor) = when (todo.priority) {
                        Priority.HIGH -> "높음" to androidx.compose.ui.graphics.Color(0xFFFF5252)
                        Priority.MEDIUM -> "보통" to androidx.compose.ui.graphics.Color(0xFFFFA726)
                        Priority.LOW -> "낮음" to androidx.compose.ui.graphics.Color(0xFF66BB6A)
                    }

                    androidx.compose.foundation.Canvas(
                        modifier = Modifier.size(8.dp)
                    ) {
                        drawCircle(color = priorityColor)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = priorityText,
                        fontSize = 12.sp,
                        color = priorityColor
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 생성 시간
                    Text(
                        text = formatDate(todo.createdAt),
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }

            // 삭제 버튼
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "삭제",
                    tint = androidx.compose.ui.graphics.Color.LightGray
                )
            }
        }
    }
}

// ===== 날짜 포맷 함수 =====
fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "방금"
        diff < 3_600_000 -> "${diff / 60_000}분 전"
        diff < 86_400_000 -> "${diff / 3_600_000}시간 전"
        else -> {
            val date = java.util.Date(timestamp)
            val format = java.text.SimpleDateFormat("MM월 dd일", java.util.Locale.KOREAN)
            format.format(date)
        }
    }
}

// ===== SettingsScreen (설정 탭) =====
@Composable
fun SettingsScreen() {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 헤더
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "설정",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "앱 설정을 관리하세요",
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 디스플레이 섹션
            SettingsSection(title = "디스플레이") {
                var darkMode by remember { mutableStateOf(false) }

                SettingsItem(
                    icon = if (darkMode) Icons.Default.Nightlight else Icons.Default.LightMode,
                    title = "다크 모드",
                    subtitle = if (darkMode) "어두운 테마를 사용 중입니다" else "밝은 테마를 사용 중입니다",
                    trailing = {
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { darkMode = it }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 알림 섹션
            SettingsSection(title = "알림") {
                var notificationEnabled by remember { mutableStateOf(true) }

                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "알림 설정",
                    subtitle = if (notificationEnabled) "알림이 활성화되어 있습니다" else "알림이 비활성화되어 있습니다",
                    trailing = {
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = { notificationEnabled = it }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 앱 정보 섹션
            SettingsSection(title = "앱 정보") {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color(0xFF7C4DFF)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "할 일 관리 앱",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "버전 1.0.0",
                                fontSize = 12.sp,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "간단하고 효율적인 할 일 관리 앱입니다. 우선순위를 설정하고, 할 일을 체크하며 생산적인 하루를 보내세요!",
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Gray,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "개발",
                                fontSize = 12.sp,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                            Text(
                                "Figma Make",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(
                                "라이센스",
                                fontSize = 12.sp,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                            Text(
                                "MIT",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 저작권
            Text(
                "© 2025 할 일 관리. All rights reserved.",
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color.Gray,
                modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally)
            )
        }
    }
}

// ===== 설정 섹션 카드 =====
@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.White
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

// ===== 설정 아이템 =====
@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color(0xFF7C4DFF),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                subtitle,
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }

        trailing()
    }
}

// ===== EditScreen (편집 화면) =====
@Composable
fun EditScreen(
    navController: NavHostController,
    viewModel: TodoViewModel,
    todoId: Int
) {
    // Todo 데이터 가져오기
    var todo by remember { mutableStateOf<Todo?>(null) }
    var todoText by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var hasChanges by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Todo 로드
    LaunchedEffect(todoId) {
        val loadedTodo = viewModel.getTodoById(todoId)
        if (loadedTodo != null) {
            todo = loadedTodo
            todoText = loadedTodo.content
            selectedPriority = loadedTodo.priority
        }
    }

    // 변경사항 감지
    LaunchedEffect(todoText, selectedPriority) {
        if (todo != null) {
            hasChanges = todoText != todo!!.content || selectedPriority != todo!!.priority
        }
    }

    // 뒤로가기 처리 (변경사항 있으면 확인)
    androidx.activity.compose.BackHandler(enabled = hasChanges) {
        showExitDialog = true
    }

    // 확인 다이얼로그
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("변경사항 저장 안됨") },
            text = { Text("저장하지 않고 나가시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    navController.popBackStack()
                }) {
                    Text("나가기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

    // UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("할일 편집") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasChanges) {
                            showExitDialog = true
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF7C4DFF),
                    titleContentColor = androidx.compose.ui.graphics.Color.White,
                    navigationIconContentColor = androidx.compose.ui.graphics.Color.White
                )
            )
        }
    ) { paddingValues ->
        if (todo == null) {
            // 로딩 중
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // 편집 카드
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.ui.graphics.Color.White
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "할 일 내용",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 텍스트 입력
                        TextField(
                            value = todoText,
                            onValueChange = { todoText = it },
                            placeholder = { Text("할 일을 입력하세요...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFFF5F5F5),
                                focusedContainerColor = androidx.compose.ui.graphics.Color(0xFFF5F5F5),
                                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "우선도",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 우선도 선택
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            PriorityButton(
                                text = "높음",
                                priority = Priority.HIGH,
                                selected = selectedPriority == Priority.HIGH,
                                onClick = { selectedPriority = Priority.HIGH }
                            )

                            PriorityButton(
                                text = "보통",
                                priority = Priority.MEDIUM,
                                selected = selectedPriority == Priority.MEDIUM,
                                onClick = { selectedPriority = Priority.MEDIUM }
                            )

                            PriorityButton(
                                text = "낮음",
                                priority = Priority.LOW,
                                selected = selectedPriority == Priority.LOW,
                                onClick = { selectedPriority = Priority.LOW }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 저장 버튼
                Button(
                    onClick = {
                        if (todoText.isNotBlank()) {
                            val updatedTodo = todo!!.copy(
                                content = todoText,
                                priority = selectedPriority
                            )
                            viewModel.updateTodo(updatedTodo)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFF7C4DFF)
                    ),
                    enabled = todoText.isNotBlank() && hasChanges
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("저장하기")
                }
            }
        }
    }
}