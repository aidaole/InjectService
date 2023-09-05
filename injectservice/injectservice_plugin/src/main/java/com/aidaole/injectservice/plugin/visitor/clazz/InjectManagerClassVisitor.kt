package com.aidaole.injectservice.plugin.visitor.clazz

import com.aidaole.injectservice.plugin.utils.logi
import com.aidaole.injectservice.plugin.visitor.method.InjectManagerMethodVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class InjectManagerClassVisitor(visitor: ClassVisitor, val classPath: String = "") :
    ClassVisitor(Opcodes.ASM6, visitor) {
    companion object {
        private const val TAG = "InjectManagerClassVisitor"
        public const val INSPECT_MANAGER_NAME = "com/aidaole/injectservice/runtime/InjectManager"
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
        if (className == INSPECT_MANAGER_NAME) {
            "find manager: $name".logi(TAG)
            needHook = true
        }
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        var methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (needHook) {
            if (name == "registerAll") {
                "find method: $name, $descriptor".logi(TAG)
                methodVisitor = InjectManagerMethodVisitor(api, methodVisitor)
            }
        }
        return methodVisitor
    }
}