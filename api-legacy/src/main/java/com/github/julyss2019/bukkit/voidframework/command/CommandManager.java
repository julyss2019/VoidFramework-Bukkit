package com.github.julyss2019.bukkit.voidframework.command;

import com.github.julyss2019.bukkit.voidframework.annotation.Nullable;
import com.github.julyss2019.bukkit.voidframework.command.exception.CommandExecutionException;
import com.github.julyss2019.bukkit.voidframework.command.exception.CommandParseException;
import com.github.julyss2019.bukkit.voidframework.command.failure.CommandFailureHandler;
import com.github.julyss2019.bukkit.voidframework.command.helper.CommandHelper;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.command.ActiveCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.command.ArrayCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.command.OptionalCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.command.SingleCommandParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.ActiveContextParam;
import com.github.julyss2019.bukkit.voidframework.command.internal.param.context.SenderParam;
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
import java.util.function.Consumer;
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
            CommandElement element = currentTree.getCommandElement();

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
            CommandElement element = currentTree.getCommandElement();

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
                CommandElement component = child.getCommandElement();

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
    private void dispatchCommand(@NonNull CommandSender sender, @NonNull String[] cli) {
        int cliLength = cli.length;
        CommandTree closestCommandTree = getClosestCommandTree(cli);
        CommandElement closestElement = closestCommandTree.getCommandElement();
        CommandGroupContext commandGroup = closestElement.getActiveCommandGroup();
        CommandFramework commandFramework = commandGroup.getCommandFramework();
        CommandHelper commandHelper = commandFramework.getCommandHelper();
        CommandFailureHandler commandFailureHandler = commandFramework.getFailureHandler();

        if (commandHelper == null) {
            throw new IllegalStateException("Missing CommandHelper: " + commandFramework.getClass());
        }

        if (commandFailureHandler == null) {
            throw new IllegalStateException("Missing CommandExecutionFailureHandler: " + commandFramework.getClass());
        }

        int closestCommandTreeLevel = closestCommandTree.getLevel();

        // 非根 Mapping 则显示帮助命令
        if (closestCommandTreeLevel < cliLength && cli[closestCommandTreeLevel].equals("?")) {
            commandHelper.onHelp(sender, closestCommandTree, cli);
            return;
        }

        // 检查权限
        if (!checkPermission(sender, closestCommandTree)) {
            commandFailureHandler.onMissingPermission(sender,
                    closestCommandTree,
                    cli,
                    getAvailablePermissions(closestCommandTree).toArray(new String[]{}));
            return;
        }

        // 最接近的树只匹配到了 CommandMapping 层面
        if (closestElement instanceof CommandMappingElement) {
            commandFailureHandler.onCommandFormatError(sender, closestCommandTree, cli);
        } else if (closestElement instanceof CommandBodyElement) { // 最接近的树匹配到了 CommandBody 层面
            CommandBodyElement bodyElement = (CommandBodyElement) closestElement;
            String[] textParams = new String[cliLength - closestCommandTreeLevel]; // 去除所有 CommandMapping + CommandBody.Id 后剩下的参数
            int textParamLength = textParams.length;

            System.arraycopy(cli, closestCommandTreeLevel, textParams, 0, textParams.length);

            // 输入的参数在 [min, max] 之间
            if (textParamLength >= bodyElement.getMinInputParamCount() && textParamLength <= bodyElement.getMaxInputParamCount()) {
                List<Object> methodParams = new ArrayList<>(); // 经过解析后的方法参数

                if (!parseCommandContexts(sender, cli, closestCommandTree, methodParams)) {
                    return;
                }

                // 用户输入了无法解析的参数
                if (!parseCommandParams(sender, cli, closestCommandTree, textParams, methodParams)) {
                    return;
                }

                // 执行方法
                logger.debug(String.format("Method params: %s", methodParams));
                bodyElement.invokeMethod(methodParams.toArray());
            } else {
                commandFailureHandler.onCommandFormatError(sender, closestCommandTree, cli);
            }
        } else {
            throw new UnsupportedOperationException("Unsupported element: " + closestElement.getClass().getName());
        }
    }

    private boolean parseCommandContexts(CommandSender sender, String[] cli, CommandTree commandTree, List<Object> methodParams) {
        SenderType senderType = SenderType.of(sender);
        CommandBodyElement commandBodyElement = (CommandBodyElement) commandTree.getCommandElement();
        CommandGroupContext commandGroupContext = commandBodyElement.getActiveCommandGroup();
        CommandFramework commandFramework = commandGroupContext.getCommandFramework();
        CommandFailureHandler failureHandler = commandFramework.getFailureHandler();

        // 处理上下文参数
        for (ActiveContextParam contextParam : commandBodyElement.getContextParams()) {
            if (contextParam instanceof SenderParam) {
                boolean isMatched = false;

                for (SenderType senderType1 : commandBodyElement.getSenderTypes()) {
                    if (senderType == senderType1) {
                        isMatched = true;
                        break;
                    }
                }

                if (!isMatched) {
                    failureHandler.onCommandSenderMismatch(sender, commandTree, cli);
                    return false;
                }

                methodParams.add(sender);
            } else {
                throw new UnsupportedOperationException("Unsupported ContextParam: " + contextParam.getClass());
            }
        }

        return true;
    }

    private boolean parseCommandParams(CommandSender sender,
                                       String[] cli,
                                       CommandTree commandTree,
                                       String[] textParams,
                                       List<Object> methodParams) {
        CommandBodyElement commandBodyElement = (CommandBodyElement) commandTree.getCommandElement();
        CommandGroupContext commandGroupContext = commandBodyElement.getActiveCommandGroup();
        CommandFramework commandFramework = commandGroupContext.getCommandFramework();
        List<ActiveCommandParam> commandParams = commandBodyElement.getCommandParams();
        int textParamLength = textParams.length;

        // 处理命令参数
        for (int commandParamIndex = 0; commandParamIndex < commandParams.size(); commandParamIndex++) {
            ActiveCommandParam commandParam = commandParams.get(commandParamIndex);
            List<String> commandParamTextParams = new ArrayList<>(); // 本次遍历的命令参数对应的文本参数

            // 单个命令参数
            if (commandParam instanceof SingleCommandParam) {
                commandParamTextParams.add(textParams[commandParamIndex]);
            } else if (commandParam instanceof OptionalCommandParam) { // 可选命令参数
                if (commandParamIndex >= textParamLength) {
                    commandParamTextParams.add(null);
                } else {
                    commandParamTextParams.add(textParams[commandParamIndex]);
                }
            } else if (commandParam instanceof ArrayCommandParam) { // 数组命令参数
                commandParamTextParams.addAll(Arrays.asList(textParams).subList(commandParamIndex, textParamLength)); // 添加当前索引后的所有参数
            } else {
                throw new UnsupportedOperationException("Unsupported CommandParam: " + commandParam.getClass());
            }

            Class<?> actualCommandParamType = commandParam.getType();

            // 处理数组类型
            if (commandParam instanceof ArrayCommandParam) {
                actualCommandParamType = ((ArrayCommandParam) commandParam).getActualType();
            }

            List<Object> commandParamValues = new ArrayList<>(); // 命令参数解析出来的对象

            // 解析原始参数
            for (String commandParamTextParam : commandParamTextParams) {
                int finalCommandParamIndex = commandParamIndex;
                Consumer<String> userParseErrorConsumer = errorMessage -> commandFramework.getFailureHandler().onCommandParamParseError(sender,
                        rootCommandTree,
                        cli,
                        commandTree.getLevel() - finalCommandParamIndex - 1,
                        Texts.setPlaceholders(errorMessage, new PlaceholderContainer().put("commandParamTextParam", commandParamTextParam)));

                // 用户输入了无法解析的参数
                if (!parseCommandParam(sender,
                        commandFramework,
                        commandParamTextParam,
                        actualCommandParamType,
                        commandParamValues,
                        userParseErrorConsumer)) {
                    return false;
                }
            }

            // 数组类型特殊处理
            if (commandParam instanceof ArrayCommandParam) {
                // 包装为数组
                Object array = Array.newInstance(actualCommandParamType, commandParamValues.size());

                for (int i = 0; i < commandParamValues.size(); i++) {
                    Array.set(array, i, commandParamValues.get(i));
                }

                methodParams.add(array);
            } else {
                methodParams.add(commandParamValues.get(0));
            }
        }

        return true;
    }

    private boolean parseCommandParam(CommandSender sender,
                                      CommandFramework commandFramework,
                                      String textParam,
                                      Class<?> type,
                                      List<Object> commandParamValues,
                                      Consumer<String> userParseErrorConsumer) {
        if (textParam == null) {
            commandParamValues.add(null);
            return true;
        }

        ParamParser paramParser = commandFramework.getParamParser(type);

        if (paramParser == null) {
            throw new CommandParseException(String.format("Unable to find ParamParser for: '%s'", type.getName()));
        }

        // 参数解析
        Response response = paramParser.parse(sender, type, textParam);

        if (response == null) {
            throw new CommandParseException(String.format(String.format("ParamParser '%s' missing response", paramParser)));
        }

        Object object = response.getObject();
        String errorMessage = response.getErrorMessage();

        if (object == null) {
            if (errorMessage == null) {
                throw new CommandParseException(String.format("ParamParser '%s' missing error message", paramParser));
            } else {
                userParseErrorConsumer.accept(errorMessage);
                return false;
            }
        }

        commandParamValues.add(object);
        return true;
    }

    /**
     * 补全 Tab
     *
     * @param sender           发送者
     * @param cli 命令行数组
     */
    public List<String> completeTab(@NonNull CommandSender sender, @NonNull String[] cli) {
        int cliLength = cli.length;
        CommandTree closestCommandTree = getClosestCommandTree(cli);
        CommandElement commandElement = closestCommandTree.getCommandElement();
        CommandGroupContext activeCommandGroup = commandElement.getActiveCommandGroup();
        List<String> availableCompletions = new ArrayList<>();

        if (commandElement instanceof CommandBodyElement) {
            CommandBodyElement commandBodyElement = (CommandBodyElement) commandElement;
            int textParamCount = cliLength - closestCommandTree.getLevel(); // 已输入的参数数量

            // 如果 textParamCount 小于等于 0 则还是在 CommandBodyElement 的 Command ID 部分
            if (textParamCount > 0 && textParamCount <= commandBodyElement.getMaxInputParamCount()) {
                List<ActiveCommandParam> commandParams = commandBodyElement.getCommandParams();
                // 考虑数组类型的参数
                int commandParamIndex = Math.min(textParamCount - 1, commandParams.size() - 1);
                ActiveCommandParam commandParam = commandParams.get(commandParamIndex);
                Class<?> paramType = commandParam.getType();
                ParamTabCompleter tabCompleter = activeCommandGroup.getCommandFramework().getParamTabCompleter(paramType);

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
        } else if (commandElement instanceof CommandMappingElement) {
            availableCompletions = closestCommandTree
                    .getChildren()
                    .stream()
                    .map(CommandTree::getCommandElement)
                    .map(CommandElement::getCommandId)
                    .collect(Collectors.toList());
        }

        String lastParam = cli[cliLength - 1];

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
                try {
                    dispatchCommand(sender, getFullCommandLineArray(id, args));
                } catch (Throwable e) {
                    throw CommandExecutionException.newException(String.format("/%s %s", id, String.join(" ", args)), e);
                }

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
                if (child.getCommandElement().getCommandId().equalsIgnoreCase(id)) {
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
            String commandId = child.getCommandElement().getCommandId();

            if (!isRegisteredBukkitCommandId(commandId)) {
                registerBukkitCommandId(commandId);
            }
        }
    }
}
