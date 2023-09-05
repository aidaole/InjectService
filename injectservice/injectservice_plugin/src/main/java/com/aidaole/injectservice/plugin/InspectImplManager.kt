package com.aidaole.injectservice.plugin

import com.aidaole.injectservice.plugin.utils.logi

object InspectImplManager {
    private const val TAG = "InspectImplManager"
    private var classPoll = mutableListOf<Pair<String, String>>()

    fun addClass(implName: String, interfaceName: String) {
        classPoll.add(Pair(implName, interfaceName))
    }

    fun printInfo() {
        classPoll.forEach {
            "printInfo: $it".logi(TAG)
        }
    }

    fun getAllImpls(): MutableList<Pair<String, String>> {
        return classPoll
    }
}