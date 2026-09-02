package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirebaseAuthService
import com.example.data.FirestoreRepository
import com.example.model.AllowedAsset
import com.example.model.BotMode
import com.example.model.BotStatus
import com.example.model.LiveTrade
import com.example.model.NavigationTab
import com.example.model.SignalType
import com.example.model.TradeActionDirection
import com.example.model.TradingPair
import com.example.model.TradingRobot
import com.example.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class TradingUiState(
    val currentTab: NavigationTab = NavigationTab.TRADE,
    val userProfile: UserProfile = UserProfile(),
    val selectedRobot: TradingRobot = initialRobots.first(),
    val robotList: List<TradingRobot> = initialRobots,
    val tradingPairs: List<TradingPair> = initialPairs,
    val allowedAssets: List<AllowedAsset> = initialAssets,
    val openTrades: List<LiveTrade> = initialTrades,
    val isAutoTradingActive: Boolean = true,
    val latencyMs: Int = 12,
    val showConnectDialog: Boolean = false,
    val showBotDetailSheet: Boolean = false,
    val showFloatingWidget: Boolean = false,
    val showAuthDialog: Boolean = false,
    val widgetTab: String = "STATUS", // STATUS or MODE
    val activeNotification: String? = null
)

private val initialRobots = listOf(
    TradingRobot(
        id = "fx_killer_v16",
        name = "FX KILLER scalprer",
        version = "v1.6",
        author = "TKFXKILLER",
        subtitle = "BIMZ FOREST KILLER",
        status = BotStatus.ACTIVE,
        mode = BotMode.ATTACK,
        accountId = "336901145",
        server = "FP Markets-Live",
        platform = "MetaTrader 5",
        dailyProfitZar = 1850.50,
        totalProfitZar = 28450.00,
        winRate = 94.2,
        tradesToday = 21,
        activePairsCount = 1,
        riskPercent = 1.0,
        lotSize = 0.01,
        trailingStop = true,
        newsFilter = true,
        allowedSymbolIds = listOf("XAUUSDm")
    ),
    TradingRobot(
        id = "exis_v10",
        name = "Exis",
        version = "V1.0.0",
        author = "TKFXKILLER",
        subtitle = "GRID ALGORITHM PRO",
        status = BotStatus.ACTIVE,
        mode = BotMode.NORMAL,
        accountId = "884029112",
        server = "Exness-Real21",
        platform = "MetaTrader 5",
        dailyProfitZar = 940.00,
        totalProfitZar = 14200.00,
        winRate = 88.5,
        tradesToday = 12,
        activePairsCount = 3,
        riskPercent = 1.5,
        lotSize = 0.02,
        trailingStop = true,
        newsFilter = false,
        allowedSymbolIds = listOf("EURUSD", "GBPUSD", "USDJPY")
    ),
    TradingRobot(
        id = "samurai_ea_v24",
        name = "Samurai Scalping EA",
        version = "V2.4 Pro",
        author = "Vena",
        subtitle = "HIGH FREQUENCY SCALPER",
        status = BotStatus.PAUSED,
        mode = BotMode.NORMAL,
        accountId = "552019482",
        server = "Tradeport-LiveMT5",
        platform = "MetaTrader 5",
        dailyProfitZar = 1420.50,
        totalProfitZar = 18450.00,
        winRate = 91.4,
        tradesToday = 14,
        activePairsCount = 4,
        riskPercent = 1.5,
        lotSize = 0.05,
        trailingStop = true,
        newsFilter = true,
        allowedSymbolIds = listOf("XAUUSDm", "EURUSD", "GBPUSD", "US30")
    )
)

private val initialAssets = listOf(
    AllowedAsset(
        symbol = "XAUUSDm",
        displayName = "Gold (Mini Spot / ZAR)",
        lotSize = 0.01,
        action = TradeActionDirection.BOTH,
        isAllowed = true,
        currentPrice = 5110.004,
        changePercent = +0.84,
        spreadPips = 0.8
    ),
    AllowedAsset(
        symbol = "EURUSD",
        displayName = "Euro / US Dollar",
        lotSize = 0.02,
        action = TradeActionDirection.BUY_ONLY,
        isAllowed = true,
        currentPrice = 1.08450,
        changePercent = +0.14,
        spreadPips = 0.4
    ),
    AllowedAsset(
        symbol = "GBPUSD",
        displayName = "Great British Pound / USD",
        lotSize = 0.02,
        action = TradeActionDirection.BOTH,
        isAllowed = true,
        currentPrice = 1.29420,
        changePercent = -0.22,
        spreadPips = 0.7
    ),
    AllowedAsset(
        symbol = "US30",
        displayName = "Wall Street 30 Index",
        lotSize = 0.01,
        action = TradeActionDirection.BOTH,
        isAllowed = true,
        currentPrice = 39820.00,
        changePercent = +0.55,
        spreadPips = 2.1
    ),
    AllowedAsset(
        symbol = "NAS100",
        displayName = "US Tech 100 Index",
        lotSize = 0.01,
        action = TradeActionDirection.SELL_ONLY,
        isAllowed = false,
        currentPrice = 18420.50,
        changePercent = -0.48,
        spreadPips = 1.8
    ),
    AllowedAsset(
        symbol = "USDJPY",
        displayName = "US Dollar / Japanese Yen",
        lotSize = 0.03,
        action = TradeActionDirection.BOTH,
        isAllowed = false,
        currentPrice = 153.420,
        changePercent = +0.31,
        spreadPips = 0.5
    )
)

private val initialPairs = listOf(
    TradingPair(
        symbol = "XAUUSDm",
        displayName = "Gold Mini / USD",
        basePrice = 5094.129,
        currentPrice = 5110.004,
        changePercent = +0.84,
        spreadPips = 0.8,
        signal = SignalType.STRONG_BUY,
        timeframe = "M5",
        isEnabled = true,
        historyPrices = listOf(5094.1, 5098.2, 5104.5, 5101.8, 5107.0, 5110.0)
    ),
    TradingPair(
        symbol = "EURUSD",
        displayName = "Euro / US Dollar",
        basePrice = 1.08320,
        currentPrice = 1.08450,
        changePercent = +0.14,
        spreadPips = 0.4,
        signal = SignalType.STRONG_BUY,
        timeframe = "M1",
        isEnabled = true,
        historyPrices = listOf(1.0832, 1.0835, 1.0839, 1.0842, 1.0845)
    ),
    TradingPair(
        symbol = "GBPUSD",
        displayName = "British Pound / USD",
        basePrice = 1.29680,
        currentPrice = 1.29420,
        changePercent = -0.22,
        spreadPips = 0.7,
        signal = SignalType.STRONG_SELL,
        timeframe = "M5",
        isEnabled = true,
        historyPrices = listOf(1.2968, 1.2960, 1.2952, 1.2948, 1.2942)
    ),
    TradingPair(
        symbol = "US30",
        displayName = "Dow Jones 30 Index",
        basePrice = 39650.0,
        currentPrice = 39820.0,
        changePercent = +0.55,
        spreadPips = 2.1,
        signal = SignalType.BUY,
        timeframe = "M5",
        isEnabled = true,
        historyPrices = listOf(39650.0, 39710.0, 39690.0, 39780.0, 39820.0)
    ),
    TradingPair(
        symbol = "NAS100",
        displayName = "Nasdaq 100 Index",
        basePrice = 18510.0,
        currentPrice = 18420.5,
        changePercent = -0.48,
        spreadPips = 1.8,
        signal = SignalType.SELL,
        timeframe = "M1",
        isEnabled = false,
        historyPrices = listOf(18510.0, 18490.0, 18460.0, 18435.0, 18420.5)
    ),
    TradingPair(
        symbol = "USDJPY",
        displayName = "US Dollar / Japanese Yen",
        basePrice = 152.950,
        currentPrice = 153.420,
        changePercent = +0.31,
        spreadPips = 0.5,
        signal = SignalType.BUY,
        timeframe = "M5",
        isEnabled = false,
        historyPrices = listOf(152.95, 153.10, 153.25, 153.30, 153.42)
    )
)

private val initialTrades = listOf(
    LiveTrade(
        id = "#336901145",
        symbol = "XAUUSDm",
        type = SignalType.BUY,
        lotSize = 0.01,
        openPrice = 5094.129,
        currentPrice = 5110.004,
        pnlZar = 282.97,
        tp = 5118.00,
        sl = 5088.00,
        timeAgo = "M5 • Running"
    ),
    LiveTrade(
        id = "#336901146",
        symbol = "XAUUSDm",
        type = SignalType.BUY,
        lotSize = 0.01,
        openPrice = 5094.229,
        currentPrice = 5110.954,
        pnlZar = 281.30,
        tp = 5118.00,
        sl = 5088.00,
        timeAgo = "M5 • Running"
    )
)

class TradingBotViewModel(application: Application) : AndroidViewModel(application) {

    private val authService = FirebaseAuthService(application)
    private val firestoreRepo = FirestoreRepository(application)

    private val _uiState = MutableStateFlow(TradingUiState())
    val uiState: StateFlow<TradingUiState> = _uiState.asStateFlow()

    init {
        // Collect user profile from auth service
        viewModelScope.launch {
            authService.currentUserState.collect { user ->
                _uiState.update { it.copy(userProfile = user) }
            }
        }

        startPriceTicker()
        startLatencyJitter()
    }

    fun selectTab(tab: NavigationTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun toggleAutoTrading() {
        val newState = !_uiState.value.isAutoTradingActive
        val updatedRobot = _uiState.value.selectedRobot.copy(
            status = if (newState) BotStatus.ACTIVE else BotStatus.PAUSED
        )
        _uiState.update { state ->
            state.copy(
                isAutoTradingActive = newState,
                selectedRobot = updatedRobot,
                activeNotification = if (newState) "⚡ FX KILLER Scalper EA Activated! High-Frequency Trading Live" else "⏸ EA Bot Execution Paused"
            )
        }
        syncCurrentBotToFirestore(updatedRobot)
    }

    fun setBotMode(mode: BotMode) {
        val updatedRobot = _uiState.value.selectedRobot.copy(mode = mode)
        _uiState.update { state ->
            state.copy(
                selectedRobot = updatedRobot,
                activeNotification = "EA Mode switched to: ${mode.name} MODE"
            )
        }
        syncCurrentBotToFirestore(updatedRobot)
    }

    fun toggleFloatingWidget() {
        _uiState.update { it.copy(showFloatingWidget = !it.showFloatingWidget) }
    }

    fun setWidgetTab(tab: String) {
        _uiState.update { it.copy(widgetTab = tab) }
    }

    fun toggleAuthDialog() {
        _uiState.update { it.copy(showAuthDialog = !it.showAuthDialog) }
    }

    fun signInWithGoogleDemo() {
        authService.signInAsDemoGoogleUser()
        _uiState.update {
            it.copy(
                showAuthDialog = false,
                activeNotification = "Signed in as ${it.userProfile.displayName} via Google Firebase Auth"
            )
        }
    }

    fun signOut() {
        authService.signOut()
        _uiState.update {
            it.copy(
                showAuthDialog = false,
                activeNotification = "Signed out to Guest Profile"
            )
        }
    }

    fun togglePair(symbol: String) {
        _uiState.update { state ->
            val updatedPairs = state.tradingPairs.map { pair ->
                if (pair.symbol == symbol) pair.copy(isEnabled = !pair.isEnabled) else pair
            }
            state.copy(tradingPairs = updatedPairs)
        }
    }

    fun toggleAllowedAsset(symbol: String) {
        _uiState.update { state ->
            val updated = state.allowedAssets.map { asset ->
                if (asset.symbol == symbol) asset.copy(isAllowed = !asset.isAllowed) else asset
            }
            state.copy(allowedAssets = updated)
        }
        viewModelScope.launch {
            firestoreRepo.saveAllowedAssets(_uiState.value.userProfile.uid, _uiState.value.allowedAssets)
        }
    }

    fun updateAssetLotSize(symbol: String, newLotSize: Double) {
        _uiState.update { state ->
            val updated = state.allowedAssets.map { asset ->
                if (asset.symbol == symbol) asset.copy(lotSize = newLotSize) else asset
            }
            state.copy(allowedAssets = updated)
        }
        viewModelScope.launch {
            firestoreRepo.saveAllowedAssets(_uiState.value.userProfile.uid, _uiState.value.allowedAssets)
        }
    }

    fun updateAssetAction(symbol: String, newAction: TradeActionDirection) {
        _uiState.update { state ->
            val updated = state.allowedAssets.map { asset ->
                if (asset.symbol == symbol) asset.copy(action = newAction) else asset
            }
            state.copy(allowedAssets = updated)
        }
        viewModelScope.launch {
            firestoreRepo.saveAllowedAssets(_uiState.value.userProfile.uid, _uiState.value.allowedAssets)
        }
    }

    fun selectRobotForDetail(robot: TradingRobot) {
        _uiState.update { it.copy(selectedRobot = robot, showBotDetailSheet = true) }
    }

    fun switchActiveRobot(robot: TradingRobot) {
        _uiState.update {
            it.copy(
                selectedRobot = robot,
                isAutoTradingActive = robot.status == BotStatus.ACTIVE,
                activeNotification = "Switched active EA to: ${robot.name} ${robot.version}"
            )
        }
    }

    fun openConnectDialog() {
        _uiState.update { it.copy(showConnectDialog = true) }
    }

    fun dismissConnectDialog() {
        _uiState.update { it.copy(showConnectDialog = false) }
    }

    fun dismissBotDetailSheet() {
        _uiState.update { it.copy(showBotDetailSheet = false) }
    }

    fun connectNewRobot(name: String, accountId: String, server: String, platform: String, lotSize: Double, riskPercent: Double) {
        val newRobot = TradingRobot(
            id = "bot_${System.currentTimeMillis() % 10000}",
            name = name.ifBlank { "Custom Scalper EA" },
            version = "v1.0",
            author = _uiState.value.userProfile.displayName,
            subtitle = "CUSTOM MT5 BRIDGE",
            status = BotStatus.ACTIVE,
            mode = BotMode.NORMAL,
            accountId = accountId.ifBlank { "99182301" },
            server = server.ifBlank { "LiveMT5-Bridge" },
            platform = platform,
            dailyProfitZar = 0.0,
            totalProfitZar = 0.0,
            winRate = 0.0,
            tradesToday = 0,
            activePairsCount = 2,
            riskPercent = riskPercent,
            lotSize = lotSize,
            trailingStop = true,
            newsFilter = true
        )
        _uiState.update { state ->
            state.copy(
                robotList = listOf(newRobot) + state.robotList,
                selectedRobot = newRobot,
                showConnectDialog = false,
                activeNotification = "Successfully connected EA ${newRobot.name} to Firestore & MT5 Server"
            )
        }
        syncCurrentBotToFirestore(newRobot)
    }

    fun closeTrade(tradeId: String) {
        val closedTrade = _uiState.value.openTrades.find { it.id == tradeId }
        _uiState.update { state ->
            val updatedTrades = state.openTrades.filter { it.id != tradeId }
            state.copy(
                openTrades = updatedTrades,
                activeNotification = "Closed position $tradeId with +R${String.format("%.2f", closedTrade?.pnlZar ?: 0.0)} Profit"
            )
        }
        if (closedTrade != null) {
            viewModelScope.launch {
                firestoreRepo.recordTrade(_uiState.value.userProfile.uid, closedTrade)
            }
        }
    }

    fun emergencyCloseAll() {
        val totalProfit = _uiState.value.openTrades.sumOf { it.pnlZar }
        _uiState.update { state ->
            state.copy(
                openTrades = emptyList(),
                activeNotification = "Emergency Closed All Positions (+R${String.format("%.2f", totalProfit)} Profit secured)"
            )
        }
    }

    fun executeSignalTrade(pair: TradingPair) {
        val newTrade = LiveTrade(
            id = "#${(336901150..336909999).random()}",
            symbol = pair.symbol,
            type = if (pair.signal == SignalType.SELL || pair.signal == SignalType.STRONG_SELL) SignalType.SELL else SignalType.BUY,
            lotSize = _uiState.value.selectedRobot.lotSize,
            openPrice = pair.currentPrice,
            currentPrice = pair.currentPrice,
            pnlZar = (260..290).random().toDouble() + 0.45,
            tp = if (pair.signal == SignalType.BUY || pair.signal == SignalType.STRONG_BUY) pair.currentPrice * 1.0025 else pair.currentPrice * 0.9975,
            sl = if (pair.signal == SignalType.BUY || pair.signal == SignalType.STRONG_BUY) pair.currentPrice * 0.9985 else pair.currentPrice * 1.0015,
            timeAgo = "M5 • Running"
        )
        _uiState.update { state ->
            state.copy(
                openTrades = listOf(newTrade) + state.openTrades,
                activeNotification = "Executed ${newTrade.type.name} on ${pair.symbol} @ ${pair.currentPrice}"
            )
        }
        viewModelScope.launch {
            firestoreRepo.recordTrade(_uiState.value.userProfile.uid, newTrade)
        }
    }

    fun clearNotification() {
        _uiState.update { it.copy(activeNotification = null) }
    }

    private fun syncCurrentBotToFirestore(robot: TradingRobot) {
        viewModelScope.launch {
            firestoreRepo.saveBotSettings(_uiState.value.userProfile.uid, robot)
        }
    }

    private fun startPriceTicker() {
        viewModelScope.launch {
            while (true) {
                delay(1800)
                _uiState.update { state ->
                    val updatedPairs = state.tradingPairs.map { pair ->
                        val changeDelta = (Random.nextDouble() - 0.48) * (pair.currentPrice * 0.0003)
                        val newPrice = (pair.currentPrice + changeDelta).coerceAtLeast(0.0001)
                        val newPercent = ((newPrice - pair.basePrice) / pair.basePrice) * 100.0
                        val newHistory = (pair.historyPrices + newPrice).takeLast(10)
                        pair.copy(
                            currentPrice = newPrice,
                            changePercent = newPercent,
                            historyPrices = newHistory
                        )
                    }

                    // Also tick floating trades
                    val goldPrice = updatedPairs.find { it.symbol == "XAUUSDm" }?.currentPrice ?: 5110.0
                    val updatedTrades = state.openTrades.map { trade ->
                        if (trade.symbol == "XAUUSDm") {
                            val diff = goldPrice - trade.openPrice
                            val pnl = (diff * 17.5).coerceAtLeast(150.0)
                            trade.copy(currentPrice = goldPrice, pnlZar = pnl)
                        } else {
                            trade
                        }
                    }

                    // Live balance calculation
                    val currentFloatingZar = updatedTrades.sumOf { it.pnlZar }
                    val baseBalance = 418.89
                    val equity = baseBalance + currentFloatingZar

                    val updatedUser = state.userProfile.copy(
                        balanceZar = equity,
                        equityZar = equity + 420.0,
                        freeMarginZar = equity + 335.0,
                        marginLevelPercent = (equity / 85.39) * 100.0
                    )

                    state.copy(
                        tradingPairs = updatedPairs,
                        openTrades = updatedTrades,
                        userProfile = updatedUser
                    )
                }
            }
        }
    }

    private fun startLatencyJitter() {
        viewModelScope.launch {
            while (true) {
                delay(4000)
                _uiState.update { state ->
                    val newLatency = (9..18).random()
                    state.copy(latencyMs = newLatency)
                }
            }
        }
    }
}
