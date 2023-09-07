package com.github.julyss2019.bukkit.voidframework.command;

import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandParam;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 命令组验证器
 * 用于验证命令组的合法性
 */
public class CommandGroupVerifier {
    private enum State {
        CONTEXT,
        PARAM
    }

    /**
     * 验证
     * @param commandGroup 命令组
     */
    public static void verify(@NonNull CommandGroup commandGroup) {
        Class<? extends CommandGroup> commandGroupClass = commandGroup.getClass();

        for (Method method : commandGroupClass.getDeclaredMethods()) {
            CommandBody commandBodyAnnotation = method.getDeclaredAnnotation(CommandBody.class);

            if (commandBodyAnnotation != null) {
                if (commandBodyAnnotation.value().isEmpty() && commandBodyAnnotation.name().isEmpty()) {
                    throw CommandGroupVerifyException.newMethodException(commandGroup, method, "both value() and name() is empty");
                }

                Parameter[] methodParameters = method.getParameters();
                State state = State.CONTEXT;

                for (Parameter param : methodParameters) {
                    Class<?> paramType = param.getType();
                    CommandParam commandParamAnnotation = param.getAnnotation(CommandParam.class);

                    // 上下文
                    if (commandParamAnnotation == null) {
                        if (state == State.PARAM) {
                            throw CommandGroupVerifyException.newIllegalMethodParamException(commandGroup, method, param, "illegal context position");
                        } else if (!CommandSender.class.isAssignableFrom(paramType)) {
                            throw CommandGroupVerifyException.newIllegalMethodParamException(commandGroup, method, param, "illegal context type");
                        }
                    } else { // 参数
                        state = State.PARAM;
                    }
                }
            }
        }
    }


}
