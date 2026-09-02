package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.AllowedAsset
import com.example.model.BotMode
import com.example.model.BotStatus
import com.example.model.LiveTrade
import com.example.model.TradingRobot
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepository(private val context: Context) {
    private val TAG = "FirestoreRepository"
    private var firestore: FirebaseFirestore? = null

    init {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                firestore = FirebaseFirestore.getInstance()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Firestore initialization error: ${e.message}")
        }
    }

    suspend fun saveBotSettings(userId: String, robot: TradingRobot) = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext false
            val data = mapOf(
                "name" to robot.name,
                "version" to robot.version,
                "author" to robot.author,
                "subtitle" to robot.subtitle,
                "status" to robot.status.name,
                "mode" to robot.mode.name,
                "lotSize" to robot.lotSize,
                "riskPercent" to robot.riskPercent,
                "server" to robot.server,
                "platform" to robot.platform,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(userId)
                .collection("robots")
                .document(robot.id)
                .set(data, SetOptions.merge())
                .await()
            Log.d(TAG, "Bot settings saved to Firestore for ${robot.id}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save bot settings: ${e.message}")
            false
        }
    }

    suspend fun saveAllowedAssets(userId: String, assets: List<AllowedAsset>) = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext false
            val batch = db.batch()
            assets.forEach { asset ->
                val docRef = db.collection("users")
                    .document(userId)
                    .collection("allowed_assets")
                    .document(asset.symbol)
                val data = mapOf(
                    "symbol" to asset.symbol,
                    "displayName" to asset.displayName,
                    "lotSize" to asset.lotSize,
                    "action" to asset.action.name,
                    "isAllowed" to asset.isAllowed,
                    "updatedAt" to System.currentTimeMillis()
                )
                batch.set(docRef, data, SetOptions.merge())
            }
            batch.commit().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save allowed assets: ${e.message}")
            false
        }
    }

    suspend fun recordTrade(userId: String, trade: LiveTrade) = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext false
            val data = mapOf(
                "id" to trade.id,
                "symbol" to trade.symbol,
                "type" to trade.type.name,
                "lotSize" to trade.lotSize,
                "openPrice" to trade.openPrice,
                "currentPrice" to trade.currentPrice,
                "pnlZar" to trade.pnlZar,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(userId)
                .collection("trade_history")
                .document(trade.id)
                .set(data)
                .await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to record trade in Firestore: ${e.message}")
            false
        }
    }
}
