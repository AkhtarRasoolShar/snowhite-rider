import re

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "r") as f:
    content = f.read()

bad_signature_and_fetch = """@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderChatScreen(
    orderId: Int,
    mySenderType: String,
    mySenderId: Int,
    orderCode: String = "SW-$orderId",
    otherPartyName: String = if (mySenderType.equals("customer", ignoreCase = true)) "Delivery Captain" else "Customer",
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var inputMessage by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var isInitialLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isAgentTyping by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    val isCustomerView = mySenderType.equals("customer", ignoreCase = true)
    val otherPartyRole = if (isCustomerView) "SnoWhite Delivery Captain" else "SnoWhite Customer"

    // Real-time polling function: fetches messages for orderId
    suspend fun fetchMessages(silent: Boolean = false) {
        if (!silent && messages.isEmpty()) {
            isInitialLoading = true
        }
        try {
            val response = RetrofitClient.apiService.getChatMessages(
                action = "get_chat_messages",
                orderId = orderId
            )
            if (response.isSuccessful) {
                val body = response.body()
                val list = body?.data ?: body?.messages ?: emptyList()
                if (list.isNotEmpty()) {
                    // Update list only if there are new items or differences to prevent unnecessary UI jitter
                    if (list.size != messages.size || list.lastOrNull()?.id != messages.lastOrNull()?.id) {
                        messages.clear()
                        messages.addAll(list)
                        coroutineScope.launch {
                            delay(100)
                            if (messages.isNotEmpty()) {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    }
                }
            }
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e("CHAT_DEBUG", "Failed to fetch chat messages: ${e.localizedMessage}")
        } finally {
            isInitialLoading = false
            isRefreshing = false
        }
    }"""

good_signature_and_fetch = """@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderChatScreen(
    orderId: Int,
    mySenderType: String,
    mySenderId: Int,
    orderCode: String = "SW-$orderId",
    otherPartyName: String = if (mySenderType.equals("customer", ignoreCase = true)) "Delivery Captain" else "Customer",
    chatMessages: List<ChatMessage> = emptyList(),
    viewModel: com.example.ui.viewmodel.SnowWhiteViewModel,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var inputMessage by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var isInitialLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isAgentTyping by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    val isCustomerView = mySenderType.equals("customer", ignoreCase = true)
    val otherPartyRole = if (isCustomerView) "SnoWhite Delivery Captain" else "SnoWhite Customer"

    // Sync external messages from ViewModel State
    LaunchedEffect(chatMessages) {
        if (chatMessages.isNotEmpty()) {
            if (chatMessages.size != messages.size || chatMessages.lastOrNull()?.id != messages.lastOrNull()?.id) {
                messages.clear()
                messages.addAll(chatMessages)
                delay(100)
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }
            isInitialLoading = false
            isRefreshing = false
        }
    }

    // Wrap the viewModel fetch in a convenient local function
    fun fetchMessages(silent: Boolean = false) {
        if (!silent && messages.isEmpty()) {
            isInitialLoading = true
        }
        viewModel.fetchMessages(orderId)
        
        // Timeout to disable loading spinners if it gets stuck
        coroutineScope.launch {
            delay(1500)
            isInitialLoading = false
            isRefreshing = false
        }
    }"""

content = content.replace(bad_signature_and_fetch, good_signature_and_fetch)

bad_polling = """    // Polling effect every 3 seconds to fetch new messages in real-time
    LaunchedEffect(orderId) {
        fetchMessages(silent = false)
        while (isActive) {
            delay(3000L)
            fetchMessages(silent = true)
        }
    }"""

good_polling = """    // Polling effect every 3 seconds to fetch new messages in real-time
    LaunchedEffect(orderId) {
        while(true) {
            viewModel.fetchMessages(orderId)
            kotlinx.coroutines.delay(3000) // Poll every 3 seconds
        }
    }"""

content = content.replace(bad_polling, good_polling)

bad_is_me = """                    items(messages, key = { it.id ?: it.hashCode() }) { msg ->
                        val isMe = (msg.senderType?.lowercase() == mySenderType.lowercase()) ||
                                (msg.senderId == mySenderId)"""

good_is_me = """                    items(messages, key = { it.id ?: it.hashCode() }) { msg ->
                        // STRICT DIFFERENTIATION: Only match senderType to avoid crossing streams if senderIds happen to match
                        val isMe = msg.senderType?.equals(mySenderType, ignoreCase = true) == true"""

content = content.replace(bad_is_me, good_is_me)

with open("app/src/main/java/com/example/ui/screens/OrderChatScreen.kt", "w") as f:
    f.write(content)
print("Patched OrderChatScreen")
