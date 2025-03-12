package Command;

import Model.Task;
import Repository.Task.Json.JsonTaskRepository;

import java.util.List;

public class ListTaskCommand extends AbstractCommand {

    private static final String COMMAND = "list";

    private final JsonTaskRepository repository;

    private final String statusFilter;

    public ListTaskCommand(JsonTaskRepository repository, String statusFilter) {
        super(COMMAND);
        this.repository = repository;
        this.statusFilter = statusFilter;
    }

    @Override
    public void execute() {
        List<Task> tasks = repository.findByStatus(statusFilter);
        System.out.println(tasks);
    }
}
