package com.example.confectioneryapp; // Пакет залишається тим самим

import android.app.Application; // Потрібен для ViewModel, щоб мати доступ до ресурсів, якщо знадобиться

import androidx.annotation.NonNull; // Помітка, що параметр або змінна не можуть бути null
import androidx.lifecycle.AndroidViewModel; // Базовий клас для ViewModel, яка "знає" про контекст додатка
import androidx.lifecycle.LiveData;     // Клас для даних, за якими можна "спостерігати" (наприклад, з UI)

import java.util.List; // Для використання списків
import java.util.concurrent.ExecutorService; // Штука для виконання задач в окремому потоці
import java.util.concurrent.Executors;   // Допомагає створювати ExecutorService

/**
 * Ця ViewModel керує даними про десерти для нашого UI (екранів).
 * Вона бере дані з бази та готує їх для показу.
 * Також вона обробляє дії користувача, пов'язані з даними (додати, оновити, видалити).
 */
public class DessertViewModel extends AndroidViewModel {

    // Наш інструмент (DAO) для роботи з таблицею десертів у базі даних.
    // final означає, що після присвоєння значення цю змінну не можна буде змінити.
    private final DessertDao dessertDao;

    // Список усіх десертів, який "живий". Тобто, якщо дані в базі зміняться,
    // цей список автоматично оновить те, що бачить користувач на екрані.
    private final LiveData<List<DessertEntity>> allDesserts;

    // Створюємо окремий потік для роботи з базою даних.
    // Це важливо, щоб не "гальмувати" основний потік, де малюється інтерфейс.
    // newSingleThreadExecutor() означає, що всі операції з базою будуть виконуватися по черзі в одному потоці.
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Це конструктор. Він викликається, коли система створює нашу ViewModel.
     * @param application Посилання на наш додаток, щоб отримати доступ до бази даних.
     */
    public DessertViewModel(@NonNull Application application) {
        super(application); // Викликаємо конструктор батьківського класу
        // Отримуємо "зв'язок" з нашою базою даних
        AppDatabase db = AppDatabase.getInstance(application);
        // Через базу даних отримуємо наш інструмент (DAO) для роботи з десертами
        dessertDao = db.dessertDao(); // Тут ми використовуємо метод з AppDatabase, який ми оновили
        // Запитуємо у DAO список усіх десертів. Цей список буде "живим".
        allDesserts = dessertDao.getAllDesserts(); // А тут - метод з DessertDao, який ми теж оновили
    }

    /**
     * Цей метод дозволяє іншим частинам програми (наприклад, нашому Activity)
     * отримати "живий" список усіх десертів.
     * @return LiveData зі списком DessertEntity.
     */
    public LiveData<List<DessertEntity>> getAllDesserts() { // Назва методу тепер відповідає десертам
        return allDesserts;
    }

    /**
     * Додає новий десерт до бази даних.
     * Робить це в окремому потоці, щоб не заблокувати UI.
     * @param dessert - об'єкт десерту, який потрібно зберегти.
     */
    public void insert(DessertEntity dessert) { // Параметр тепер теж DessertEntity
        executorService.execute(() -> dessertDao.insertDessert(dessert)); // Викликаємо відповідний метод з DessertDao
    }

    /**
     * Оновлює інформацію про існуючий десерт в базі даних.
     * Робить це в окремому потоці.
     * @param dessert - об'єкт десерту з новими даними.
     */
    public void update(DessertEntity dessert) { // Параметр тепер теж DessertEntity
        executorService.execute(() -> dessertDao.updateDessert(dessert)); // Викликаємо відповідний метод з DessertDao
    }

    /**
     * Видаляє десерт з бази даних.
     * Робить це в окремому потоці.
     * @param dessert - об'єкт десерту, який потрібно видалити.
     */
    public void delete(DessertEntity dessert) { // Параметр тепер теж DessertEntity
        executorService.execute(() -> dessertDao.deleteDessert(dessert)); // Викликаємо відповідний метод з DessertDao
    }

    /**
     * Цей метод викликається, коли ViewModel більше не потрібна і буде знищена.
     * Важливо "закрити" наш ExecutorService, щоб уникнути витоків ресурсів.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // "Вимикаємо" наш окремий потік
    }
}