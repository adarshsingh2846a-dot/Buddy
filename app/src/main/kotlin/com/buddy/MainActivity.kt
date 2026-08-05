package com.buddy

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private var isRunning = false
    private var pulseAnim: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnToggle = findViewById<MaterialButton>(R.id.btnToggle)
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val tvWakeWord = findViewById<TextView>(R.id.tvWakeWord)
        val micPulse = findViewById<View>(R.id.micPulse)

        tvWakeWord.text = "Wake Word: ${Settings.getInstance(this).wakeWord}"

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnToggle.setOnClickListener {
            if (isRunning) {
                startService(Intent(this, WakeService::class.java).apply { action = "STOP" })
                isRunning = false
                tvStatus.text = "System Offline"
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.buddy_error))
                btnToggle.text = "INITIALIZE CORE"
                pulseAnim?.cancel()
                micPulse.visibility = View.GONE
            } else {
                if (checkPerms()) {
                    ContextCompat.startForegroundService(this, Intent(this, WakeService::class.java))
                    isRunning = true
                    tvStatus.text = "System Online - Listening"
                    tvStatus.setTextColor(ContextCompat.getColor(this, R.color.buddy_primary))
                    btnToggle.text = "DEACTIVATE CORE"
                    pulseAnim = Utils.startPulseAnimation(micPulse)
                } else {
                    reqPerms()
                }
            }
        }
    }

    private fun checkPerms(): Boolean {
        val p1 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        val p2 = if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
        return p1 && p2
    }

    private fun reqPerms() {
        val perms = mutableListOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE)
        if (Build.VERSION.SDK_INT >= 33) perms.add(Manifest.permission.POST_NOTIFICATIONS)
        ActivityCompat.requestPermissions(this, perms.toTypedArray(), 1)
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.tvWakeWord).text = "Wake Word: ${Settings.getInstance(this).wakeWord}"
    }
}
