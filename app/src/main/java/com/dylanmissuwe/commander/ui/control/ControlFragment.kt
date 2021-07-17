package com.dylanmissuwe.commander.ui.control

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dylanmissuwe.commander.*
import com.dylanmissuwe.commander.databinding.FragmentControlBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class ControlFragment : Fragment() {

    private lateinit var controlViewModel: ControlViewModel
    private var _binding: FragmentControlBinding? = null

    private var panSpeed: Int = 12
    private var tiltSpeed: Int = 10
    private var zoomSpeed: Int = 4

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        controlViewModel = ViewModelProvider(this).get(ControlViewModel::class.java)

        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        _binding = FragmentControlBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val camera = PtzCamera(MyApplication.client)

        val panSpeedView = view.findViewById<SeekBar>(R.id.pan_speed)
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
        val tiltSpeedView = view.findViewById<SeekBar>(R.id.tilt_speed)
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
        val zoomSpeedView = view.findViewById<SeekBar>(R.id.zoom_speed)
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
            val button = view.findViewById<ControlButton>(buttonId)

            button.setOnTouchListener(View.OnTouchListener { v, event ->
                v.performClick()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Thread {
                            try {
                                when (buttons[i]) {
                                    "button_up" -> { camera.TiltUp(tiltSpeed) }
                                    "button_down" -> { camera.TiltDown(tiltSpeed) }
                                    "button_left" -> { camera.PanLeft(tiltSpeed) }
                                    "button_right" -> { camera.PanRight(tiltSpeed) }
                                    "button_zoom_in" -> { camera.ZoomIn(tiltSpeed) }
                                    "button_zoom_out" -> { camera.ZoomOut(tiltSpeed) }
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

        val grid = view.findViewById<GridLayout>(R.id.gridLayout)
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
            for (i in 0 until linearlayout.childCount){
                val buttonview: View = linearlayout.getChildAt(i)
                buttonview.isEnabled = false
                buttonview.alpha = 0.5f
            }
        }

    }

    private fun ThrowError() {
        activity?.runOnUiThread {
            Toast.makeText(context, "Not logged in anymore", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}