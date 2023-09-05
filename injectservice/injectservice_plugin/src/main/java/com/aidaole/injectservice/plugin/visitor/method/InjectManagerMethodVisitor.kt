package com.aidaole.injectservice.plugin.visitor.method

import com.aidaole.injectservice.plugin.InspectImplManager
import com.aidaole.injectservice.plugin.utils.logi
import com.aidaole.injectservice.plugin.visitor.clazz.InjectManagerClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class InjectManagerMethodVisitor(
    api: Int,
    methodVisitor: MethodVisitor
) : MethodVisitor(api, methodVisitor) {
    companion object {
        private const val TAG = "InjectManagerMethodVisitor"
    }

    override fun visitInsn(opcode: Int) {
        insertRegisterMethod(opcode, this, InspectImplManager.getAllImpls())
        super.visitInsn(opcode)
    }

    private fun insertRegisterMethod(
        opcode: Int,
        methodVisitor: MethodVisitor,
        allImpls: MutableList<Pair<String, String>>
    ) {
        if (opcode == Opcodes.ARETURN || opcode == Opcodes.RETURN) {
            allImpls.forEach { item ->
                doInsertRegister(item.first, item.second, opcode, methodVisitor)
            }
        }
    }

    private fun doInsertRegister(implName: String, interfaceName: String, opcode: Int, methodVisitor: MethodVisitor) {
        "doInsertRegister: $implName, $interfaceName".logi(TAG)
        methodVisitor.visitLdcInsn(Type.getType(interfaceName));
        methodVisitor.visitTypeInsn(Opcodes.NEW, implName);
        methodVisitor.visitInsn(Opcodes.DUP);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, implName, "<init>", "()V", false)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            InjectManagerClassVisitor.INSPECT_MANAGER_NAME,
            "register",
            "(Ljava/lang/Class;Ljava/lang/Object;)V",
            false
        )
    }
}