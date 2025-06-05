package com.example.confectioneryapp;

import androidx.lifecycle.LiveData; // Потрібно для LiveData, щоб дані оновлювалися автоматично в UI
import androidx.room.Dao;          // Говорить Room, що це Data Access Object (об'єкт для доступу до даних)
import androidx.room.Delete;       // Анотація для методу видалення запису
import androidx.room.Insert;       // Анотація для методу вставки нового запису
import androidx.room.Query;        // Анотація для написання власних SQL-запитів
import androidx.room.Update;       // Анотація для методу оновлення запису

import java.util.List;

/**
 * Інтерфейс для роботи з десертами в базі даних.
 * Room сам створить необхідний код для реалізації цих методів.
 * Ми просто описуємо, які дії хочемо виконувати.
 */
@Dao
public interface DessertDao {

    /**
     * Дістає всі-всі десерти з таблиці "desserts".
     * Вони будуть відсортовані за номером (id) так, щоб новіші були першими.
     * LiveData дозволяє екрану автоматично оновлювати список, якщо щось змінилося в базі.
     */
    @Query("SELECT * FROM desserts ORDER BY id DESC")
    LiveData<List<DessertEntity>> getAllDesserts();

    /**
     * Додає новий десерт в базу.
     * @param dessert - це той десерт, який ми хочемо зберегти.
     */
    @Insert
    void insertDessert(DessertEntity dessert);

    /**
     * Оновлює інформацію про вже існуючий десерт.
     * @param dessert - десерт з новими даними, який замінить старий в базі.
     */
    @Update
    void updateDessert(DessertEntity dessert); // Змінили назву методу та параметра

    /**
     * Видаляє десерт з бази.
     * @param dessert - той десерт, який треба видалити.
     */
    @Delete
    void deleteDessert(DessertEntity dessert);

    /**
     * Знаходить і повертає один конкретний десерт за його унікальним номером (id).
     * @param dessertId - номер десерту, який шукаємо.
     * @return Знайдений десерт або null, якщо такого немає.
     */
    // Назва таблиці "desserts" і поле "id"
    @Query("SELECT * FROM desserts WHERE id = :dessertId LIMIT 1")
    DessertEntity getDessertById(int dessertId);
}