package com.aidaole

import com.aidaole.injectservice.runtime.InjectManager
import com.aidaole.injectservice.runtime.InjectService
import com.aidaole.lib.interfaces.DialogInterface
import com.aidaole.lib.interfaces.WindowInterface

@InjectService(WindowInterface::class)
class WindowInterfaceImpl : WindowInterface {
    override fun hello(): String {
        val dialog = InjectManager.get(DialogInterface::class.java)
        return "hello " + dialog.dialog()
    }
}