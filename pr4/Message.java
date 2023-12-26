package pr4;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// Определение класса Message
public class Message {

    // Объявление публичного неизменяемого поля message, представляющего сообщение
    public final String message;

    // Аннотация JsonCreator указывает Jackson использовать этот конструктор при создании объекта из JSON
    @JsonCreator
    // Публичный конструктор класса Message с аннотацией JsonProperty, чтобы получить значение поля из JSON
    public Message(@JsonProperty("message") String message) {
        // Присваивание значению message входного параметра конструктора полю message класса Message
        this.message = message;
    }
}