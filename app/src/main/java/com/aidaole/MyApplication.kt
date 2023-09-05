package com.aidaole

import android.app.Application
import android.content.Context
import com.aidaole.injectservice.runtime.InjectManager

class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        InjectManager.init()
    }
}