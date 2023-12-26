package pr4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MessageMapper {

    // Создание экземпляра ObjectMapper для преобразования объектов Java в JSON и обратно
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    // Метод для преобразования объекта Message в JSON-строку
    public static String messageToJson(Message msg) {
        try {
            // Использование метода writeValueAsString() ObjectMapper для преобразования объекта в JSON-строку
            return jsonMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            // Генерация исключения в случае ошибки
            throw new IllegalStateException(e);
        }
    }

    // Метод для преобразования JSON-строки в объект Message
    public static Message jsonToMessage(String json) {
        try {
            // Использование метода readValue() ObjectMapper для преобразования JSON-строки в объект Message
            return jsonMapper.readValue(json, Message.class);
        } catch (IOException e) {
            // Генерация исключения в случае ошибки
            throw new IllegalStateException(e);
        }
    }
}