package com.github.julyss2019.bukkit.voidframework.command;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.command.failure.CommandFailureHandler;
import com.github.julyss2019.bukkit.voidframework.command.helper.CommandHelper;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.ContextMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.SenderContextMethodParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.ArrayParamDescription;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.FixedParamDescription;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.OptionalParamDescription;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.user.ParamDescription;
import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import com.github.julyss2019.bukkit.voidframework.command.tree.RootCommandTree;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandBodyElement;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandElement;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandMappingElement;
import com.github.julyss2019.bukkit.voidframework.command.internal.util.BukkitCommandUtil;
import com.github.julyss2019.bukkit.voidframework.command.param.parser.ParamParser;
import com.github.julyss2019.bukkit.voidframework.command.param.parser.Response;
import com.github.julyss2019.bukkit.voidframework.command.param.tab.completer.ParamTabCompleter;
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.locale.resource.LocaleResource;
import com.github.julyss2019.bukkit.voidframework.logging.logger.Logger;
import com.github.julyss2019.bukkit.voidframework.text.PlaceholderContainer;
import com.github.julyss2019.bukkit.voidframework.text.Texts;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 命令总控
 */
public class CommandManager {
    private final LegacyVoidFrameworkPlugin legacyVoidFrameworkPlugin;
    private final Logger logger;
    private final CommandTree rootCommandTree = new RootCommandTree();
    private final Set<String> registeredBukkitCommandIds = new HashSet<>();
    private final Set<CommandFramework> commandFrameworks = new HashSet<>();

    public CommandManager(@NonNull LegacyVoidFrameworkPlugin plugin) {
        this.legacyVoidFrameworkPlugin = plugin;
        this.logger = plugin.getPluginLogger();
    }

    /**
     * 获取顶级命令树
     */
    public CommandTree getRootCommandTree() {
        return rootCommandTree;
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
    private void dispatchCommand(@NonNull CommandSender sender, @NonNull String[] commandLineArray) {
        String commandLineStr = generateCommandLine(commandLineArray);
        logger.debug("dispatch command '%s' for '%s'", commandLineStr, sender.toString());

        SenderType senderType = SenderType.of(sender);
        int commandLineArrayLength = commandLineArray.length;
        CommandTree closestTree = getClosestCommandTree(commandLineArray);
        CommandElement element = closestTree.getElement();

        CommandGroupContext holder = element.getHolder();
        CommandFramework commandFramework = holder.getCommandFramework();
        CommandHelper helper = commandFramework.getCommandHelper();
        CommandFailureHandler failureHandler = commandFramework.getCommandExecutionFailureHandler();

        if (helper == null) {
            throw CommandExecutionException.newException(commandLineArray, holder, "missing CommandHelper: " + commandFramework.getClass());
        }

        if (failureHandler == null) {
            throw CommandExecutionException.newException(commandLineArray, holder, "missing CommandExecutionFailureHandler: " + commandFramework.getClass());
        }

        int afterInputIndex = closestTree.getLevel();

        // 帮助命令
        if (afterInputIndex < commandLineArrayLength && commandLineArray[afterInputIndex].equals("?")) {
            // TODO 暂时不做分页
            int page = 0;
            int pageParamIndex = afterInputIndex + 1;

            if (pageParamIndex < commandLineArrayLength) {
                try {
                    page = Integer.parseInt(commandLineArray[pageParamIndex]);
                } catch (Exception ignored) {
                }
            }

            helper.onHelp(sender, closestTree, commandLineArray);
            return;
        }

        if (!checkPermission(sender, closestTree)) {
            failureHandler.onMissingPermission(sender, closestTree, commandLineArray, getAvailablePermissions(closestTree).toArray(new String[]{}));
            return;
        }

        if (element instanceof CommandMappingElement) {
            // 最接近的树只匹配到了 CommandMapping 层面
            failureHandler.onCommandFormatError(sender, closestTree, commandLineArray);
        } else if (element instanceof CommandBodyElement) {
            // 最接近的树匹配到了 CommandBody 层面
            CommandBodyElement bodyElement = (CommandBodyElement) element;
            String[] inputCommandParams = new String[commandLineArrayLength - afterInputIndex]; // 去除所有 CommandMapping + CommandBody.Id 后剩下的参数
            int userInputParamsLength = inputCommandParams.length;

            System.arraycopy(commandLineArray, afterInputIndex, inputCommandParams, 0, inputCommandParams.length);
            logger.debug("execute by CommandGroup " + holder.getCommandGroup() + "(" + bodyElement.getMethod() + ")");

            // 输入的参数在 [min, max] 之间
            if (userInputParamsLength >= bodyElement.getMinInputParamCount() && userInputParamsLength <= bodyElement.getMaxInputParamCount()) {
                List<Object> allParamValues = new ArrayList<>(); // 经过解析后的方法值
                int userInputParamsIndex = 0;

                // 处理上下文参数
                for (ContextMethodParam contextMethodParam : bodyElement.getContextMethodParams()) {
                    if (contextMethodParam instanceof SenderContextMethodParam) {
                        boolean matched = false;

                        for (SenderType senderType1 : bodyElement.getSenderTypes()) {
                            if (senderType == senderType1) {
                                matched = true;
                                break;
                            }
                        }

                        if (!matched) {
                            failureHandler.onCommandSenderMismatch(sender, closestTree, commandLineArray);
                            return;
                        }

                        allParamValues.add(sender);
                    } else {
                        throw new UnsupportedOperationException("unsupported ContextParam: " + contextMethodParam.getClass());
                    }
                }

                List<ParamDescription> paramDescriptions = bodyElement.getParamDescriptions();

                for (ParamDescription paramDescription : paramDescriptions) {
                    List<String> userInputParamsParseTmp = new ArrayList<>(); // 待解析的参数

                    // 固定的
                    if (paramDescription instanceof FixedParamDescription) {
                        userInputParamsParseTmp.add(inputCommandParams[userInputParamsIndex]);
                    } else if (paramDescription instanceof OptionalParamDescription) { // 可选的
                        if (userInputParamsIndex >= userInputParamsLength) {
                            userInputParamsParseTmp.add(null);
                        } else {
                            userInputParamsParseTmp.add(inputCommandParams[userInputParamsIndex]);
                        }
                    } else if (paramDescription instanceof ArrayParamDescription) { // 数组的
                        userInputParamsParseTmp.addAll(Arrays.asList(inputCommandParams).subList(userInputParamsIndex, userInputParamsLength)); // 添加当前索引后的所有参数
                    } else {
                        throw new UnsupportedOperationException("unsupported UserInputMethodParam: " + paramDescription.getClass());
                    }

                    userInputParamsIndex++;

                    // 解析参数
                    for (String userInputParam : userInputParamsParseTmp) {
                        if (userInputParam == null) {
                            allParamValues.add(null);
                            continue;
                        }

                        Class<?> paramType = paramDescription.getType();

                        // 处理数组类型
                        if (paramDescription instanceof ArrayParamDescription) {
                            paramType = ((ArrayParamDescription) paramDescription).getActualType();
                        }

                        ParamParser paramParser = commandFramework.getParamParser(paramType);

                        if (paramParser == null) {
                            throw CommandExecutionException.newException(commandLineArray,
                                    holder,
                                    String.format("can not found ParamParser for type '%s'", paramType.getName()));
                        }

                        // 参数解析
                        Response response = paramParser.parse(sender, paramType, userInputParam);

                        if (response == null) {
                            throw CommandExecutionException.newException(commandLineArray, holder, "missing response");
                        }

                        Object object = response.getObject();
                        String errorMessage = response.getErrorMessage();

                        if (object == null) {
                            if (errorMessage == null) {
                                throw CommandExecutionException.newException(commandLineArray, holder, "missing errorMessage");
                            } else {
                                failureHandler.onCommandParamParseError(sender,
                                        rootCommandTree,
                                        commandLineArray,
                                        userInputParamsIndex + afterInputIndex - 1,
                                        Texts.setPlaceholders(errorMessage, new PlaceholderContainer().put("param", userInputParam)));
                                return;
                            }
                        } else {
                            allParamValues.add(object);
                        }
                    }
                }

                // 执行方法
                try {
                    logger.debug(String.format("method params: %s", allParamValues));
                    bodyElement.invokeMethod(allParamValues.toArray());
                } catch (Exception e) {
                    throw CommandExecutionException.newException(commandLineArray, holder, "method error", e);
                }

                logger.debug("method invoked");
            } else {
                failureHandler.onCommandFormatError(sender, closestTree, commandLineArray);
            }
        } else {
            throw new UnsupportedOperationException("unsupported element: " + element.getClass().getName());
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
                List<ParamDescription> paramDescriptions = commandBodyElement.getParamDescriptions();
                int paramIndex = inputLen - 1;
                ParamDescription paramDescription = paramDescriptions.get(paramIndex);
                Class<?> paramType = paramDescription.getType();
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
