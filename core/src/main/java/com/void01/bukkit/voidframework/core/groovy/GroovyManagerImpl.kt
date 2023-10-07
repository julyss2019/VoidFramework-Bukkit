package com.void01.bukkit.voidframework.core.groovy

import com.void01.bukkit.voidframework.api.common.VoidFramework2
import com.void01.bukkit.voidframework.api.common.groovy.GroovyBinding
import com.void01.bukkit.voidframework.api.common.groovy.GroovyConfig
import com.void01.bukkit.voidframework.api.common.groovy.GroovyManager
import com.void01.bukkit.voidframework.api.common.library.IsolatedClassLoader
import com.void01.bukkit.voidframework.api.common.library.Library
import com.void01.bukkit.voidframework.api.common.library.Repository
import com.void01.bukkit.voidframework.core.VoidFrameworkPlugin
import groovy.lang.Binding
import groovy.lang.GroovyClassLoader
import groovy.lang.GroovyShell
import java.io.File

class GroovyManagerImpl(plugin: VoidFrameworkPlugin) : GroovyManager {
    private val groovyIsolatedClassLoader = IsolatedClassLoader(plugin.javaClass.classLoader)

    private val groovyClassLoaderClass: Class<*>
    private val groovyShellClass: Class<*>
    private val bindingClass: Class<*>
    private val compilerConfigurationClass: Class<*>

    init {
        VoidFramework2.getLibraryManager().load(
            Library.Builder.create()
                .setClassLoader(groovyIsolatedClassLoader)
                .addRepositories(Repository.ALIYUN, Repository.CENTRAL)
                .setDependencyByGradleStyleExpression("org.apache.groovy:groovy:4.0.14")
                .build()
        )

        groovyClassLoaderClass = groovyIsolatedClassLoader.loadClass("groovy.lang.GroovyClassLoader")
        groovyShellClass = groovyIsolatedClassLoader.loadClass("groovy.lang.GroovyShell")
        bindingClass = groovyIsolatedClassLoader.loadClass("groovy.lang.Binding")
        compilerConfigurationClass =
            groovyIsolatedClassLoader.loadClass("org.codehaus.groovy.control.CompilerConfiguration")
    }

    override fun eval(scriptText: String): Any? {
        val groovyConfig = GroovyConfig()

        return eval(scriptText, groovyConfig, GroovyBinding())
    }

    override fun eval(scriptText: String, config: GroovyConfig, binding: GroovyBinding): Any? {
        val groovyShellInst = groovyShellClass
            .getConstructor(ClassLoader::class.java, bindingClass, compilerConfigurationClass)
            .newInstance(
                getMixedClassLoader(config.parentClassLoader),
                createBinding(binding),
                createGroovyCompileConfig(config)
            ) // parent，config，binding
        val groovyClassLoaderInst = groovyShellClass.getDeclaredMethod("getClassLoader").invoke(groovyShellInst)

        addGroovyClassPaths(groovyClassLoaderInst, config)
        return groovyShellClass.getDeclaredMethod("evaluate", String::class.java).invoke(groovyShellInst, scriptText)
    }

    override fun parseClass(sourceText: String): Class<*> {
        return parseClass(sourceText, GroovyConfig())
    }

    override fun parseClass(sourceFile: File): Class<*> {
        return parseClass(sourceFile, GroovyConfig())
    }

    override fun parseClass(sourceText: String, config: GroovyConfig): Class<*> {
        /*
         * Groovy 若要避免冲突，必须使用孤立的加载器，relocate 无法解决问题，因为运行时部分路径从外部读取，会导致找不到类
         * 注意本类里，不要直接引用类，会导致被 bukkit 加载器加载，直接报 ClassNotFoundException
         */


        val groovyClassLoaderInst = groovyClassLoaderClass
            .getConstructor(ClassLoader::class.java, compilerConfigurationClass)
            .newInstance(
                getMixedClassLoader(config.parentClassLoader),
                createGroovyCompileConfig(config)
            ) // parent，config

        // 添加 classpath
        addGroovyClassPaths(groovyClassLoaderInst, config)
        return groovyClassLoaderClass
            .getDeclaredMethod("parseClass", String::class.java)
            .invoke(groovyClassLoaderInst, sourceText) as Class<*>
    }

    override fun parseClass(sourceFile: File, config: GroovyConfig): Class<*> {
        return parseClass(sourceFile.readText(), config)
    }

    private fun addGroovyClassPaths(groovyClassLoaderInst: Any, config: GroovyConfig) {
        config.getClassPaths().forEach {
            groovyClassLoaderClass
                .getDeclaredMethod("addClasspath", String::class.java)
                .invoke(groovyClassLoaderInst, it)
        }
    }

    private fun createBinding(groovyBinding: GroovyBinding): Any {
        val bindingInst = bindingClass.newInstance()
        val method = bindingClass.getDeclaredMethod("setVariable", String::class.java, Any::class.java)

        groovyBinding.getVariables().forEach {
            method.invoke(bindingInst, it.key, it.value)
        }

        return bindingInst
    }

    private fun createGroovyCompileConfig(config: GroovyConfig): Any {
        val compilerConfigurationInst = compilerConfigurationClass.newInstance()

        // 尽管 source 是 UTF-8，但导入类需要进行以下设置，否则可能乱码
        compilerConfigurationClass
            .getDeclaredMethod("setSourceEncoding", String::class.java)
            .invoke(compilerConfigurationInst, config.sourceEncoding)
        return compilerConfigurationInst
    }

    /*
    用了 parentClassLoader 没 groovy
    用了 isolatedClassLoader 没 parent
    进行混合
     */
    private fun getMixedClassLoader(classLoader: ClassLoader): ClassLoader {
        return object : ClassLoader() {
            // 注意使用 loadClass(String, Boolean)
            override fun loadClass(name: String?, resolve: Boolean): Class<*> {
                return try {
                    classLoader.loadClass(name)
                } catch (ex: ClassNotFoundException) {
                    groovyIsolatedClassLoader.loadClass(name)
                }
            }
        }
    }

}