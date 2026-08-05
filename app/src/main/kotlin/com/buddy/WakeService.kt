package com.buddy

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.IBinder

class WakeService : Service() {
    private lateinit var settings: Settings
    private lateinit var notifHelper: NotificationHelper
    private lateinit var wakeDetector: WakeWordDetector
    private lateinit var speechManager: SpeechRecognizerManager
    private lateinit var parser: CommandParser
    private lateinit var router: Router
    private var toneGen: ToneGenerator? = null

    override fun onCreate() {
        super.onCreate()
        settings = Settings.getInstance(this)
        notifHelper = NotificationHelper(this)
        parser = CommandParser(settings)
        router = Router(this, settings)
        speechManager = SpeechRecognizerManager(this)
        toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)

        wakeDetector = WakeWordDetector(this, settings) {
            notifHelper.update("Listening for command...")
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
            speechManager.listen(
                onResult = { text ->
                    notifHelper.update("Processing: $text")
                    val cmd = parser.parse(text)
                    router.route(cmd) { 
                        notifHelper.update("Listening for wake word...")
                        wakeDetector.start()
                    }
                },
                onError = {
                    notifHelper.update("Listening for wake word...")
                    wakeDetector.start()
                }
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") {
            wakeDetector.stop()
            speechManager.destroy()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }
        startForeground(NotificationHelper.ID, notifHelper.build("Listening for wake word..."))
        wakeDetector.start()
        return START_STICKY
    }

    override fun onDestroy() {
        wakeDetector.stop()
        speechManager.destroy()
        toneGen?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
