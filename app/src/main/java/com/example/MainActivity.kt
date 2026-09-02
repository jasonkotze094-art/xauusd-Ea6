package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NavigationTab
import com.example.ui.components.AuthDialog
import com.example.ui.components.BotDetailSheet
import com.example.ui.components.ConnectRobotDialog
import com.example.ui.components.FloatingEaWidget
import com.example.ui.components.NavigationTabBar
import com.example.ui.screens.AssetsScreen
import com.example.ui.screens.InfoScreen
import com.example.ui.screens.PairsScreen
import com.example.ui.screens.TradeDashboardScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.TradingBotViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: TradingBotViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.activeNotification) {
        uiState.activeNotification?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearNotification()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = DarkBackground,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 80.dp)
            )
        },
        bottomBar = {
            NavigationTabBar(
                selectedTab = uiState.currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "tab_transition"
            ) { tab ->
                when (tab) {
                    NavigationTab.TRADE -> {
                        TradeDashboardScreen(
                            userProfile = uiState.userProfile,
                            selectedRobot = uiState.selectedRobot,
                            robotList = uiState.robotList,
                            openTrades = uiState.openTrades,
                            tradingPairs = uiState.tradingPairs,
                            isAutoTradingActive = uiState.isAutoTradingActive,
                            latencyMs = uiState.latencyMs,
                            onToggleAutoTrading = { viewModel.toggleAutoTrading() },
                            onTogglePair = { symbol -> viewModel.togglePair(symbol) },
                            onSelectRobot = { robot -> viewModel.selectRobotForDetail(robot) },
                            onConnectNewRobot = { viewModel.openConnectDialog() },
                            onCloseTrade = { id -> viewModel.closeTrade(id) },
                            onEmergencyCloseAll = { viewModel.emergencyCloseAll() },
                            onExecuteSignalTrade = { pair -> viewModel.executeSignalTrade(pair) },
                            onLaunchWidgetClick = { viewModel.toggleFloatingWidget() },
                            onAuthClick = { viewModel.toggleAuthDialog() }
                        )
                    }
                    NavigationTab.ASSETS -> {
                        AssetsScreen(
                            allowedAssets = uiState.allowedAssets,
                            onToggleAllowed = { symbol -> viewModel.toggleAllowedAsset(symbol) },
                            onUpdateLotSize = { symbol, lot -> viewModel.updateAssetLotSize(symbol, lot) },
                            onUpdateAction = { symbol, action -> viewModel.updateAssetAction(symbol, action) }
                        )
                    }
                    NavigationTab.PAIRS -> {
                        PairsScreen(
                            pairs = uiState.tradingPairs,
                            onTogglePair = { symbol -> viewModel.togglePair(symbol) }
                        )
                    }
                    NavigationTab.INFO -> {
                        InfoScreen()
                    }
                }
            }

            // Connect New Robot Dialog
            if (uiState.showConnectDialog) {
                ConnectRobotDialog(
                    onDismiss = { viewModel.dismissConnectDialog() },
                    onConnect = { name, accountId, server, platform, lotSize, riskPercent ->
                        viewModel.connectNewRobot(name, accountId, server, platform, lotSize, riskPercent)
                    }
                )
            }

            // Floating EA Widget Popup
            if (uiState.showFloatingWidget) {
                FloatingEaWidget(
                    robot = uiState.selectedRobot,
                    selectedTab = uiState.widgetTab,
                    isAutoTradingActive = uiState.isAutoTradingActive,
                    onTabChange = { tab -> viewModel.setWidgetTab(tab) },
                    onModeChange = { mode -> viewModel.setBotMode(mode) },
                    onDismiss = { viewModel.toggleFloatingWidget() }
                )
            }

            // Google Auth & Firestore Cloud Sync Dialog
            if (uiState.showAuthDialog) {
                AuthDialog(
                    userProfile = uiState.userProfile,
                    onSignInGoogle = { viewModel.signInWithGoogleDemo() },
                    onSignOut = { viewModel.signOut() },
                    onDismiss = { viewModel.toggleAuthDialog() }
                )
            }

            // Bot Details Bottom Sheet
            if (uiState.showBotDetailSheet) {
                BotDetailSheet(
                    robot = uiState.selectedRobot,
                    onDismiss = { viewModel.dismissBotDetailSheet() },
                    onSetActive = { activeBot -> viewModel.switchActiveRobot(activeBot) }
                )
            }
        }
    }
}
