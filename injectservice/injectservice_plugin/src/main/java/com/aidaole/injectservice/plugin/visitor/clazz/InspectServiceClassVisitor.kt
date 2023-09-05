package com.aidaole.injectservice.plugin.visitor.clazz

import com.aidaole.injectservice.plugin.InspectImplManager
import com.aidaole.injectservice.plugin.utils.logi
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class InspectServiceClassVisitor(visitor: ClassVisitor) : ClassVisitor(Opcodes.ASM6, visitor) {
    companion object {
        private const val TAG = "InspectServiceClassVisitor"
        const val INSPECT_SERVICE_NAME = "Lcom/aidaole/injectservice/runtime/InjectService;"
    }

    private var needHook = false
    private var className = ""

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        if (descriptor == INSPECT_SERVICE_NAME) {
            needHook = true
            return AnnotationValueVisitor(api, super.visitAnnotation(descriptor, visible), className)
        } else {
            return super.visitAnnotation(descriptor, visible)
        }
    }

    private class AnnotationValueVisitor(api: Int, av: AnnotationVisitor?, val className: String) :
        AnnotationVisitor(api, av) {
        override fun visit(name: String, value: Any) {
            InspectImplManager.addClass(className, value.toString())
            "find interface: $className,  key:$name -> value:$value".logi(TAG)
            super.visit(name, value)
        }
    }
}