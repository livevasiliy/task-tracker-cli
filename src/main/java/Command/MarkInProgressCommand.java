package Command;

import Model.Task;
import Enum.TaskStatus;
import Repository.Task.Json.JsonTaskRepository;

public class MarkInProgressCommand extends AbstractCommand {

    private static final String COMMAND = "mark-in-progress";

    private final JsonTaskRepository repository;

    private final int taskId;

    public MarkInProgressCommand(JsonTaskRepository repository, int taskId) {
        super(COMMAND);
        this.repository = repository;
        this.taskId = taskId;
    }

    @Override
    public void execute() {
        try {
            Task task = repository.getById(taskId);
            task.setStatus(TaskStatus.IN_PROGRESS);
            repository.update(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
