package Resolver;

import Command.*;
import Repository.Task.Json.JsonTaskRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class CommandResolver {
    private final Map<String, Function<String[], AbstractCommand>> commandMap = new HashMap<>();

    public CommandResolver(JsonTaskRepository taskRepository) {
        commandMap.put("add", args -> new CreateTaskCommand(taskRepository, args[0]));
        commandMap.put("update", args -> new UpdateTaskCommand(taskRepository, Integer.parseInt(args[0]), args[1]));
        commandMap.put("delete", args -> new DeleteTaskCommand(taskRepository, Integer.parseInt(args[0])));
        commandMap.put("mark-in-progress", args -> new MarkInProgressCommand(taskRepository, Integer.parseInt(args[0])));
        commandMap.put("mark-done", args -> new MarkDoneCommand(taskRepository, Integer.parseInt(args[0])));
        commandMap.put("list", args -> new ListTaskCommand(taskRepository, args.length > 0 ? args[0] : null));
    }

    public AbstractCommand resolve(String command, String[] args) {
        Function<String[], AbstractCommand> resolver = commandMap.get(command);
        if (resolver == null) {
            throw new IllegalArgumentException("Unknown command: " + command);
        }

        return resolver.apply(args);
    }
}
