package com.nekoclub.repoone

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat

class OverlayService : Service() {
    
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var isExpanded = false
    
    companion object {
        var isRunning = false
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        isRunning = true
        createOverlayView()
    }
    
    private fun createOverlayView() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // Inflate the overlay layout
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)
        
        // Set up window parameters
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100
        
        // Add the view to window
        windowManager?.addView(overlayView, params)
        
        // Set up drag functionality
        setupDragListener(params)
        
        // Set up overlay controls
        setupOverlayControls()
    }
    
    private fun setupDragListener(params: WindowManager.LayoutParams) {
        overlayView?.findViewById<View>(R.id.overlayDragHandle)?.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager?.updateViewLayout(overlayView, params)
                        return true
                    }
                }
                return false
            }
        })
    }
    
    private fun setupOverlayControls() {
        val btnExpand = overlayView?.findViewById<ImageView>(R.id.btnExpand)
        val btnClose = overlayView?.findViewById<Button>(R.id.btnClose)
        val btnOpenApp = overlayView?.findViewById<Button>(R.id.btnOpenApp)
        val expandedPanel = overlayView?.findViewById<View>(R.id.expandedPanel)
        
        btnExpand?.setOnClickListener {
            isExpanded = !isExpanded
            expandedPanel?.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
        
        btnClose?.setOnClickListener {
            stopSelf()
        }
        
        btnOpenApp?.setOnClickListener {
            val intent = Intent(this, LauncherActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        if (overlayView != null) {
            windowManager?.removeView(overlayView)
            overlayView = null
        }
    }
}
