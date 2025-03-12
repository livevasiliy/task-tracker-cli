package Repository.Task.Json;

import Exceptions.TaskNotFoundException;
import Model.Task;
import Enum.TaskStatus;
import Utils.JsonParser;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public final class JsonTaskRepositoryImpl implements JsonTaskRepository {
    private final String fileName = "tasks.json";
    private final List<Task> tasks = new ArrayList<>();
    private Long nextId = 1L;

    public JsonTaskRepositoryImpl() {
        initFile();
        readFile();
    }

    @Override
    public Task save(Task task) {
        task.setId(nextId++);
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(Date.from(Instant.now()));
        tasks.add(task);
        saveTasksToFile();
        return task;
    }

    @Override
    public void delete(Task task) {
        boolean removed = tasks.remove(task);
        if (removed) {
            saveTasksToFile();
        }
    }

    @Override
    public void update(Task task) throws TaskNotFoundException {
        for (Task t : tasks) {
            if (Objects.equals(t.getId(), task.getId())) {
                t.setDescription(task.getDescription());
                t.setStatus(task.getStatus());
                t.setUpdatedAt(Date.from(Instant.now()));
                saveTasksToFile();
                return;
            }
        }
        throw new TaskNotFoundException();
    }

    @Override
    public List<Task> getAll() {
        return tasks;
    }

    @Override
    public Task getById(int id) throws TaskNotFoundException {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public List<Task> findByStatus(String status) {
        if (status == null) {
            return getAll();
        }
        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
        return tasks.stream()
                .filter(t -> t.getStatus() == taskStatus)
                .collect(Collectors.toList());
    }

    private void initFile() {
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("Error while creating file: " + e.getMessage());
        }
    }

    private void readFile() {
        try {
            // Чтение JSON файла в строку
            StringBuilder jsonContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
            }

            // Парсинг JSON строки вручную
            List<Map<String, Object>> parsedTasks = JsonParser.parseJsonArray(jsonContent.toString());

            // Преобразование Map в объекты Task
            for (Map<String, Object> taskMap : parsedTasks) {
                Task task = new Task();
                task.setId((long) taskMap.get("id"));
                task.setDescription((String) taskMap.get("description"));
                task.setStatus(TaskStatus.valueOf((String) taskMap.get("status")));
                task.setCreatedAt(new Date(Long.parseLong(taskMap.get("createdAt").toString())));
                task.setUpdatedAt(taskMap.containsKey("updatedAt") ? new Date(Long.parseLong((String) taskMap.get("updatedAt"))) : null);
                tasks.add(task);
            }

            // Обновление nextId
            if (!tasks.isEmpty()) {
                nextId = tasks.stream().mapToLong(Task::getId).max().getAsLong() + 1;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        }
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("[\n");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                writer.write(String.format(
                        "  {\"id\": %d, \"description\": \"%s\", \"status\": \"%s\", \"createdAt\": %d, \"updatedAt\": %s}",
                        task.getId(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getCreatedAt().getTime(),
                        task.getUpdatedAt() != null ? task.getUpdatedAt().getTime() : null
                ));
                if (i < tasks.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}