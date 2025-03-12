package Command;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    public static List<String> parseArguments(String input) {
        List<String> arguments = new ArrayList<>();
        StringBuilder currentArgument = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '"') {
                // Переключаем флаг inQuotes при встрече кавычки
                inQuotes = !inQuotes;
            } else if (ch == ' ' && !inQuotes) {
                // Если пробел вне кавычек, добавляем текущий аргумент
                if (!currentArgument.isEmpty()) {
                    arguments.add(currentArgument.toString());
                    currentArgument.setLength(0); // Очищаем StringBuilder
                }
            } else {
                // Добавляем символ к текущему аргументу
                currentArgument.append(ch);
            }
        }

        // Добавляем последний аргумент, если он есть
        if (!currentArgument.isEmpty()) {
            arguments.add(currentArgument.toString());
        }

        return arguments;
    }
}