package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AllowedAsset
import com.example.model.TradeActionDirection
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkBackground
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
fun AssetsScreen(
    allowedAssets: List<AllowedAsset>,
    onToggleAllowed: (String) -> Unit,
    onUpdateLotSize: (String, Double) -> Unit,
    onUpdateAction: (String, TradeActionDirection) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf("ALLOWED") } // "ALLOWED" or "ALL"

    val displayedAssets = remember(allowedAssets, activeTab) {
        if (activeTab == "ALLOWED") {
            allowedAssets.filter { it.isAllowed }
        } else {
            allowedAssets
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Top Header Tabs: Allowed Symbols | Bull Logo | All Symbols (From Video)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab: Allowed Symbols
            val isAllowedTab = activeTab == "ALLOWED"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { activeTab = "ALLOWED" }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("tab_allowed_symbols")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Allowed Symbols",
                        color = if (isAllowedTab) Color.White else TextMuted,
                        fontSize = 15.sp,
                        fontWeight = if (isAllowedTab) FontWeight.Bold else FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (isAllowedTab) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(NeonGreen)
                        )
                    }
                }
            }

            // Center Cyber Horns Icon / Emblem
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(NeonGreenDeep)
                    .border(1.dp, NeonGreenGlow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚡",
                    color = NeonGreen,
                    fontSize = 18.sp
                )
            }

            // Tab: All Symbols
            val isAllTab = activeTab == "ALL"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { activeTab = "ALL" }
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .testTag("tab_all_symbols")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "All Symbols",
                        color = if (isAllTab) Color.White else TextMuted,
                        fontSize = 15.sp,
                        fontWeight = if (isAllTab) FontWeight.Bold else FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (isAllTab) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(NeonGreen)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description from video: "These are Symbols you have selected for your EA to trade."
        Text(
            text = "These are Symbols you have selected for your EA to trade.",
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // List of Asset Cards
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(displayedAssets, key = { it.symbol }) { asset ->
                AssetTradeCard(
                    asset = asset,
                    onToggleAllowed = { onToggleAllowed(asset.symbol) },
                    onIncreaseLot = {
                        val newLot = ((asset.lotSize + 0.01) * 100).toInt() / 100.0
                        onUpdateLotSize(asset.symbol, newLot)
                    },
                    onDecreaseLot = {
                        val newLot = (((asset.lotSize - 0.01).coerceAtLeast(0.01)) * 100).toInt() / 100.0
                        onUpdateLotSize(asset.symbol, newLot)
                    },
                    onSelectAction = { action ->
                        onUpdateAction(asset.symbol, action)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

/**
 * Exactly matches the video's neon green bordered asset card:
 * Green circular arrow icon, Symbol name (XAUUSDm), Lot Size: 0.01, Action: BOTH
 */
@Composable
private fun AssetTradeCard(
    asset: AllowedAsset,
    onToggleAllowed: () -> Unit,
    onIncreaseLot: () -> Unit,
    onDecreaseLot: () -> Unit,
    onSelectAction: (TradeActionDirection) -> Unit
) {
    var showActionMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.5.dp,
                color = if (asset.isAllowed) NeonGreen else DarkBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("asset_card_${asset.symbol}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main Row (From Video)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Circular Green Forward Arrow
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(NeonGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = asset.symbol,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Lot Size : ",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                            Text(
                                text = String.format("%.2f", asset.lotSize),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Action : ",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clickable { showActionMenu = true }
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonGreenDeep)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = asset.action.name,
                                    color = NeonGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                DropdownMenu(
                                    expanded = showActionMenu,
                                    onDismissRequest = { showActionMenu = false },
                                    modifier = Modifier.background(DarkSurfaceElevated)
                                ) {
                                    TradeActionDirection.values().forEach { dir ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = dir.name,
                                                    color = if (asset.action == dir) NeonGreen else Color.White
                                                )
                                            },
                                            onClick = {
                                                onSelectAction(dir)
                                                showActionMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Toggle Switch for Allowing EA Trading on this symbol
                Switch(
                    checked = asset.isAllowed,
                    onCheckedChange = { onToggleAllowed() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = NeonGreen,
                        uncheckedTrackColor = DarkSurfaceElevated
                    ),
                    modifier = Modifier.testTag("switch_asset_${asset.symbol}")
                )
            }

            // Secondary Config Bar: Lot Adjuster & Live Price
            if (asset.isAllowed) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurface)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Price: ${if (asset.currentPrice > 100) String.format("%.2f", asset.currentPrice) else String.format("%.5f", asset.currentPrice)}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Lot:",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceElevated)
                                .clickable(onClick = onDecreaseLot),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                tint = TextSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(NeonGreenDeep)
                                .clickable(onClick = onIncreaseLot),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = NeonGreen,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
