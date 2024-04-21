package com.github.julyss2019.bukkit.voidframework.command.tree.element;

import com.github.julyss2019.bukkit.voidframework.command.SenderType;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody;
import com.github.julyss2019.bukkit.voidframework.command.CommandGroupContext;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.SenderParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.ArrayCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.FixedCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.OptionalCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.CommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.SenderSenderParam;
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
    private final List<SenderParam> senderParams = new ArrayList<>();
    private final List<CommandParam> commandParams = new ArrayList<>();
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
            com.github.julyss2019.bukkit.voidframework.command.annotation.CommandParam annotation = parameter.getAnnotation(com.github.julyss2019.bukkit.voidframework.command.annotation.CommandParam.class);

            // 用户输入参数
            if (annotation != null) {
                String description = annotation.description();

                if (annotation.optional()) {
                    commandParams.add(new OptionalCommandParam(type, description));
                } else if (type.isArray()) {
                    commandParams.add(new ArrayCommandParam(type, description, type.getComponentType()));
                } else {
                    commandParams.add(new FixedCommandParam(type, description));
                }
            } else { // 上下文参数
                if (CommandSender.class.isAssignableFrom(type)) {
                    senderParams.add(new SenderSenderParam(parameter.getType()));
                }
            }
        }

        // String[] a
        // String a, String[] b
        // String a, [Optional]String a

        if (commandParams.isEmpty()) {
            // 空的直接赋 0, 避免越界
            this.minInputParamCount = 0;
            this.maxInputParamCount = 0;
        } else if (commandParams.get(0).getType().isArray()) {
            // 首个为数组 [0, inf)
            // String[] a
            this.minInputParamCount = 0;
            this.maxInputParamCount = Integer.MAX_VALUE;
        } else if (commandParams.get(commandParams.size() - 1).getType().isArray()) {
            // 末尾为数组 [size -1, inf)
            // String a, String[] b
            this.minInputParamCount = commandParams.size() - 1;
            this.maxInputParamCount = Integer.MAX_VALUE;
        } else {
            // String a, String b
            // String a, String b, [String] c
            for (CommandParam commandParam : commandParams) {
                if (commandParam instanceof FixedCommandParam) {
                    this.minInputParamCount++;
                } else {
                    break;
                }
            }

            this.maxInputParamCount = commandParams.size();
        }
    }

    public List<SenderParam> getSenderParams() {
        return Collections.unmodifiableList(senderParams);
    }

    public List<CommandParam> getCommandParams() {
        return Collections.unmodifiableList(commandParams);
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
