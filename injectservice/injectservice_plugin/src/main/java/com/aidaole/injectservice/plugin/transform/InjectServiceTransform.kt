package com.aidaole.injectservice.plugin.transform

import com.android.build.api.transform.TransformInvocation
import com.aidaole.injectservice.plugin.utils.logi
import com.aidaole.injectservice.plugin.visitor.clazz.InspectServiceClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class InjectServiceTransform : BaseTransform() {
    override fun getName(): String {
        return "InspectServiceTransform"
    }

    private fun printName() {
        ("   +-----------------------------+   ").logi()
        ("   |                             |   ").logi()
        ("   |    InspectServiceTransform  |   ").logi()
        ("   |                             |   ").logi()
        ("   +-----------------------------+   ").logi()
    }

    override fun transform(transformInvocation: TransformInvocation) {
        printName()
        super.transform(transformInvocation)
    }

    override fun getClassVisitor(writer: ClassWriter): ClassVisitor {
        return InspectServiceClassVisitor(writer)
    }
}