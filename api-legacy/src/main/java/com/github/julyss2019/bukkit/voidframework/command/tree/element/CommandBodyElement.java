package com.github.julyss2019.bukkit.voidframework.command.tree.element;

import com.github.julyss2019.bukkit.voidframework.command.SenderType;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandParam;
import com.github.julyss2019.bukkit.voidframework.command.CommandGroupContext;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.ContextMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.ArrayUserInputMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.FixedUserInputMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.OptionalUserInputMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.UserInputMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.SenderContextMethodParam;
import lombok.NonNull;
import lombok.ToString;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
public class CommandBodyElement extends BaseCommandElement {
    private final String description; // 默认介绍, 即写死在代码里的介绍
    private final Method method;
    private final SenderType[] senderTypes;
    private final Object commandGroupInst;
    private final List<ContextMethodParam> contextMethodParams = new ArrayList<>();
    private final List<UserInputMethodParam> userInputMethodParams = new ArrayList<>();
    private int minInputParamCount;
    private int maxInputParamCount;

    public CommandBodyElement(@NonNull CommandGroupContext commandGroupContext, @NonNull Method method, @NonNull CommandBody commandBodyAnnotation) {
        setHolder(commandGroupContext);

        String tmp = commandBodyAnnotation.value();

        // 兼容两种
        if (!tmp.isEmpty()) {
            setCommandId(tmp);
        } else {
            setCommandId(commandBodyAnnotation.name());
        }

        setPermission(commandBodyAnnotation.permission());

        this.method = method;
        this.description = commandBodyAnnotation.description();
        this.senderTypes = commandBodyAnnotation.senders();
        this.commandGroupInst = commandGroupContext.getCommandGroup();

        // 解析参数
        for (Parameter parameter : method.getParameters()) {
            Class<?> type = parameter.getType();
            CommandParam annotation = parameter.getAnnotation(CommandParam.class);

            // 用户输入参数
            if (annotation != null) {
                String description = annotation.description();

                if (annotation.optional()) {
                    userInputMethodParams.add(new OptionalUserInputMethodParam(type, description));
                } else if (type.isArray()) {
                    userInputMethodParams.add(new ArrayUserInputMethodParam(type, description, type.getComponentType()));
                } else {
                    userInputMethodParams.add(new FixedUserInputMethodParam(type, description));
                }
            } else { // 上下文参数
                if (CommandSender.class.isAssignableFrom(type)) {
                    contextMethodParams.add(new SenderContextMethodParam(parameter.getType()));
                }
            }
        }

        // String[] a
        // String a, String[] b
        // String a, [Optional]String a

        if (userInputMethodParams.isEmpty()) {
            // 空的直接赋 0, 避免越界
            this.minInputParamCount = 0;
            this.maxInputParamCount = 0;
        } else if (userInputMethodParams.get(0).getType().isArray()) {
            // 首个为数组 [0, inf)
            // String[] a
            this.minInputParamCount = 0;
            this.maxInputParamCount = Integer.MAX_VALUE;
        } else if (userInputMethodParams.get(userInputMethodParams.size() - 1).getType().isArray()) {
            // 末尾为数组 [size -1, inf)
            // String a, String[] b
            this.minInputParamCount = userInputMethodParams.size() - 1;
            this.maxInputParamCount = Integer.MAX_VALUE;
        } else {
            // String a, String b
            // String a, String b, [String] c
            for (UserInputMethodParam userInputMethodParam : userInputMethodParams) {
                if (userInputMethodParam instanceof FixedUserInputMethodParam) {
                    this.minInputParamCount++;
                } else {
                    break;
                }
            }

            this.maxInputParamCount = userInputMethodParams.size();
        }
    }

    public List<ContextMethodParam> getContextMethodParams() {
        return Collections.unmodifiableList(contextMethodParams);
    }

    public List<UserInputMethodParam> getUserInputMethodParams() {
        return Collections.unmodifiableList(userInputMethodParams);
    }

    /**
     * 执行方法
     *
     * @param args 参数
     */
    public void invokeMethod(Object... args) {
        try {
            method.invoke(commandGroupInst, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 最小命令参数数量
     */
    public int getMinInputParamCount() {
        return minInputParamCount;
    }

    /**
     * 最大命令参数数量（仅含带 @Param 注解的）
     */
    public int getMaxInputParamCount() {
        return maxInputParamCount;
    }

    /**
     * 获取命令体描述（经过本土化处理）
     */
    public String getDescription() {
        return description;
    }

    public Method getMethod() {
        return method;
    }

    public SenderType[] getSenderTypes() {
        return senderTypes;
    }
}
