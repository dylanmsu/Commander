package com.dylanmissuwe.commander.ui

//import com.dylanmissuwe.commander.ui.databinding.ActivityFullscreenBinding
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.dylanmissuwe.commander.*
import com.dylanmissuwe.commander.databinding.ActivityFullscreenBinding
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultAllocator

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var fullscreenContent: PlayerView
    private lateinit var fullscreenContentControls: ConstraintLayout
    private val hideHandler = Handler()

    private var panSpeed: Int = 12
    private var tiltSpeed: Int = 10
    private var zoomSpeed: Int = 4

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreenContent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        //supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.videoPlayer
        fullscreenContent.setOnTouchListener { v, event -> toggle(); false }

        fullscreenContentControls = binding.fullscreenContentControls

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //binding.dummyButton.setOnTouchListener(delayHideTouchListener)

        val loadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE))
            .setBufferDurationsMs(
                50,
                50,
                0,
                0
            )
            .setTargetBufferBytes(DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES)
            .setPrioritizeTimeOverSizeThresholds(DefaultLoadControl.DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS)
            .createDefaultLoadControl()

        val mediaSource: MediaSource = RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri("rtsp://192.168.0.123/vaddio-conferenceshot-stream"))
        val player = SimpleExoPlayer.Builder(this.applicationContext).setLoadControl(loadControl).build()

        val playerView = findViewById(R.id.videoPlayer) as PlayerView
        playerView.setPlayer(player);

        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()

        val camera = PtzCamera(MyApplication.client)

        val panSpeedView = findViewById<SeekBar>(R.id.pan_speed)
        panSpeedView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                panSpeed = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })
        val tiltSpeedView = findViewById<SeekBar>(R.id.tilt_speed)
        tiltSpeedView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tiltSpeed = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })
        val zoomSpeedView = findViewById<SeekBar>(R.id.zoom_speed)
        zoomSpeedView.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                zoomSpeed = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //
            }

        })

        val buttons = arrayOf<String>(
            "button_up",
            "button_down",
            "button_left",
            "button_right",
            "button_zoom_in",
            "button_zoom_out",
            "button_home",
            "button_mute",
            "button_off"
        )

        for (i in buttons.indices){
            val buttonId = getResources().getIdentifier(buttons[i], "id", BuildConfig.APPLICATION_ID)
            val button = findViewById<ControlButton>(buttonId)

            button.setOnTouchListener(View.OnTouchListener { v, event ->
                v.performClick()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Thread {
                            try {
                                when (buttons[i]) {
                                    "button_up" -> { camera.TiltUp(tiltSpeed) }
                                    "button_down" -> { camera.TiltDown(tiltSpeed) }
                                    "button_left" -> { camera.PanLeft(panSpeed) }
                                    "button_right" -> { camera.PanRight(panSpeed) }
                                    "button_zoom_in" -> { camera.ZoomIn(zoomSpeed) }
                                    "button_zoom_out" -> { camera.ZoomOut(zoomSpeed) }
                                    "button_home" -> { camera.Home() }
                                    "button_mute" -> { camera.VideoMuteToggle() }
                                    "button_off" -> { camera.StandbyToggle() }
                                }
                            } catch (e :java.net.SocketException) {
                                //ThrowError()
                            }
                        }.start()
                        return@OnTouchListener true // if you want to handle the touch event
                    }
                    MotionEvent.ACTION_UP -> {
                        Thread {
                            try {
                                when (buttons[i]) {
                                    "button_up" -> { camera.TiltStop() }
                                    "button_down" -> { camera.TiltStop() }
                                    "button_left" -> { camera.PanStop() }
                                    "button_right" -> { camera.PanStop() }
                                    "button_zoom_in" -> { camera.ZoomStop() }
                                    "button_zoom_out" -> { camera.ZoomStop() }
                                }
                            } catch (e :java.net.SocketException) {
                                ThrowError()
                            }
                        }.start()
                        return@OnTouchListener true // if you want to handle the touch event
                    }
                }
                false
            })
        }

        val exitButton = findViewById(R.id.button_exit) as ControlButton
        exitButton.setOnClickListener {
            MyApplication.client.Disconnect()

            val grid = findViewById<View>(R.id.gridLayout) as GridLayout?
            grid?.let {
                for (i in 0 until it.childCount){
                    val view: View = it.getChildAt(i)
                    view.isEnabled = false
                    view.alpha = 0.5f
                }
            }

            val linearlayout = findViewById<View>(R.id.sliders) as LinearLayout?
            linearlayout?.let {
                for (i in 0 until it.childCount){
                    val view: View = it.getChildAt(i)
                    view.isEnabled = false
                    //view.alpha = 0.5f
                }
            }

            val button = findViewById<View>(R.id.connect_button) as Button?
            button?.isEnabled = true

            Toast.makeText(this, "Disconnected", Toast.LENGTH_LONG).show()

            finish()
        }

        /*val grid = view.findViewById<GridLayout>(R.id.gridLayout)
        if (MyApplication.client.isLoggedIn()){
            for (i in 0 until grid.childCount){
                val buttonview: View = grid.getChildAt(i)
                buttonview.isEnabled = true
                buttonview.alpha = 1f
            }
        } else {
            for (i in 0 until grid.childCount){
                val buttonview: View = grid.getChildAt(i)
                buttonview.isEnabled = false
                buttonview.alpha = 0.5f
            }
        }

        val linearlayout = view.findViewById<LinearLayout>(R.id.sliders)
        if (MyApplication.client.isLoggedIn()){
            for (i in 0 until linearlayout.childCount){
                val buttonview: View = linearlayout.getChildAt(i)
                buttonview.isEnabled = true
                buttonview.alpha = 1f
            }
        } else {
            for (i in 0 until linearlayout.childCount) {
                val buttonview: View = linearlayout.getChildAt(i)
                buttonview.isEnabled = false
                buttonview.alpha = 0.5f
            }
        }*/
    }

    private fun ThrowError() {
        this.runOnUiThread {
            Toast.makeText(this, "Not logged in anymore", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        //supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}