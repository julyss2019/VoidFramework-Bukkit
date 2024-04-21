package com.github.julyss2019.bukkit.voidframework.command;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.command.failure.CommandFailureHandler;
import com.github.julyss2019.bukkit.voidframework.command.helper.CommandHelper;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.SenderParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.SenderSenderParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.ArrayCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.CommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.FixedCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.OptionalCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.util.BukkitCommandUtil;
import com.github.julyss2019.bukkit.voidframework.command.param.parser.ParamParser;
import com.github.julyss2019.bukkit.voidframework.command.param.parser.Response;
import com.github.julyss2019.bukkit.voidframework.command.param.tab.completer.ParamTabCompleter;
import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import com.github.julyss2019.bukkit.voidframework.command.tree.RootCommandTree;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandBodyElement;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandElement;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandMappingElement;
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.internal.logger.LegacyPluginLogger;
import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import com.github.julyss2019.bukkit.voidframework.text.Texts;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 命令总控
 */
public class CommandManager {
    private final LegacyVoidFrameworkPlugin legacyVoidFrameworkPlugin;
    private final LegacyPluginLogger logger;
    /**
     * -- GETTER --
     * 获取顶级命令树
     */
    @Getter
    private final CommandTree rootCommandTree = new RootCommandTree();
    private final Set<String> registeredBukkitCommandIds = new HashSet<>();
    private final Set<CommandFramework> commandFrameworks = new HashSet<>();

    public CommandManager(@NonNull LegacyVoidFrameworkPlugin plugin) {
        this.legacyVoidFrameworkPlugin = plugin;
        this.logger = plugin.getPluginLogger();
    }

    /**
     * 注销一个插件的所有命令框架
     *
     * @param plugin 插件
     */
    public void unregisterCommandFrameworks(@NonNull Plugin plugin) {
        Iterator<CommandFramework> iterator = commandFrameworks.iterator();

        while (iterator.hasNext()) {
            CommandFramework commandFramework = iterator.next();

            if (commandFramework.getPlugin().equals(plugin)) {
                unregisterCommandFramework0(commandFramework);
                iterator.remove();
            }
        }
    }

    /**
     * 注销一个插件的所有命令框架
     *
     * @param commandFramework 命令框架
     */
    private void unregisterCommandFramework0(CommandFramework commandFramework) {
        commandFramework.unregisterCommandGroups();
    }

    /**
     * 注销一个插件的所有命令框架
     *
     * @param commandFramework 命令框架
     */
    public void unregisterCommandFramework(@NonNull CommandFramework commandFramework) {
        unregisterCommandFramework0(commandFramework);
        commandFrameworks.remove(commandFramework);
    }

    /**
     * 注销所有命令框架
     */
    public void unregisterAllCommandFrameworks() {
        commandFrameworks.forEach(this::unregisterCommandFramework0);
        commandFrameworks.clear();
    }

    /**
     * 检查未拥有权限
     * 自底向上检查，任意节点拥有权限即可
     *
     * @param sender      发送者
     * @param commandTree 命令数
     */
    private boolean checkPermission(CommandSender sender, CommandTree commandTree) {
        CommandTree currentTree = commandTree;
        boolean needPermission = false;

        while (currentTree != null) {
            CommandElement element = currentTree.getElement();

            if (element == null) {
                break;
            }

            String permission = element.getPermission();

            if (!permission.isEmpty()) {
                needPermission = true;

                if (sender.hasPermission(permission)) {
                    return true;
                }
            }

            currentTree = currentTree.getParent();
        }

        return !needPermission;
    }

    /**
     * 获取节点权限（可能为多个）
     *
     * @return 以自底向上的顺序返回可用的所有权限
     */
    private List<String> getAvailablePermissions(CommandTree commandTree) {
        List<String> permissions = new ArrayList<>();
        CommandTree currentTree = commandTree;

        while (currentTree != null) {
            CommandElement element = currentTree.getElement();

            if (element == null) {
                break;
            }

            String permission = element.getPermission();

            if (!permission.isEmpty()) {
                permissions.add(permission);
            }

            currentTree = currentTree.getParent();
        }

        return permissions;
    }

    /**
     * 获取最接近的命令树
     *
     * @param commandLineArray 命令行数组
     */
    private CommandTree getClosestCommandTree(@NonNull String[] commandLineArray) {
        CommandTree currentTree = this.rootCommandTree;

        for (String commandIdAndParam : commandLineArray) {
            boolean found = false;

            for (CommandTree child : currentTree.getChildren()) {
                CommandElement component = child.getElement();

                if (component.getCommandId().equalsIgnoreCase(commandIdAndParam)) {
                    currentTree = child;
                    found = true;
                }
            }

            if (!found) {
                break;
            }
        }

        return currentTree;
    }

    /**
     * 调度执行命令
     */
    private void dispatchCommand(@NonNull CommandSender sender, @NonNull String[] cliArray) {
        SenderType senderType = SenderType.of(sender);
        int cliLength = cliArray.length;
        CommandTree closestTree = getClosestCommandTree(cliArray);
        CommandElement closestElement = closestTree.getElement();

        CommandGroupContext commandGroup = closestElement.getHolder();
        CommandFramework commandFramework = commandGroup.getCommandFramework();
        CommandHelper helper = commandFramework.getCommandHelper();
        CommandFailureHandler failureHandler = commandFramework.getCommandExecutionFailureHandler();

        if (helper == null) {
            throw CommandExecutionException.newException(cliArray, commandGroup, "missing CommandHelper: " + commandFramework.getClass());
        }

        if (failureHandler == null) {
            throw CommandExecutionException.newException(cliArray, commandGroup, "missing CommandExecutionFailureHandler: " + commandFramework.getClass());
        }

        int firstParamIndex = closestTree.getLevel();

        // 帮助命令
        if (firstParamIndex < cliLength && cliArray[firstParamIndex].equals("?")) {
            helper.onHelp(sender, closestTree, cliArray);
            return;
        }

        // 检查权限
        if (!checkPermission(sender, closestTree)) {
            failureHandler.onMissingPermission(sender, closestTree, cliArray, getAvailablePermissions(closestTree).toArray(new String[]{}));
            return;
        }

        // 最接近的树只匹配到了 CommandMapping 层面
        if (closestElement instanceof CommandMappingElement) {
            failureHandler.onCommandFormatError(sender, closestTree, cliArray);
        } else if (closestElement instanceof CommandBodyElement) {             // 最接近的树匹配到了 CommandBody 层面
            CommandBodyElement bodyElement = (CommandBodyElement) closestElement;
            String[] params = new String[cliLength - firstParamIndex]; // 去除所有 CommandMapping + CommandBody.Id 后剩下的参数
            int paramsLength = params.length;

            System.arraycopy(cliArray, firstParamIndex, params, 0, params.length);

            // 输入的参数在 [min, max] 之间
            if (paramsLength >= bodyElement.getMinInputParamCount() && paramsLength <= bodyElement.getMaxInputParamCount()) {
                List<Object> methodParams = new ArrayList<>(); // 经过解析后的方法参数

                // 处理上下文参数
                for (SenderParam senderParam : bodyElement.getSenderParams()) {
                    if (senderParam instanceof SenderSenderParam) {
                        boolean isMatched = false;

                        for (SenderType senderType1 : bodyElement.getSenderTypes()) {
                            if (senderType == senderType1) {
                                isMatched = true;
                                break;
                            }
                        }

                        if (!isMatched) {
                            failureHandler.onCommandSenderMismatch(sender, closestTree, cliArray);
                            return;
                        }

                        methodParams.add(sender);
                    } else {
                        throw new UnsupportedOperationException("unsupported ContextParam: " + senderParam.getClass());
                    }
                }

                int textParamsIndex = 0;

                logger.debug(bodyElement.getCommandParams().toString());

                // 处理命令参数
                for (CommandParam commandParam : bodyElement.getCommandParams()) {
                    List<String> textParams = new ArrayList<>(); // 待解析的参数

                    // 固定的
                    if (commandParam instanceof FixedCommandParam) {
                        textParams.add(params[textParamsIndex]);
                    } else if (commandParam instanceof OptionalCommandParam) { // 可选的
                        if (textParamsIndex >= paramsLength) {
                            textParams.add(null);
                        } else {
                            textParams.add(params[textParamsIndex]);
                        }
                    } else if (commandParam instanceof ArrayCommandParam) { // 数组的
                        textParams.addAll(Arrays.asList(params).subList(textParamsIndex, paramsLength)); // 添加当前索引后的所有参数
                    } else {
                        throw new UnsupportedOperationException("unsupported UserInputMethodParam: " + commandParam.getClass());
                    }

                    textParamsIndex++;

                    Class<?> paramType = commandParam.getType();

                    // 处理数组类型
                    if (commandParam instanceof ArrayCommandParam) {
                        paramType = ((ArrayCommandParam) commandParam).getActualType();
                    }

                    List<Object> parsed = new ArrayList<>();

                    // 解析参数
                    for (String textParam : textParams) {
                        if (textParam == null) {
                            methodParams.add(null);
                            continue;
                        }


                        ParamParser paramParser = commandFramework.getParamParser(paramType);

                        if (paramParser == null) {
                            throw CommandExecutionException.newException(cliArray, commandGroup, String.format("can not found ParamParser for type '%s'", paramType.getName()));
                        }

                        // 参数解析
                        Response response = paramParser.parse(sender, paramType, textParam);

                        if (response == null) {
                            throw CommandExecutionException.newException(cliArray, commandGroup, "missing response");
                        }

                        Object object = response.getObject();
                        String errorMessage = response.getErrorMessage();

                        if (object == null) {
                            if (errorMessage == null) {
                                throw CommandExecutionException.newException(cliArray, commandGroup, "missing errorMessage");
                            } else {
                                failureHandler.onCommandParamParseError(sender,
                                        rootCommandTree,
                                        cliArray,
                                        textParamsIndex + firstParamIndex - 1,
                                        Texts.setPlaceholders(errorMessage, new PlaceholderContainer().put("param", textParam)));
                                return;
                            }
                        } else {
                            parsed.add(object);
                        }
                    }

                    // 如果只有一个则是普通类型，反之则是数组类型
                    if (parsed.size() == 1) {
                        methodParams.add(parsed.get(0));
                    } else {
                        Object array = Array.newInstance(paramType, parsed.size());

                        for (int i = 0; i < parsed.size(); i++) {
                            Array.set(array, i, parsed.get(i));
                        }

                        methodParams.add(array);
                    }
                }

                // 执行方法
                try {
                    logger.debug(String.format("Method params: %s", methodParams));
                    bodyElement.invokeMethod(methodParams.toArray());
                } catch (Exception e) {
                    throw CommandExecutionException.newException(cliArray, commandGroup, "method error", e);
                }
            } else {
                failureHandler.onCommandFormatError(sender, closestTree, cliArray);
            }
        } else {
            throw new UnsupportedOperationException("unsupported closestElement: " + closestElement.getClass().getName());
        }
    }

    /**
     * 补全 Tab
     *
     * @param sender           发送者
     * @param commandLineArray 命令行数组
     */
    public List<String> completeTab(@NonNull CommandSender sender, @NonNull String[] commandLineArray) {
        int len = commandLineArray.length;
        String lastParam = commandLineArray[len - 1];
        CommandTree closestTree = getClosestCommandTree(commandLineArray);
        CommandElement closedElement = closestTree.getElement();
        CommandGroupContext closestHolder = closedElement.getHolder();
        List<String> availableCompletions = new ArrayList<>();

        if (closedElement instanceof CommandBodyElement) {
            CommandBodyElement commandBodyElement = (CommandBodyElement) closedElement;
            int inputLen = len - closestTree.getLevel(); // 已输入的参数数量

            if (inputLen > 0 && inputLen <= commandBodyElement.getMaxInputParamCount()) {
                List<CommandParam> commandParams = commandBodyElement.getCommandParams();
                int paramIndex = inputLen - 1;
                CommandParam commandParam = commandParams.get(paramIndex);
                Class<?> paramType = commandParam.getType();
                ParamTabCompleter tabCompleter = closestHolder.getCommandFramework().getParamTabCompleter(paramType);

                if (tabCompleter != null) {
                    availableCompletions = tabCompleter.complete(sender, paramType);
                } else {
                    // 没有补全器
                    availableCompletions = Bukkit.getOnlinePlayers()
                            .stream()
                            .map(HumanEntity::getName)
                            .collect(Collectors.toList());
                }
            }
        } else if (closedElement instanceof CommandMappingElement) {
            availableCompletions = closestTree
                    .getChildren()
                    .stream()
                    .map(CommandTree::getElement)
                    .map(CommandElement::getCommandId)
                    .collect(Collectors.toList());
        }

        // 末尾的参数进行前缀过滤
        return availableCompletions
                .stream()
                .filter(s -> s.toLowerCase().startsWith(lastParam.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 将命令数组转换为命令行
     *
     * @param commandLineArray 命令数组
     */
    private String generateCommandLine(String[] commandLineArray) {
        return String.join(" ", commandLineArray);
    }

    public CommandFramework createCommandFramework(@NonNull Plugin plugin) {
        CommandFramework commandFramework = new CommandFramework(legacyVoidFrameworkPlugin, plugin);

        commandFrameworks.add(commandFramework);
        return commandFramework;
    }

    /**
     * 创建命令框架
     *
     * @param plugin         持有者
     * @param localeResource 本土化资源
     */
    @Deprecated
    public CommandFramework createCommandFramework(@NonNull Plugin plugin, @Nullable LocaleResource localeResource) {
        CommandFramework commandFramework = new CommandFramework(legacyVoidFrameworkPlugin, plugin);

        commandFrameworks.add(commandFramework);
        return commandFramework;
    }

    /**
     * 获取所有命令框架
     */
    public Set<CommandFramework> getCommandFrameworks() {
        return Collections.unmodifiableSet(commandFrameworks);
    }

    /**
     * 注销 Bukkit Id
     * 主要用于集中管理
     */
    public void unregisterBukkitCommandId(@NonNull String id) {
        registeredBukkitCommandIds.remove(id);
    }

    /**
     * 是否已经注册 Bukkit Id
     * 主要用于集中管理
     */
    boolean isRegisteredBukkitCommandId(@NonNull String id) {
        return registeredBukkitCommandIds.contains(id);
    }

    /**
     * 注册 Bukkit ID
     * 主要用于集中管理
     */
    void registerBukkitCommandId(@NonNull String id) {
        if (isRegisteredBukkitCommandId(id)) {
            throw new IllegalArgumentException(String.format("id %s already registered", id));
        }

        if (BukkitCommandUtil.existsCommand(id)) {
            throw new IllegalArgumentException(String.format("command '%s' already exists in bukkit command map", id));
        }

        registeredBukkitCommandIds.add(id);
        // 接管 Bukkit 命令
        BukkitCommandUtil.registerCommand(id, new BukkitCommand(id) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                dispatchCommand(sender, getFullCommandLineArray(id, args));
                return true;
            }

            @Override
            public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                return completeTab(sender, getFullCommandLineArray(id, args));
            }
        });
    }

    /**
     * 获取完整的命令行数组（无 /, 带 Bukkit Id 的）
     */
    private String[] getFullCommandLineArray(String bukkitId, String[] args) {
        String[] cmdLineArray = new String[args.length + 1];

        cmdLineArray[0] = bukkitId;
        System.arraycopy(args, 0, cmdLineArray, 1, args.length);
        return cmdLineArray;
    }

    /**
     * 修正 Bukkit 命令 Id
     * 注销未使用的 Bukkit 命令 Id
     * 注册要使用的 Bukkit 命令 Id
     */
    public void adjustBukkitCommandIds() {
        Iterator<String> iterator = registeredBukkitCommandIds.iterator();

        // 注销无效的 Bukkit Command Id
        while (iterator.hasNext()) {
            String id = iterator.next();
            boolean valid = false;

            for (CommandTree child : rootCommandTree.getChildren()) {
                if (child.getElement().getCommandId().equalsIgnoreCase(id)) {
                    valid = true;
                    break;
                }
            }

            if (valid) {
                continue;
            }

            iterator.remove();
            BukkitCommandUtil.unregisterCommand(id);
        }

        // 注册已使用的 Bukkit 命令 Id
        for (CommandTree child : rootCommandTree.getChildren()) {
            String commandId = child.getElement().getCommandId();

            if (!isRegisteredBukkitCommandId(commandId)) {
                registerBukkitCommandId(commandId);
            }
        }
    }
}
