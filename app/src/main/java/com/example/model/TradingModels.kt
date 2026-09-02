package com.example.model

enum class BotStatus {
    ACTIVE,
    PAUSED,
    DISCONNECTED
}

enum class SignalType {
    STRONG_BUY,
    BUY,
    STRONG_SELL,
    SELL,
    NEUTRAL
}

enum class NavigationTab {
    TRADE,
    ASSETS,
    PAIRS,
    INFO
}

enum class BotMode {
    NORMAL,
    ATTACK
}

enum class TradeActionDirection {
    BOTH,
    BUY_ONLY,
    SELL_ONLY
}

data class UserProfile(
    val uid: String = "guest_trader_01",
    val email: String = "trader@fxkiller.ea",
    val displayName: String = "TKFXKILLER (Pro Trader)",
    val isGoogleUser: Boolean = false,
    val photoUrl: String? = null,
    val balanceZar: Double = 563.46,
    val equityZar: Double = 988.25,
    val freeMarginZar: Double = 902.86,
    val marginLevelPercent: Double = 1157.30,
    val marginZar: Double = 85.39,
    val isSyncedWithFirestore: Boolean = true
)

data class TradingRobot(
    val id: String,
    val name: String,
    val version: String,
    val author: String = "TKFXKILLER",
    val subtitle: String = "BIMZ FOREST KILLER",
    val status: BotStatus = BotStatus.ACTIVE,
    val mode: BotMode = BotMode.ATTACK,
    val accountId: String = "336901145",
    val server: String = "FP Markets-Live",
    val platform: String = "MetaTrader 5",
    val dailyProfitZar: Double = 1850.50,
    val totalProfitZar: Double = 24600.00,
    val winRate: Double = 92.4,
    val tradesToday: Int = 18,
    val activePairsCount: Int = 4,
    val riskPercent: Double = 1.0,
    val lotSize: Double = 0.01,
    val trailingStop: Boolean = true,
    val newsFilter: Boolean = true,
    val allowedSymbolIds: List<String> = listOf("XAUUSDm", "EURUSD", "GBPUSD", "US30")
)

data class AllowedAsset(
    val symbol: String,
    val displayName: String,
    val lotSize: Double = 0.01,
    val action: TradeActionDirection = TradeActionDirection.BOTH,
    val isAllowed: Boolean = true,
    val currentPrice: Double,
    val changePercent: Double,
    val spreadPips: Double = 1.2
)

data class TradingPair(
    val symbol: String,
    val displayName: String,
    val basePrice: Double,
    val currentPrice: Double,
    val changePercent: Double,
    val spreadPips: Double,
    val signal: SignalType,
    val timeframe: String = "M5",
    val isEnabled: Boolean = true,
    val historyPrices: List<Double> = emptyList()
)

data class LiveTrade(
    val id: String,
    val symbol: String,
    val type: SignalType, // BUY or SELL
    val lotSize: Double,
    val openPrice: Double,
    val currentPrice: Double,
    val pnlZar: Double,
    val tp: Double,
    val sl: Double,
    val timeAgo: String
)
