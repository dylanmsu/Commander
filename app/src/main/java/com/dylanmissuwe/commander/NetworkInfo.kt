package com.dylanmissuwe.commander

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class NetworkInfo {
    private var ip_address: String = ""

    constructor(ip_address: String){
        this.ip_address = ip_address
    }

    public fun IsReachable(port: Int): Boolean {
        var exists = false

        try {
            val sockaddr: SocketAddress = InetSocketAddress(ip_address,port)
            // Create an unbound socket
            val sock = Socket()

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            val timeoutMs = 2000 // 2 seconds
            sock.connect(sockaddr, timeoutMs)
            exists = true
        } catch (e: IOException) {
            // Handle exception
        }

        return exists
    }
}