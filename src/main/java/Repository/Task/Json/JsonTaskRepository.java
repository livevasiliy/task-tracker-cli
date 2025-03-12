package Repository.Task.Json;

import Model.Task;
import Repository.CrudRepository;

import java.util.List;

public interface JsonTaskRepository extends CrudRepository<Task> {
    public List<Task> findByStatus(String status);
}
