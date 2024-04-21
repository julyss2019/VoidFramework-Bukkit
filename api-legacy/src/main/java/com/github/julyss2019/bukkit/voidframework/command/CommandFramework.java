package com.github.julyss2019.bukkit.voidframework.command;

import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandBody;
import com.github.julyss2019.bukkit.voidframework.command.annotation.CommandMapping;
import com.github.julyss2019.bukkit.voidframework.command.failure.CommandFailureHandler;
import com.github.julyss2019.bukkit.voidframework.command.failure.CommandFailureHandlerImpl;
import com.github.julyss2019.bukkit.voidframework.command.helper.CommandHelper;
import com.github.julyss2019.bukkit.voidframework.command.helper.CommandHelperImpl;
import com.github.julyss2019.bukkit.voidframework.command.tree.CommandTree;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandBodyElement;
import com.github.julyss2019.bukkit.voidframework.command.tree.element.CommandMappingElement;
import com.github.julyss2019.bukkit.voidframework.command.param.parser.*;
import com.github.julyss2019.bukkit.voidframework.command.param.tab.completer.EnumParamTabCompleter;
import com.github.julyss2019.bukkit.voidframework.command.param.tab.completer.IntegerParamTabCompleter;
import com.github.julyss2019.bukkit.voidframework.command.param.tab.completer.ParamTabCompleter;
import com.github.julyss2019.bukkit.voidframework.command.param.tab.completer.PlayerParamTabCompleter;
import com.github.julyss2019.bukkit.voidframework.internal.LegacyVoidFrameworkPlugin;
import com.github.julyss2019.bukkit.voidframework.internal.logger.LegacyPluginLogger;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.*;

public class CommandFramework {
    private final LegacyVoidFrameworkPlugin legacyVoidFrameworkPlugin;
    private final Plugin plugin;
    private final LegacyPluginLogger logger;
    private final CommandManager commandManager;
    private final CommandMapping pluginScopeCommandMapping; // 插件层面的命令映射注解
    private final Set<CommandGroupContext> commandGroupContexts = new HashSet<>();
    private final List<ParamParser> paramParsers = new ArrayList<>();
    private final List<ParamTabCompleter> paramTabCompleters = new ArrayList<>();
    private CommandFailureHandler commandFailureHandler;
    private CommandHelper commandHelper;

    public CommandFramework(@NonNull LegacyVoidFrameworkPlugin legacyVoidFrameworkPlugin, @NonNull Plugin plugin) {
        this.legacyVoidFrameworkPlugin = legacyVoidFrameworkPlugin;
        this.plugin = plugin;
        this.commandManager = legacyVoidFrameworkPlugin.getCommandManager();
        this.logger = legacyVoidFrameworkPlugin.getPluginLogger();
        this.pluginScopeCommandMapping = plugin.getClass().getAnnotation(CommandMapping.class);

        addBuildInParamParsers();
        addBuildInParamTabCompleters();

        setCommandExecutionFailureHandler(new CommandFailureHandlerImpl(legacyVoidFrameworkPlugin.getLocaleResource()));
        setCommandHelper(new CommandHelperImpl(legacyVoidFrameworkPlugin.getLocaleResource()));
    }

    public void setCommandExecutionFailureHandler(@NonNull CommandFailureHandler commandFailureHandler) {
        this.commandFailureHandler = commandFailureHandler;
    }

    public void setCommandHelper(@NonNull CommandHelper commandHelper) {
        this.commandHelper = commandHelper;
    }

    public ParamTabCompleter getParamTabCompleter(@NonNull Class<?> type) {
        for (ParamTabCompleter paramTabCompleter : paramTabCompleters) {
            for (Class<?> supportedParamType : paramTabCompleter.getSupportedParamTypes()) {
                if (supportedParamType.isAssignableFrom(type)) {
                    return paramTabCompleter;
                }
            }
        }

        return null;
    }

    public ParamParser getParamParser(@NonNull Class<?> type) {
        for (ParamParser paramParser : paramParsers) {
            for (Class<?> supportedParamType : paramParser.getSupportedParamTypes()) {
                if (supportedParamType.isAssignableFrom(type)) {
                    return paramParser;
                }
            }
        }

        return null;
    }

    public void addParamParser(@NonNull ParamParser paramParser) {
        paramParsers.add(paramParser);
    }

    private void addBuildInParamParsers() {
        addParamParser(new EnumParamParser());
        addParamParser(new IntegerParamParser());
        addParamParser(new DoubleParamParser());
        addParamParser(new PlayerParamParser());
        addParamParser(new StringParamParser());
        addParamParser(new BooleanParamParser());
        addParamParser(new LongParamParser());
        addParamParser(new OfflinePlayerParamParser());
    }

    public void addParamTabCompleter(@NonNull ParamTabCompleter paramTabCompleter) {
        paramTabCompleters.add(paramTabCompleter);
    }

    public void addBuildInParamTabCompleters() {
        addParamTabCompleter(new EnumParamTabCompleter());
        addParamTabCompleter(new IntegerParamTabCompleter());
        addParamTabCompleter(new PlayerParamTabCompleter());
    }

    /**
     * 获取所有命令组
     */
    public Set<CommandGroupContext> getCommandGroupContexts() {
        return Collections.unmodifiableSet(commandGroupContexts);
    }

    private void unregisterCommandGroup0(CommandGroupContext commandGroupContext) {
        logger.info("CommandGroup unregistered: " + commandGroupContext.getCommandGroup());
        commandManager.getRootCommandTree().getChildren().removeIf(child -> child.getElement().getHolder().equals(commandGroupContext));
        commandManager.adjustBukkitCommandIds();
    }

    /**
     * 注销命令组
     */
    public void unregisterCommandGroup(@NonNull CommandGroupContext commandGroupContext) {
        unregisterCommandGroup0(commandGroupContext);
        commandGroupContexts.remove(commandGroupContext);
        commandManager.adjustBukkitCommandIds();
    }

    /**
     * 注销所有命令组
     */
    public void unregisterCommandGroups() {
        for (CommandGroupContext commandGroup : commandGroupContexts) {
            unregisterCommandGroup0(commandGroup);
        }

        commandGroupContexts.clear();
        commandManager.adjustBukkitCommandIds();
    }

    /**
     * 处理命令命令映射元素
     *
     * @param currentTree         当前命令树
     * @param commandGroupContext 命令持有者
     * @param commandMapping      命令映射注解
     * @return 解析过的命令映射元素
     */
    private CommandTree solveCommandMappingElement(CommandTree currentTree, CommandGroupContext commandGroupContext, CommandMapping commandMapping) {
        for (String id : commandMapping.value().split("/")) {
            CommandMappingElement mappingElement = new CommandMappingElement(commandGroupContext, id, commandMapping);

            currentTree = currentTree.getOrAddChild(new CommandTree(mappingElement));
        }

        return currentTree;
    }

    /**
     * 注册命令组
     *
     * @param commandGroup 命令组
     */
    public CommandGroupContext registerCommandGroup(@NonNull CommandGroup commandGroup) {
        // 先验证合法性
        CommandGroupVerifier.verify(commandGroup);

        CommandGroupContext commandGroupContext = new CommandGroupContext(plugin, this, commandGroup);
        CommandTree currentTree = commandManager.getRootCommandTree();

        // 处理全局层面的映射
        if (pluginScopeCommandMapping != null) {
            currentTree = solveCommandMappingElement(currentTree, commandGroupContext, pluginScopeCommandMapping);
        }

        Class<?> commandGroupClass = commandGroup.getClass();
        CommandMapping commandMappingAnnotation = commandGroupClass.getDeclaredAnnotation(CommandMapping.class);

        // 处理 CommandGroup 层面的映射
        if (commandMappingAnnotation != null) {
            currentTree = solveCommandMappingElement(currentTree, commandGroupContext, commandMappingAnnotation);
        }

        // 处理命令体
        for (Method method : commandGroupClass.getDeclaredMethods()) {
            CommandBody commandBodyAnnotation = method.getDeclaredAnnotation(CommandBody.class);

            if (commandBodyAnnotation != null) {
                CommandBodyElement element = new CommandBodyElement(commandGroupContext, method, commandBodyAnnotation);

                currentTree.addChild(new CommandTree(element));
            }
        }

        commandGroupContexts.add(commandGroupContext);
        commandManager.adjustBukkitCommandIds();
        logger.debug("command registered: " + commandGroup);
        logger.debug("current command tree: ");
        Arrays.stream(commandManager.getRootCommandTree().getTreeAsString().split("\n")).forEach(logger::debug);
        return commandGroupContext;
    }

    /**
     * 获取命令执行失败处理器
     */
    public CommandFailureHandler getCommandExecutionFailureHandler() {
        return commandFailureHandler;
    }

    /**
     * 获取命令帮助器
     */
    public CommandHelper getCommandHelper() {
        return commandHelper;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
