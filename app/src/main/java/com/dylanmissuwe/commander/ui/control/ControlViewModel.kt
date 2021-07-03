package com.dylanmissuwe.commander.ui.control

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ControlViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is control Fragment"
    }
    val text: LiveData<String> = _text
}