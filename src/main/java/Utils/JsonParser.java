package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    public static List<Map<String, Object>> parseJsonArray(String jsonString) {
        List<Map<String, Object>> result = new ArrayList<>();
        jsonString = jsonString.trim();

        if (!jsonString.startsWith("[") || !jsonString.endsWith("]")) {
            throw new IllegalArgumentException("Invalid JSON array format");
        }

        // Убираем квадратные скобки
        jsonString = jsonString.substring(1, jsonString.length() - 1).trim();

        // Разделяем объекты внутри массива
        String[] jsonObjects = jsonString.split("\\},\\s*\\{");

        for (String jsonObject : jsonObjects) {
            // Добавляем обратно фигурные скобки, если они были удалены при split
            if (!jsonObject.startsWith("{")) {
                jsonObject = "{" + jsonObject;
            }
            if (!jsonObject.endsWith("}")) {
                jsonObject = jsonObject + "}";
            }

            // Парсим каждый объект
            Map<String, Object> task = parseJsonObject(jsonObject);
            result.add(task);
        }

        return result;
    }

    private static Map<String, Object> parseJsonObject(String jsonObject) {
        Map<String, Object> result = new HashMap<>();
        jsonObject = jsonObject.trim();

        if (!jsonObject.startsWith("{") || !jsonObject.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON object format");
        }

        // Убираем фигурные скобки
        jsonObject = jsonObject.substring(1, jsonObject.length() - 1).trim();

        // Разделяем пары ключ-значение
        String[] keyValuePairs = jsonObject.split(",\\s*");

        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":\\s*", 2);
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Invalid key-value pair: " + pair);
            }

            String key = keyValue[0].replaceAll("\"", "").trim();
            String value = keyValue[1].trim();

            // Убираем кавычки для строковых значений
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            // Преобразуем числовые значения
            if (value.matches("-?\\d+")) {
                result.put(key, Long.parseLong(value));
            } else {
                result.put(key, value);
            }
        }

        return result;
    }
}
