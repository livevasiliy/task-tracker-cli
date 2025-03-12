package Command;

import Model.Task;
import Repository.Task.Json.JsonTaskRepository;

public class DeleteTaskCommand extends AbstractCommand {

    private static final String COMMAND = "delete";

    private final JsonTaskRepository repository;

    private final int taskId;

    public DeleteTaskCommand(JsonTaskRepository repository, int taskId) {
        super(COMMAND);
        this.repository = repository;
        this.taskId = taskId;
    }

    @Override
    public void execute() {
        try {
            Task task = null;
            task = repository.getById(this.taskId);
            this.repository.delete(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
