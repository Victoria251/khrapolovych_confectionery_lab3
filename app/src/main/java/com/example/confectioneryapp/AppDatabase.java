package com.example.confectioneryapp;

import android.content.Context;

import androidx.room.Database;   // Головна "мітка" для класу, що це база даних
import androidx.room.Room;        // Інструмент для побудови нашої бази даних
import androidx.room.RoomDatabase; // Базовий клас, від якого ми будуємо свою базу

/**
 * Це "серце" нашої бази даних.
 * Тут ми кажемо, які таблички (сутності) у нас будуть і яка версія бази.
 * Він один на весь додаток (Singleton).
 */
// @Database - головна анотація.
// entities - тут перераховуємо всі наші класи-таблички. У нас одна - DessertEntity.
// version - номер версії. Дуже важливо його змінювати, якщо ми міняємо структуру таблиць.
// exportSchema = false - не експортувати схему бази в окремий файл (для простоти).
@Database(entities = {DessertEntity.class}, version = 2, exportSchema = false) // Змінили версію на 2!
public abstract class AppDatabase extends RoomDatabase {

    // Тут буде жити єдиний екземпляр нашої бази даних.
    // volatile - щоб зміни були одразу видні всім потокам.
    private static volatile AppDatabase INSTANCE;

    /**
     * Цей метод дасть нам інструмент (DAO) для роботи з табличкою десертів.
     * Room сам напише код для цього методу, нам лише треба його оголосити.
     * @return об'єкт DessertDao, через який ми будемо робити запити до таблиці "desserts".
     */
    public abstract DessertDao dessertDao();

    /**
     * Головний спосіб отримати доступ до нашої бази даних.
     * Він створює базу, якщо її ще немає, або повертає вже існуючу.
     * @param context Потрібен, щоб знати, де створити файл бази даних.
     * @return Єдиний екземпляр AppDatabase.
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) { // Якщо базу ще ніхто не просив
            synchronized (AppDatabase.class) { // Захист, щоб кілька частин програми одночасно не спробували створити базу
                if (INSTANCE == null) { // Ще раз перевіряємо, може, хтось вже створив, поки ми чекали
                    // Створюємо базу!
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), // Беремо загальний контекст додатка
                                    AppDatabase.class, // Наш клас бази даних
                                    "confectionery.db") // Назва файлу, де буде зберігатися база. Тепер для кондитерської!
                            // Якщо ми оновили версію бази, а Room не знає, як перенести старі дані в нову структуру,
                            // він просто видалить стару базу і створить нову.
                            .fallbackToDestructiveMigration()
                            .build(); // Будуємо!
                }
            }
        }
        return INSTANCE; // Повертаємо готову базу
    }
}