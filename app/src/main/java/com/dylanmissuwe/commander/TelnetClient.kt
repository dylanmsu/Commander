package com.dylanmissuwe.commander

import android.view.View
import android.widget.Toast
import java.net.Socket
import java.util.*

class TelnetClient {
    private var ip_adress: String = ""
    private var port: Int = 0
    private var login_correct = false
    private var client: Socket = Socket()

    constructor() {}

    constructor(ip_adress: String, port: Int){
        this.ip_adress = ip_adress
        this.port = port
    }

    public fun setPort(port: Int) {
        this.port = port
    }

    public fun setAddress(ip_adress: String) {
        this.ip_adress = ip_adress
    }

    public fun isLoggedIn(): Boolean {
        return login_correct && (!client.isClosed && client.isConnected)
    }

    public fun Connect() {
        try {
            client = Socket(ip_adress, port)
        }
        catch (e: Exception){
            println("Connection error: $e")
            login_correct = false
        }
    }

    public fun Disconnect(){
        val output = client.getOutputStream()
        output.write(("exit\r\n").toByteArray())
        login_correct = false
        client.close();
    }

    public fun sendAndWaitForOk(command: String): Boolean {
        val output = client.getOutputStream()
        val input = client.getInputStream()

        output.write((command+"\r\n").toByteArray())

        var stop = false
        var response = ""
        while (!stop) {
            response += input.read().toChar()
            if (response.endsWith("OK")){
                stop = true
            }
        }
        println(response)
        return true
    }

    public fun Login(login: String, password: String) {
        try {
            val output = client.getOutputStream()
            val input = client.getInputStream()

            var stop = false
            var response = ""
            while (!stop) {
                response += input.read().toChar()
                if (response.endsWith("login:")){
                    stop = true
                }
            }
            println(response)
            output.write((login+"\r\n").toByteArray())

            stop = false
            response = ""
            while (!stop) {
                response += input.read().toChar()
                if (response.endsWith("Password:")){
                    stop = true
                }
            }
            println(response)
            output.write((password+"\r\n").toByteArray())

            stop = false
            response = ""
            while (!stop) {
                response += input.read().toChar()
                if (response.endsWith("login:") && !response.endsWith("Last login:")){
                    stop = true
                    println("login incorrect")
                    login_correct = false
                }

                if (response.contains("Welcome")){
                    stop = true
                    println("login correct!")
                    login_correct = true
                }
            }
            println(response)
        }
        catch (e: Exception) {
            println("Login error: $e")
            login_correct = false
        }
    }
}