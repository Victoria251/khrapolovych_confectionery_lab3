package com.example.confectioneryapp; // Тут лежить наш клас, це як адреса для нього

import androidx.room.Entity;     // Ця штука каже, що клас буде табличкою в базі даних
import androidx.room.PrimaryKey;  // А це – що поле буде унікальним ключем, як номер паспорта
import androidx.annotation.NonNull; // Це означає "не можна залишати порожнім" (null)

/**
 * Це наш "Десерт". Описує, яку інформацію про кожен десертик ми зберігаємо.
 * У базі даних це буде окрема таблиця під назвою "desserts".
 */
@Entity(tableName = "desserts")
public class DessertEntity {

    /**
     * Номер десерту в базі. Кожен десерт матиме свій унікальний номер,
     * і база даних сама його призначатиме по порядку.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * Назва нашого десерту, наприклад, "Торт Наполеон" або "Тістечко Картопля".
     * Обов'язково має бути вказана.
     */
    @NonNull
    private String name;

    /**
     * Тип десерту: чи це "торт", чи "тістечко", чи "печиво".
     * Теж обов'язково вказуємо.
     */
    @NonNull
    private String type;

    /**
     * Скільки коштує наш десертик. Може бути з копійками, тому double.
     */
    private double price;

    /**
     * Вага десерту, зазвичай в грамах.
     */
    private int weight;

    /**
     * Тут можна написати щось цікаве про десерт: з чого зроблений,
     * який на смак, чи є якісь алергени. Це поле не обов'язкове.
     */
    private String description;

    /**
     * Показує, чи є десерт зараз, чи його треба замовляти.
     * За замовчуванням, коли додаємо новий, вважаємо, що він "В наявності".
     */
    @NonNull
    private String status = "В наявності";

    // Далі йдуть методи, щоб дізнатися або змінити інформацію про десерт.
    // Це як кнопки на пульті: одна показує, інша змінює.

    public int getId() {
        return id; // Дізнатися номер десерту
    }

    public void setId(int id) {
        this.id = id; // Встановити номер (зазвичай це робить база даних)
    }

    @NonNull
    public String getName() {
        return name; // Дізнатися назву
    }

    public void setName(@NonNull String name) {
        this.name = name; // Змінити назву
    }

    @NonNull
    public String getType() {
        return type; // Дізнатися тип
    }

    public void setType(@NonNull String type) {
        this.type = type; // Змінити тип
    }

    public double getPrice() {
        return price; // Дізнатися ціну
    }

    /**
     * Встановлюємо ціну. Перевіряємо, щоб вона не була меншою за нуль,
     * бо безкоштовних десертів (або ще й з доплатою нам) не буває :)
     */
    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Ціна повинна бути більшою за нуль");
        }
        // Якщо дійшли сюди, значить, ціна > 0, і можна її присвоїти.
        this.price = price;
    }

    public int getWeight() {
        return weight; // Дізнатися вагу
    }

    /**
     * Встановлюємо вагу. Десерт має хоч щось важити.
     */
    public void setWeight(int weight) {
        if (weight > 0) { // Вага має бути більшою за 0
            this.weight = weight;
        } else {
            throw new IllegalArgumentException("Вага повинна бути більшою за нуль!");
        }
    }

    public String getDescription() {
        return description; // Дізнатися опис
    }

    public void setDescription(String description) {
        this.description = description; // Змінити опис
    }

    @NonNull
    public String getStatus() {
        return status; // Дізнатися статус
    }

    /**
     * Встановлюємо статус. Є лише кілька дозволених варіантів.
     */
    public void setStatus(@NonNull String status) {
        if (status.equals("В наявності") || status.equals("Під замовлення") || status.equals("Немає в наявності")) {
            this.status = status;
        } else {
            // Якщо вказати якийсь незрозумілий статус, програма повідомить про помилку.
            throw new IllegalArgumentException("Такого статусу немає! Можна: 'В наявності', 'Під замовлення', 'Немає в наявності'.");
        }
    }
}