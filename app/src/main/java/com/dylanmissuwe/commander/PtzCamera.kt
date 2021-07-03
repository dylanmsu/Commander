package com.dylanmissuwe.commander

import kotlin.math.max
import kotlin.math.min

class PtzCamera {
    private var client: TelnetClient = TelnetClient()

    constructor(client: TelnetClient){
        this.client = client
    }

    private fun clamp(num: Int, maximum: Int, minimum: Int): Int {
        return min(max(num, minimum), maximum)
    }

    // PAN
    public fun PanLeft(speed: Int){
        client.sendAndWaitForOk("camera pan left " + clamp(speed,24,1))
    }
    public fun PanLeft(){
        client.sendAndWaitForOk("camera pan left")
    }

    public fun PanRight(speed: Int){
        client.sendAndWaitForOk("camera pan right " + clamp(speed,24,1))
    }
    public fun PanRight(){
        client.sendAndWaitForOk("camera pan right")
    }

    public fun PanStop(){
        client.sendAndWaitForOk("camera pan stop")
    }

    //TILT
    public fun TiltUp(speed: Int){
        client.sendAndWaitForOk("camera tilt up " + clamp(speed,20,1))
    }
    public fun TiltUp(){
        client.sendAndWaitForOk("camera tilt up")
    }

    public fun TiltDown(speed: Int){
        client.sendAndWaitForOk("camera tilt down " + clamp(speed,20,1))
    }
    public fun TiltDown(){
        client.sendAndWaitForOk("camera tilt down")
    }

    public fun TiltStop(){
        client.sendAndWaitForOk("camera tilt stop")
    }

    //ZOOM
    public fun ZoomIn(speed: Int){
        client.sendAndWaitForOk("camera zoom in " + clamp(speed,7,1))
    }
    public fun ZoomIn(){
        client.sendAndWaitForOk("camera zoom in")
    }

    public fun ZoomOut(speed: Int){
        client.sendAndWaitForOk("camera zoom out " + clamp(speed,7,1))
    }
    public fun ZoomOut(){
        client.sendAndWaitForOk("camera zoom out")
    }
    public fun ZoomStop(){
        client.sendAndWaitForOk("camera zoom stop")
    }

    public fun Home(){
        client.sendAndWaitForOk("camera home")
    }

    public fun VideoMuteToggle(){
        client.sendAndWaitForOk("video mute toggle")
    }

    public fun StandbyToggle(){
        client.sendAndWaitForOk("camera standby toggle")
    }
}