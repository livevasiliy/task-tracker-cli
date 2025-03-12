import Command.AbstractCommand;
import Command.ArgumentParser;
import Repository.Task.Json.JsonTaskRepository;
import Repository.Task.Json.JsonTaskRepositoryImpl;
import Resolver.CommandResolver;

import java.util.List;
import java.util.Scanner;

public class App {
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

                // Парсим аргументы
                List<String> parsedArgs = ArgumentParser.parseArguments(input);
                if (parsedArgs.isEmpty()) {
                    System.out.println("Please enter a valid command.");
                    continue;
                }

                String commandName = parsedArgs.getFirst();
                String[] commandArgs = parsedArgs.subList(1, parsedArgs.size()).toArray(new String[0]);

                // Разрешение и выполнение команды
                try {
                    AbstractCommand command = commandResolver.resolve(commandName, commandArgs);
                    command.execute();
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception exception) {
            System.out.println("An unexpected error occurred: " + exception.getMessage());
        } finally {
            System.out.println("Goodbye!");
        }
    }
}