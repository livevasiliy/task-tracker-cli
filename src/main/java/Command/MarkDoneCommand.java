package Command;

import Model.Task;
import Enum.TaskStatus;
import Repository.Task.Json.JsonTaskRepository;

public class MarkDoneCommand extends AbstractCommand {
    private static final String COMMAND = "mark-done";

    private final JsonTaskRepository repository;

    private final int taskId;

    public MarkDoneCommand(JsonTaskRepository repository, int taskId) {
        super(COMMAND);
        this.repository = repository;
        this.taskId = taskId;
    }

    @Override
    public void execute() {
        try {
            Task task = repository.getById(taskId);
            task.setStatus(TaskStatus.DONE);
            repository.update(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
