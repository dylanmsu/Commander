package com.dylanmissuwe.commander.ui.connect

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dylanmissuwe.commander.MyApplication
import com.dylanmissuwe.commander.NetworkInfo
import com.dylanmissuwe.commander.R
import com.dylanmissuwe.commander.TelnetClient
import com.dylanmissuwe.commander.databinding.FragmentConnectBinding


class ConnectFragment : Fragment() {

    private lateinit var controlViewModel: ConnectViewModel
    private var _binding: FragmentConnectBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        controlViewModel =
            ViewModelProvider(this).get(ConnectViewModel::class.java)

        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ipAddress = view.findViewById<EditText>(R.id.ip_address)
        val login = view.findViewById<EditText>(R.id.login)
        val password = view.findViewById<EditText>(R.id.password)
        val remember = view.findViewById<SwitchCompat>(R.id.switch1)

        val prefs = requireActivity().getSharedPreferences("user_login", Context.MODE_PRIVATE)
        val rememberCredentials = prefs.getBoolean("remember_credentials", false)

        remember.setOnClickListener {
            val prefsEdit = prefs.edit()
            prefsEdit.putBoolean("remember_credentials", remember.isChecked)
            prefsEdit.apply()
            prefsEdit.commit()

        }

        remember.setChecked(rememberCredentials)

        if (remember.isChecked) {
            ipAddress.setText(prefs.getString("ip_address",""))
            login.setText(prefs.getString("login", ""))
            password.setText(prefs.getString("password", ""))
        }

        val button: Button = view.findViewById(R.id.connect_button)
        button.isEnabled = !MyApplication.client.isLoggedIn()
        button.setOnClickListener {
            Connect(view, ipAddress.text.toString(), login.text.toString(), password.text.toString(), remember.isChecked)
        }
    }

    private fun Connect(view: View, ip_address: String, login: String, password: String, remember: Boolean) {

        val loading = view.findViewById<ProgressBar>(R.id.progressBar)
        loading.visibility = View.VISIBLE
        Thread {
            val port = 23
            val networkInfo = NetworkInfo(ip_address)
            val isreachable = networkInfo.IsReachable(port)
            var isLoggedIn = false

            if (isreachable) {
                MyApplication.client = TelnetClient(ip_address, port)
                MyApplication.client.Connect()

                MyApplication.client.Login(login, password)
                isLoggedIn = MyApplication.client.isLoggedIn()
            }

            activity?.runOnUiThread {
                val toolbar = requireActivity().findViewById<View>(R.id.toolbar3) as Toolbar
                loading?.visibility = View.GONE
                if (isreachable) {
                    if (isLoggedIn) {
                        val prefs = requireActivity().getSharedPreferences("user_login", Context.MODE_PRIVATE).edit()
                        prefs.putBoolean("remember_credentials", remember)

                        if (remember) {
                            prefs.putString("ip_address", ip_address)
                            prefs.putString("login", login)
                            prefs.putString("password", password)
                        }

                        prefs.apply()
                        prefs.commit()

                        val button: Button = view.findViewById(R.id.connect_button)
                        button.isEnabled = false

                        toolbar.visibility = View.VISIBLE
                        Toast.makeText(context, "Successfully logged in!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Host is reachable but login is incorrect", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "$ip_address on port $port is not reachable", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}