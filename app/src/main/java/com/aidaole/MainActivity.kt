package com.aidaole

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aidaole.plugin.inspectservice.R
import com.aidaole.injectservice.runtime.InjectManager
import com.aidaole.lib.interfaces.WindowInterface

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val windowImpl = InjectManager.get(WindowInterface::class.java)
        findViewById<TextView>(R.id.test_text).text = windowImpl.hello()
    }
}