package com.aidaole

import com.aidaole.injectservice.runtime.InjectService
import com.aidaole.lib.interfaces.DialogInterface

@InjectService(DialogInterface::class)
class DialogInterfaceImpl : DialogInterface {
    override fun dialog(): String {
        return "inject dialog"
    }
}