package Command;

import Model.Task;
import Repository.Task.Json.JsonTaskRepository;

public class UpdateTaskCommand extends AbstractCommand {

    private static final String COMMAND = "update";

    private final JsonTaskRepository repository;

    private final int taskId;
    private final String newDescription;

    public UpdateTaskCommand(JsonTaskRepository repository, int taskId, String newDescription) {
        super(COMMAND);
        this.repository = repository;
        this.taskId = taskId;
        this.newDescription = newDescription;
    }

    @Override
    public void execute() {
        try {
            Task task = repository.getById(taskId);
            task.setDescription(newDescription);
            repository.update(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
