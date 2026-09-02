package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BotMode
import com.example.model.BotStatus
import com.example.model.LiveTrade
import com.example.model.SignalType
import com.example.model.TradingPair
import com.example.model.TradingRobot
import com.example.model.UserProfile
import com.example.ui.components.ConnectNewRobotButton
import com.example.ui.components.RobotItemCard
import com.example.ui.components.SamuraiHeroCard
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBorder
import com.example.ui.theme.NeonGreenDeep
import com.example.ui.theme.NeonGreenGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun TradeDashboardScreen(
    userProfile: UserProfile,
    selectedRobot: TradingRobot,
    robotList: List<TradingRobot>,
    openTrades: List<LiveTrade>,
    tradingPairs: List<TradingPair>,
    isAutoTradingActive: Boolean,
    latencyMs: Int,
    onToggleAutoTrading: () -> Unit,
    onTogglePair: (String) -> Unit,
    onSelectRobot: (TradingRobot) -> Unit,
    onConnectNewRobot: () -> Unit,
    onCloseTrade: (String) -> Unit,
    onEmergencyCloseAll: () -> Unit,
    onExecuteSignalTrade: (TradingPair) -> Unit,
    onLaunchWidgetClick: () -> Unit,
    onAuthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSignalFilter by remember { mutableStateOf("ALL") }

    val filteredPairs = remember(tradingPairs, selectedSignalFilter) {
        when (selectedSignalFilter) {
            "BUY" -> tradingPairs.filter { it.signal == SignalType.BUY || it.signal == SignalType.STRONG_BUY }
            "SELL" -> tradingPairs.filter { it.signal == SignalType.SELL || it.signal == SignalType.STRONG_SELL }
            "ACTIVE" -> tradingPairs.filter { it.isEnabled }
            else -> tradingPairs
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 0. Live MT5 Account Balance Bar & Firebase Auth Header (From Video)
        item {
            MetaTraderAccountHeaderCard(
                userProfile = userProfile,
                onAuthClick = onAuthClick
            )
        }

        // 1. Hero Card: FX Killer V1.6 / BIMZ Forest Killer with Fast Action Buttons
        item {
            SamuraiHeroCard(
                robot = selectedRobot,
                latencyMs = latencyMs,
                isAutoTradingActive = isAutoTradingActive,
                onQuotesClick = { /* Quotes */ },
                onTradeToggleClick = onToggleAutoTrading,
                onRemoveClick = { /* Remove */ },
                onLaunchWidgetClick = onLaunchWidgetClick
            )
        }

        // 2. Live MT5 Open Positions Feed (From Video: 529.46 - 573.23 ZAR, XAUUSDm 0.01 Buy)
        if (openTrades.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NeonGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "LIVE MT5 POSITIONS (${openTrades.size})",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Text(
                        text = "CLOSE ALL",
                        color = AccentRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onEmergencyCloseAll)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("close_all_positions_btn")
                    )
                }
            }

            items(openTrades, key = { it.id }) { trade ->
                LiveTradeCard(
                    trade = trade,
                    onClose = { onCloseTrade(trade.id) }
                )
            }
        }

        // 3. Current Scalping Signals Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(NeonGreenGlow)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CURRENT SCALPING SIGNALS",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonGreenDeep)
                            .border(1.dp, NeonGreenBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "AI ALGO M1/M5",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "ALL" to "All Signals",
                        "BUY" to "Bullish (BUY)",
                        "SELL" to "Bearish (SELL)",
                        "ACTIVE" to "EA Active Pairs"
                    ).forEach { (filterKey, label) ->
                        val isSelected = selectedSignalFilter == filterKey
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) NeonGreenDeep else DarkSurfaceCard)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonGreen else DarkBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedSignalFilter = filterKey }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("signal_filter_$filterKey")
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) NeonGreen else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Horizontal Carousel of Scalping Signal Cards
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredPairs, key = { it.symbol }) { pair ->
                    ScalpingSignalCard(
                        pair = pair,
                        onExecuteTrade = { onExecuteSignalTrade(pair) }
                    )
                }
            }
        }

        // 4. Live Currency Pairs Ticker & Matrix
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIVE CURRENCY PAIRS (${tradingPairs.size})",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Text(
                    text = "AUTO-SCALP TOGGLE",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Pairs in the main dashboard
        items(tradingPairs, key = { it.symbol }) { pair ->
            DashboardCurrencyPairRow(
                pair = pair,
                onToggle = { onTogglePair(pair.symbol) },
                onQuickTrade = { onExecuteSignalTrade(pair) }
            )
        }

        // 5. "CONNECTED ROBOTS" Section (From Video: FX KILLER scalprer v1.6, Exis V1.0.0)
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "CONNECTED ROBOTS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                modifier = Modifier.testTag("robot_list_header")
            )
        }

        // Robot Cards
        items(robotList, key = { it.id }) { robot ->
            RobotItemCard(
                robot = robot,
                isSelected = robot.id == selectedRobot.id,
                onClick = { onSelectRobot(robot) }
            )
        }

        // "+ CONNECT NEW ROBOT" Button
        item {
            ConnectNewRobotButton(
                onClick = onConnectNewRobot,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

/**
 * MetaTrader Account Top Card (From Video: 529.46 - 573.23 ZAR, Margin, Firebase Auth)
 */
@Composable
private fun MetaTraderAccountHeaderCard(
    userProfile: UserProfile,
    onAuthClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, NeonGreenBorder, RoundedCornerShape(20.dp))
            .testTag("account_header_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Balance in ZAR (as seen in the MT5 video)
                Column {
                    Text(
                        text = "METATRADER 5 ACCOUNT",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${String.format("%.2f", userProfile.balanceZar)} ZAR",
                        color = NeonGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Google Firebase Auth & Cloud Sync Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonGreenDeep)
                        .border(1.dp, NeonGreenGlow, RoundedCornerShape(12.dp))
                        .clickable(onClick = onAuthClick)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("auth_profile_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (userProfile.isGoogleUser) "FIRESTORE SYNC" else "G-SIGN IN",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Equity, Free Margin, Margin Level %
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Equity", color = TextMuted, fontSize = 9.sp)
                    Text(
                        text = "${String.format("%.2f", userProfile.equityZar)}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(text = "Free margin", color = TextMuted, fontSize = 9.sp)
                    Text(
                        text = "${String.format("%.2f", userProfile.freeMarginZar)}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Margin Level", color = TextMuted, fontSize = 9.sp)
                    Text(
                        text = "${String.format("%.1f", userProfile.marginLevelPercent)}%",
                        color = AccentGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Scalping Signal Card with Confidence Meter & Strategy Details
 */
@Composable
private fun ScalpingSignalCard(
    pair: TradingPair,
    onExecuteTrade: () -> Unit
) {
    val isBuy = pair.signal == SignalType.BUY || pair.signal == SignalType.STRONG_BUY
    val isStrong = pair.signal == SignalType.STRONG_BUY || pair.signal == SignalType.STRONG_SELL
    val signalColor = if (isBuy) NeonGreen else AccentRed
    val confidence = if (isStrong) 95 else 88
    val strategyName = if (isBuy) "Order Block + RSI Breakout" else "VWAP Rejection + EMA Cross"

    Card(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(
                1.dp,
                signalColor.copy(alpha = if (isStrong) 0.8f else 0.4f),
                RoundedCornerShape(18.dp)
            )
            .testTag("signal_card_${pair.symbol}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Symbol & Timeframe
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = pair.symbol,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(DarkSurfaceElevated)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = pair.timeframe,
                            color = TextSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Signal badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(signalColor.copy(alpha = 0.2f))
                        .border(1.dp, signalColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = pair.signal.name.replace("_", " "),
                        color = signalColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Current price & confidence
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "PRICE",
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (pair.currentPrice > 100) String.format("%.2f", pair.currentPrice) else String.format("%.5f", pair.currentPrice),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "CONFIDENCE",
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$confidence%",
                        color = AccentGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { confidence / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = signalColor,
                trackColor = DarkSurfaceElevated
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⚡ $strategyName",
                color = TextSecondary,
                fontSize = 10.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onExecuteTrade,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("execute_signal_${pair.symbol}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBuy) NeonGreenDeep else Color(0xFF3D1018)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = signalColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SCALP ${pair.signal.name.replace("_", " ")}",
                        color = signalColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Currency Pair Row for Main Dashboard
 */
@Composable
private fun DashboardCurrencyPairRow(
    pair: TradingPair,
    onToggle: () -> Unit,
    onQuickTrade: () -> Unit
) {
    val isBullish = pair.changePercent >= 0
    val signalColor = when (pair.signal) {
        SignalType.STRONG_BUY, SignalType.BUY -> NeonGreen
        SignalType.STRONG_SELL, SignalType.SELL -> AccentRed
        SignalType.NEUTRAL -> TextMuted
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                color = if (pair.isEnabled) NeonGreenBorder else DarkBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .testTag("pair_row_${pair.symbol}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = pair.symbol,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DarkSurfaceElevated)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = pair.timeframe,
                                    color = TextSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = pair.displayName,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (pair.currentPrice > 100) String.format("%.2f", pair.currentPrice) else String.format("%.5f", pair.currentPrice),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isBullish) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = if (isBullish) NeonGreen else AccentRed,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${if (isBullish) "+" else ""}${String.format("%.2f", pair.changePercent)}%",
                                color = if (isBullish) NeonGreen else AccentRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Switch(
                        checked = pair.isEnabled,
                        onCheckedChange = { onToggle() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonGreen,
                            uncheckedTrackColor = DarkSurfaceElevated
                        ),
                        modifier = Modifier.testTag("switch_${pair.symbol}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(signalColor.copy(alpha = 0.15f))
                        .border(1.dp, signalColor.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .clickable { onQuickTrade() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(signalColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pair.signal.name.replace("_", " "),
                        color = signalColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Spread: ${pair.spreadPips} pips",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                MiniPriceSparkline(
                    prices = pair.historyPrices,
                    isPositive = isBullish,
                    modifier = Modifier
                        .width(65.dp)
                        .height(20.dp)
                )
            }
        }
    }
}

/**
 * Live Open Trade Card (Exact replica from MT5 video: XAUUSDm, buy 0.01 | 5094.129 -> 5110.004 | +282.97 ZAR)
 */
@Composable
private fun LiveTradeCard(
    trade: LiveTrade,
    onClose: () -> Unit
) {
    val isProfit = trade.pnlZar >= 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, NeonGreenBorder, RoundedCornerShape(16.dp))
            .testTag("trade_card_${trade.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (trade.type == SignalType.BUY) NeonGreenDeep else AccentRed.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            if (trade.type == SignalType.BUY) NeonGreen else AccentRed,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (trade.type == SignalType.BUY) "BUY" else "SELL",
                        color = if (trade.type == SignalType.BUY) NeonGreen else AccentRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "${trade.symbol}, buy ${String.format("%.2f", trade.lotSize)}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${trade.openPrice} ➔ ${trade.currentPrice}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${if (isProfit) "+" else ""}${String.format("%.2f", trade.pnlZar)} ZAR",
                        color = if (isProfit) Color(0xFF64B5F6) else AccentRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = trade.timeAgo,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceElevated)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Trade",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniPriceSparkline(
    prices: List<Double>,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    val strokeColor = if (isPositive) NeonGreen else AccentRed

    Canvas(modifier = modifier) {
        if (prices.size < 2) return@Canvas

        val minPrice = prices.minOrNull() ?: 0.0
        val maxPrice = prices.maxOrNull() ?: 1.0
        val range = (maxPrice - minPrice).coerceAtLeast(0.0001)

        val path = Path()
        val width = size.width
        val height = size.height

        prices.forEachIndexed { index, price ->
            val x = index * (width / (prices.size - 1))
            val normalizedY = 1f - ((price - minPrice) / range).toFloat()
            val y = normalizedY * (height - 4f) + 2f

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
