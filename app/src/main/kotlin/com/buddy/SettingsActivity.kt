package com.buddy

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val s = Settings.getInstance(this)
        
        val etDev = findViewById<EditText>(R.id.etDeviceName).apply { setText(s.deviceName) }
        val etWake = findViewById<EditText>(R.id.etWakeWord).apply { setText(s.wakeWord) }
        val etPeerWake = findViewById<EditText>(R.id.etPeerWakeWord).apply { setText(s.peerWakeWord) }
        val etHost = findViewById<EditText>(R.id.etHost).apply { setText(s.peerHost) }
        val etPort = findViewById<EditText>(R.id.etPort).apply { setText(s.peerPort.toString()) }
        val etUser = findViewById<EditText>(R.id.etUser).apply { setText(s.peerUser) }
        val etPass = findViewById<EditText>(R.id.etPass).apply { setText(s.peerPass) }

        findViewById<MaterialButton>(R.id.btnSave).setOnClickListener {
            s.deviceName = etDev.text.toString()
            s.wakeWord = etWake.text.toString()
            s.peerWakeWord = etPeerWake.text.toString()
            s.peerHost = etHost.text.toString()
            s.peerPort = etPort.text.toString().toIntOrNull() ?: 8022
            s.peerUser = etUser.text.toString()
            s.peerPass = etPass.text.toString()
            Toast.makeText(this, "Configuration Saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
