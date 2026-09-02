package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.BotMode
import com.example.model.TradingRobot
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBorder
import com.example.ui.theme.NeonGreenDeep
import com.example.ui.theme.NeonGreenGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun FloatingEaWidget(
    robot: TradingRobot,
    selectedTab: String, // "STATUS" or "MODE"
    isAutoTradingActive: Boolean,
    onTabChange: (String) -> Unit,
    onModeChange: (BotMode) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(26.dp))
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        listOf(NeonGreenGlow, NeonGreenDeep, Color(0xFF00FF66))
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .testTag("floating_ea_widget_card"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = DarkBackground.copy(alpha = 0.96f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top close bar & title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FLOATING EA CONTROLLER",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Widget",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Circular BIMZ Badge
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, NeonGreenGlow, CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_fx_killer_bimz),
                        contentDescription = "Bimz Forest Killer",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${robot.name.uppercase()} ${robot.version.uppercase()}",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "~ ${robot.author}",
                    color = NeonGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tabs: STATUS vs MODE (From Video)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val isStatus = selectedTab == "STATUS"
                    val isMode = selectedTab == "MODE"

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isStatus) NeonGreenDeep else Color.Transparent)
                            .border(
                                1.dp,
                                if (isStatus) NeonGreenGlow else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onTabChange("STATUS") }
                            .padding(vertical = 10.dp)
                            .testTag("tab_status"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "STATUS",
                            color = if (isStatus) NeonGreen else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isMode) NeonGreenDeep else Color.Transparent)
                            .border(
                                1.dp,
                                if (isMode) NeonGreenGlow else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onTabChange("MODE") }
                            .padding(vertical = 10.dp)
                            .testTag("tab_mode"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MODE",
                            color = if (isMode) NeonGreen else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Tab Content: STATUS
                if (selectedTab == "STATUS") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .border(1.dp, NeonGreenBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        StatusItemRow(title = "CONNECTED ACCOUNT", isActive = true)
                        StatusItemRow(title = "INTERNET", isActive = true)
                        StatusItemRow(title = "XAUUSDM", isActive = true)
                        StatusItemRow(title = "BOT ACTIVE", isActive = isAutoTradingActive)
                    }
                } else {
                    // Tab Content: MODE (NORMAL vs ATTACK)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurface)
                            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // NORMAL Mode Option
                        val isNormal = robot.mode == BotMode.NORMAL
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isNormal) NeonGreenDeep else DarkSurfaceElevated)
                                .border(
                                    1.dp,
                                    if (isNormal) NeonGreenGlow else DarkBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onModeChange(BotMode.NORMAL) }
                                .padding(14.dp)
                                .testTag("select_mode_normal")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = if (isNormal) NeonGreen else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "NORMAL",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Standard swing & conservative M5 triggers",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                if (isNormal) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(NeonGreen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // ATTACK Mode Option (From Video)
                        val isAttack = robot.mode == BotMode.ATTACK
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isAttack) Color(0xFF2E0D12) else DarkSurfaceElevated)
                                .border(
                                    1.5.dp,
                                    if (isAttack) AccentRed else DarkBorder,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onModeChange(BotMode.ATTACK) }
                                .padding(14.dp)
                                .testTag("select_mode_attack")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FlashOn,
                                        contentDescription = null,
                                        tint = if (isAttack) AccentRed else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "ATTACK",
                                            color = if (isAttack) AccentRed else Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "Aggressive M1/M5 instant momentum scalp",
                                            color = if (isAttack) AccentGold else TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                if (isAttack) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(AccentRed),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
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
}

@Composable
private fun StatusItemRow(
    title: String,
    isActive: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isActive) NeonGreenDeep else DarkSurfaceElevated)
                    .border(
                        1.dp,
                        if (isActive) NeonGreenGlow else DarkBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = if (isActive) Color.White else TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Text(
            text = if (isActive) "ONLINE" else "OFFLINE",
            color = if (isActive) NeonGreen else AccentRed,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
