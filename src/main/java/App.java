import Command.AbstractCommand;
import Repository.Task.Json.JsonTaskRepository;
import Repository.Task.Json.JsonTaskRepositoryImpl;
import Resolver.CommandResolver;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            // Из-за отсутствия в проекте Dependency Injection мы вручную подставляем нужную нам реализацию
            JsonTaskRepository jsonTaskRepository = new JsonTaskRepositoryImpl();
            CommandResolver commandResolver = new CommandResolver(jsonTaskRepository);

            System.out.println("Welcome to the Task Tracker CLI");
            System.out.println("Type 'exit' to quit.");
            while (true) {
                // Запрос команды у пользователя
                System.out.print("Enter command: ");
                String input = scanner.nextLine().trim();

                // Проверка на выход
                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Exiting...");
                    break;
                }

                // Разбиение ввода на команду и аргументы
                String[] parts = input.split("\\s+");
                if (parts.length == 0) {
                    System.out.println("Please enter a valid command.");
                    continue;
                }

                String commandName = parts[0];
                String[] commandArgs = new String[parts.length - 1];
                System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);

                // Разрешение и выполнение команды
                try {
                    AbstractCommand command = commandResolver.resolve(commandName, commandArgs);
                    command.execute();
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred", exception);
        } finally {
            System.out.println("Goodbye!");
        }
    }
}