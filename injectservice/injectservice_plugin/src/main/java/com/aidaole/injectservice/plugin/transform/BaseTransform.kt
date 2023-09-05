package com.aidaole.injectservice.plugin.transform

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

abstract class BaseTransform : Transform() {

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean = false

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        val outProvider = transformInvocation.outputProvider
        transformInvocation.inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                processJarInput(jarInput, outProvider)
            }
            input.directoryInputs.forEach { directoryInput ->
                processDirectoryInput(directoryInput, outProvider)
            }
        }
    }

    private fun processJarInput(jarInput: JarInput, outProvider: TransformOutputProvider) {
        val dest = outProvider.getContentLocation(
            jarInput.file.absolutePath,
            jarInput.contentTypes,
            jarInput.scopes,
            Format.JAR
        )
        val jarFile = JarFile(jarInput.file)
        val jarOutputStream = JarOutputStream(dest.outputStream())
        for (entry in jarFile.entries()) {
            val entryInputStream = jarFile.getInputStream(entry)
            val newEntry = JarEntry(entry.name)
            jarOutputStream.putNextEntry(newEntry)

            val bytesRead = entryInputStream.readAllBytes()
            if (entry.name.endsWith(".class")) {
                val processedBytes = processClass(bytesRead)
                jarOutputStream.write(processedBytes)
            } else {
                jarOutputStream.write(bytesRead)
            }

            jarOutputStream.closeEntry()
            entryInputStream.close()
        }
        jarOutputStream.close()
    }

    private fun processDirectoryInput(directoryInput: DirectoryInput, outProvider: TransformOutputProvider) {
        val dest = outProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )

        if (directoryInput.file.isDirectory) {
            FileUtils.getAllFiles(directoryInput.file).forEach { file ->
                val filename = file.name
                val fileInputStream = file.inputStream()
                val readBytes = fileInputStream.readAllBytes()
                fileInputStream.close()

                val filePath = file.parentFile.absolutePath + File.separator + filename
                val fos = FileOutputStream(filePath)
                if (filename.endsWith(".class")) {
                    val processedBytes = processClass(readBytes)
                    fos.write(processedBytes)
                } else {
                    fos.write(readBytes)
                }
                fos.close()
            }
        }

        FileUtils.mkdirs(dest)
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private fun processClass(inputBytes: ByteArray): ByteArray {
        val reader = ClassReader(inputBytes)
        val writer = ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
        val visitor = getClassVisitor(writer)
        reader.accept(visitor, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }

    abstract fun getClassVisitor(writer: ClassWriter): ClassVisitor
}