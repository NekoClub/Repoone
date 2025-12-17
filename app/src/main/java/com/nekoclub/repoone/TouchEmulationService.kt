package com.nekoclub.repoone

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi

class TouchEmulationService : AccessibilityService() {
    
    companion object {
        private const val TAG = "TouchEmulationService"
        private const val DEBOUNCE_DELAY_MS = 1000L // Wait 1 second between auto-clicks
        var instance: TouchEmulationService? = null
        var isAutoClickEnabled = false // User control flag
    }
    
    private var lastClickTime = 0L
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d(TAG, "Touch Emulation Service Connected")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Only process if auto-click is enabled by user
        if (!isAutoClickEnabled) {
            return
        }
        
        event?.let {
            // Check for specific events that indicate a send button
            if (it.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                it.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                
                // Debounce: Only process if enough time has passed since last click
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime >= DEBOUNCE_DELAY_MS) {
                    lastClickTime = currentTime // Set immediately to prevent race condition
                    // Look for send buttons and auto-click them
                    findAndClickSendButton()
                }
            }
        }
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Service Interrupted")
    }
    
    override fun onUnbind(intent: android.content.Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }
    
    private fun findAndClickSendButton() {
        val rootNode = rootInActiveWindow ?: return
        
        try {
            // Search for nodes with text containing "send", "share", or "post"
            val sendNodes = mutableListOf<AccessibilityNodeInfo>()
            findSendButtons(rootNode, sendNodes)
            
            // Click the first send button found
            if (sendNodes.isNotEmpty()) {
                val sendButton = sendNodes[0]
                if (sendButton.isClickable) {
                    sendButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Log.d(TAG, "Auto-clicked send button")
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // If not directly clickable, try gesture-based click
                    performGestureClick(sendButton)
                }
            }
            
            // Recycle all collected nodes
            sendNodes.forEach { it.recycle() }
        } finally {
            rootNode.recycle()
        }
    }
    
    private fun findSendButtons(node: AccessibilityNodeInfo, results: MutableList<AccessibilityNodeInfo>) {
        val text = node.text?.toString()?.lowercase() ?: ""
        val contentDesc = node.contentDescription?.toString()?.lowercase() ?: ""
        val resourceId = node.viewIdResourceName?.lowercase() ?: ""
        
        // Check if this node represents a send/share/post button
        if ((text.contains("send") || text.contains("share") || text.contains("post") ||
             contentDesc.contains("send") || contentDesc.contains("share") || contentDesc.contains("post") ||
             resourceId.contains("send") || resourceId.contains("share") || resourceId.contains("post")) &&
            (node.isClickable || node.isFocusable)) {
            results.add(node)
        }
        
        // Recursively search child nodes
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                try {
                    findSendButtons(child, results)
                } finally {
                    child.recycle()
                }
            }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.N)
    private fun performGestureClick(node: AccessibilityNodeInfo) {
        val bounds = android.graphics.Rect()
        node.getBoundsInScreen(bounds)
        
        val x = (bounds.left + bounds.right) / 2f
        val y = (bounds.top + bounds.bottom) / 2f
        
        val path = Path()
        path.moveTo(x, y)
        
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
        
        dispatchGesture(gestureBuilder.build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d(TAG, "Gesture click completed")
            }
            
            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.d(TAG, "Gesture click cancelled")
            }
        }, null)
    }
    
    fun simulateClickAtPosition(x: Float, y: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val path = Path()
            path.moveTo(x, y)
            
            val gestureBuilder = GestureDescription.Builder()
            gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            
            dispatchGesture(gestureBuilder.build(), null, null)
            Log.d(TAG, "Simulated click at ($x, $y)")
        }
    }
}
