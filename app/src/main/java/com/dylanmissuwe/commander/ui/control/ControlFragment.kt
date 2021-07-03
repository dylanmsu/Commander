package com.dylanmissuwe.commander.ui.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dylanmissuwe.commander.ControlButton
import com.dylanmissuwe.commander.MyApplication
import com.dylanmissuwe.commander.PtzCamera
import com.dylanmissuwe.commander.R
import com.dylanmissuwe.commander.databinding.FragmentControlBinding


class ControlFragment : Fragment() {

    private lateinit var controlViewModel: ControlViewModel
    private var _binding: FragmentControlBinding? = null

    private var panSpeed: Int = 1
    private var tiltSpeed: Int = 1
    private var zoomSpeed: Int = 1

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        controlViewModel =
            ViewModelProvider(this).get(ControlViewModel::class.java)

        _binding = FragmentControlBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val controlView = view.findViewById<ConstraintLayout>(R.id.control_view)

        val camera: PtzCamera = PtzCamera(MyApplication.client)

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

        val buttonUp = view.findViewById<ControlButton>(R.id.button_up)
        buttonUp.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Thread {
                        camera.TiltUp(tiltSpeed)
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.TiltStop()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonDown = view.findViewById<ControlButton>(R.id.button_down)
        buttonDown.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Thread {
                        camera.TiltDown(tiltSpeed)
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.TiltStop()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonleft = view.findViewById<ControlButton>(R.id.button_left)
        buttonleft.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Thread {
                        camera.PanLeft(panSpeed)
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.PanStop()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonright = view.findViewById<ControlButton>(R.id.button_right)
        buttonright.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Thread {
                        camera.PanRight(panSpeed)
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.PanStop()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonZoomIn = view.findViewById<ControlButton>(R.id.button_zoom_in)
        buttonZoomIn.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Thread {
                        camera.ZoomIn(zoomSpeed)
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.ZoomStop()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonZoomOut = view.findViewById<ControlButton>(R.id.button_zoom_out)
        buttonZoomOut.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Thread {
                        camera.ZoomOut(zoomSpeed)
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.ZoomStop()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonHome = view.findViewById<ControlButton>(R.id.button_home)
        buttonHome.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.Home()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonMute = view.findViewById<ControlButton>(R.id.button_mute)
        buttonMute.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.VideoMuteToggle()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

        val buttonSleep = view.findViewById<ControlButton>(R.id.button_off)
        buttonSleep.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    Thread {
                        camera.StandbyToggle()
                    }.start()
                    return@OnTouchListener true // if you want to handle the touch event
                }
            }
            false
        })

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}