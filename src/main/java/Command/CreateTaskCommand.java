package Command;

import Model.Task;
import Repository.Task.Json.JsonTaskRepository;

public class CreateTaskCommand extends AbstractCommand {
    private static final String COMMAND = "add";

    private final JsonTaskRepository repository;

    private final String description;

    public CreateTaskCommand(
            JsonTaskRepository repository,
            String description
    ) {
        super(COMMAND);
        this.repository = repository;
        this.description = description;
    }

    @Override
    public void execute() {
        Task object = new Task();
        object.setDescription(description);
        Task task = this.repository.save(object);
        System.out.println(task.getId());
    }
}
