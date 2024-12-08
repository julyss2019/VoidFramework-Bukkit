package com.void01.bukkit.voidframework.common.event

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType


object BukkitEventUtils {
    private val LOOKUP = MethodHandles.lookup()
    private val OPTIONAL_EVENT_HANDLER_DESC = Type.getDescriptor(OptionalEventHandler::class.java)
    private val EVENT_HANDLER_DESC = Type.getDescriptor(EventHandler::class.java)

    /**
     * 仅注册 "可注册的" 事件.
     *
     * 调用 Bukkit.getPluginManager.registerEvents(Plugin, Listener) 注册事件时, 如果 Listener 中存在任何一个参数无法被找到
     * 的方法, Bukkit 会在控制台输出一条错误信息不抛出异常, 导致整个 Listener 的所有事件无法被注册. 本方法提供了一种 "安全" 的注册
     * 方法来避免这个问题. 仅需在可选的事件方法头部添加 [OptionalEventHandler] 即可, 如果方法的参数类可以被找到则注册时间, 否则取
     * 消注册并输出信息.
     */
    @JvmStatic
    fun registerAvailableEvents(plugin: Plugin, listener: Listener) {
        /*val lookup = MethodHandles.lookup()
        val methodType = MethodType.methodType(Void.TYPE, PlayerMoveEvent::class.java)
        val methodHandle: MethodHandle = lookup.findVirtual(listener.javaClass, "onPlayerMoveEvent", methodType)*/
        val clazz = listener.javaClass
        val classLoader = clazz.classLoader
        val className = clazz.name
        val classReader = ClassReader(classLoader.getResourceAsStream(className.replace(".", "/") + ".class"))
        val classNode = ClassNode()

        classReader.accept(classNode, 0)
        classNode.methods.forEach { method ->
            val methodName = method.name
            val annotations = method.visibleAnnotations ?: return@forEach
            val eventHandlerAnnotation = annotations.firstOrNull {
                it.desc.equals(EVENT_HANDLER_DESC)
            } ?: return@forEach

            var priority = EventPriority.NORMAL
            var isIgnoreCancelled = false
            val annotationKeyValues = eventHandlerAnnotation.values

            if (annotationKeyValues != null) {
                for (i in annotationKeyValues.indices step 2) {
                    val key = annotationKeyValues[i] as String
                    val value = annotationKeyValues[i + 1]

                    if (key == "priority") {
                        @Suppress("UNCHECKED_CAST")
                        priority = EventPriority.valueOf((value as Array<String>)[1])
                    } else if (key == "ignoreCancelled") {
                        isIgnoreCancelled = value as Boolean
                    }
                }
            }

            val argTypes = Type.getArgumentTypes(method.desc)

            if (argTypes.size != 1) {
                throw IllegalArgumentException("EventHandler method only accept 1 argument")
            }

            val eventClassName = argTypes[0].className
            val eventClass = try {
                Class.forName(eventClassName)
            } catch (ex: ClassNotFoundException) {
                val isOptionalEventHandler = annotations.any { it.desc.equals(OPTIONAL_EVENT_HANDLER_DESC) }

                if (!isOptionalEventHandler) {
                    throw IllegalArgumentException("Unable to register event '${methodName}' because class '${eventClassName}' not found")
                } else {
                    plugin.logger.info("Ignore registering event '${methodName}' because class '${eventClassName}' not found")
                    return@forEach
                }
            }

            if (!Event::class.java.isAssignableFrom(eventClass)) {
                plugin.logger.info("Unable to register event '${methodName}' because class '${eventClassName}' is not assignable from class '${Event::class.java.name}'")
                return@forEach
            }

            val methodType = MethodType.methodType(Void.TYPE, eventClass)
            val methodHandle = LOOKUP.findVirtual(listener.javaClass, method.name, methodType)

            @Suppress("UNCHECKED_CAST")
            Bukkit.getPluginManager()
                .registerEvent(
                    eventClass as Class<out Event>,
                    listener,
                    priority,
                    { _, event ->
                        if (event.javaClass == eventClass) {
                            methodHandle.invoke(listener, event)
                        }
                    },
                    plugin,
                    isIgnoreCancelled
                )
        }
    }
}