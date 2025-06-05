package com.example.confectioneryapp;

import android.os.Bundle; // Для збереження стану Activity
import androidx.appcompat.app.AppCompatActivity; // Базовий клас для Activity з підтримкою App Bar
import androidx.lifecycle.ViewModelProvider; // Допомагає отримати ViewModel
import androidx.recyclerview.widget.LinearLayoutManager; // Розташовує елементи списку один за одним
import androidx.recyclerview.widget.RecyclerView; // Сам список для відображення даних
// import com.google.android.material.floatingactionbutton.FloatingActionButton; // Цей імпорт більше не потрібен
import com.google.android.material.button.MaterialButton; // ДОДАЄМО ЦЕЙ ІМПОРТ ДЛЯ НОВОЇ КНОПКИ
import android.widget.Toast; // Для показу повідомлень користувачу

import java.util.ArrayList; // Для створення порожнього списку на початку

/**
 * Головний екран нашого додатку "Кондитерська".
 * Тут відображається список десертів, є кнопка для додавання нового,
 * а також можна редагувати та видаляти існуючі десерти.
 */
public class MainActivity extends AppCompatActivity {

    // Наша ViewModel, яка керує даними про десерти.
    private DessertViewModel dessertViewModel;
    // Наш Адаптер, який "знає", як відобразити кожен десерт у списку.
    private DessertAdapter dessertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Встановлюємо XML-макет для цього екрану (activity_main.xml).
        setContentView(R.layout.activity_main);

        // Знаходимо наш RecyclerView в макеті
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // Кажемо йому, як розташовувати елементи (вертикально, один за одним)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // Оптимізація: якщо розмір елементів не змінюється

        // Створюємо наш адаптер для десертів, поки що з порожнім списком
        dessertAdapter = new DessertAdapter(new ArrayList<>());
        // "Прив'язуємо" адаптер до RecyclerView
        recyclerView.setAdapter(dessertAdapter);

        // Отримуємо екземпляр нашої DessertViewModel.
        dessertViewModel = new ViewModelProvider(this).get(DessertViewModel.class);

        // Тепер "підписуємося" на зміни у списку всіх десертів, який є у ViewModel.
        // Коли дані в базі зміняться, ViewModel оновить LiveData, а цей код автоматично викличеться.
        dessertViewModel.getAllDesserts().observe(this, dessertEntities -> {
            // Отримали новий список dessertEntities, передаємо його в адаптер.
            dessertAdapter.setDessertList(dessertEntities);
        });

        // Знаходимо нашу кнопку "Додати десерт" (MaterialButton)
        MaterialButton buttonAddDessert = findViewById(R.id.buttonAddDessert);
        // Встановлюємо обробник натискання на цю кнопку
        buttonAddDessert.setOnClickListener(view -> {
            // Коли кнопку натиснуто, показуємо наш діалог додавання десерту
            AddDessertDialog.show(this, newDessert -> {
                // Цей код (callback) виконається, коли користувач введе дані в діалозі і натисне "Додати".
                dessertViewModel.insert(newDessert); // Кажемо ViewModel додати цей десерт в базу
            }, null); // Передаємо null, бо це створення нового десерту, а не редагування існуючого.
        });

        // Встановлюємо обробник короткого кліку на елемент списку (для редагування)
        dessertAdapter.setOnDessertClickListener(dessert -> {
            // dessert - це той десерт, на який клікнули.
            // Зберігаємо його ID, бо сам об'єкт dessert може змінитися, поки діалог відкритий.
            final int dessertId = dessert.getId();

            // Показуємо діалог, передаючи в нього поточний десерт для редагування.
            AddDessertDialog.show(this, updatedDessert -> {
                // Цей код (callback) виконається, коли користувач змінить дані в діалозі і натисне "Оновити".
                updatedDessert.setId(dessertId); // Важливо! Встановлюємо ID для оновлення правильного запису в базі.
                dessertViewModel.update(updatedDessert); // Кажемо ViewModel оновити цей десерт.
            }, dessert); // Передаємо об'єкт десерту, який хочемо редагувати.
        });

        // Встановлюємо обробник довгого кліку на елемент списку (для видалення)
        dessertAdapter.setOnDessertLongClickListener(dessert -> {
            // dessert - десерт, на якому зробили довгий клік.
            // Показуємо стандартний діалог підтвердження перед видаленням.
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Видалити десерт?") // Заголовок діалогу
                    // Повідомлення, що показує назву десерту, який збираємося видалити
                    .setMessage("Ви впевнені, що хочете видалити '" + dessert.getName() + "'?")
                    .setPositiveButton("Видалити", (dialog, which) -> {
                        // Якщо користувач натиснув "Видалити"
                        dessertViewModel.delete(dessert); // Кажемо ViewModel видалити цей десерт
                        // ДОДАНО: Повідомлення про успішне видалення
                        Toast.makeText(MainActivity.this, "'" + dessert.getName() + "' видалено успішно!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Скасувати", null) // Кнопка "Скасувати" просто закриває діалог
                    .show(); // Показуємо діалог
        });
    }
}