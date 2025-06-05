package com.example.confectioneryapp;

import android.view.LayoutInflater; // Допомагає "надути" (створити) вигляд з XML-файлу
import android.view.View;          // Базовий клас для всіх елементів UI
import android.view.ViewGroup;     // Контейнер для інших View
import android.widget.TextView;    // Для відображення тексту

import androidx.annotation.NonNull; // Позначка, що щось не може бути null
import androidx.recyclerview.widget.RecyclerView; // Для створення списків

import java.util.List;       // Для роботи зі списками
import java.util.Locale;     // Для форматування тексту, наприклад, ціни відповідно до мови

/**
 * Це Адаптер. Він як міст між нашими даними (списком десертів)
 * та тим, як вони відображаються на екрані у вигляді списку (RecyclerView).
 * Він відповідає за створення кожного елемента списку та заповнення його даними.
 */
public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.DessertViewHolder> {

    // Тут зберігатиметься наш список десертів, який ми хочемо показати
    private List<DessertEntity> dessertList;

    // Це "слухачі" натискань на елементи списку.
    // MainActivity підпишеться на них, щоб знати, коли користувач щось натиснув.

    // Для звичайного (короткого) кліку
    public interface OnDessertClickListener {
        void onDessertClick(DessertEntity dessert); // Передаємо натиснутий десерт
    }
    private OnDessertClickListener clickListener;

    // Для довгого кліку (зазвичай використовується для видалення)
    public interface OnDessertLongClickListener {
        void onDessertLongClick(DessertEntity dessert); // Передаємо натиснутий десерт
    }
    private OnDessertLongClickListener longClickListener;

    /**
     * Конструктор Адаптера. Сюди ми передаємо початковий список десертів.
     * @param dessertList Список десертів для відображення.
     */
    public DessertAdapter(List<DessertEntity> dessertList) {
        this.dessertList = dessertList;
    }

    /**
     * Цей метод викликається, коли RecyclerView потрібно створити новий "контейнер"
     * (ViewHolder) для одного елемента списку.
     * Ми тут вказуємо, який XML-макет використовувати для одного рядка.
     */
    @NonNull
    @Override
    public DessertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Беремо наш XML-файл item_dessert.xml і створюємо з нього View-об'єкт
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dessert, parent, false); // Використовуємо наш новий макет item_dessert.xml
        return new DessertViewHolder(itemView); // Повертаємо наш новий DessertViewHolder
    }

    /**
     * Цей метод заповнює вже створений ViewHolder даними з конкретного десерту.
     * @param holder "Контейнер" (DessertViewHolder), який треба заповнити.
     * @param position Номер (індекс) десерту в нашому списку dessertList.
     */
    @Override
    public void onBindViewHolder(@NonNull DessertViewHolder holder, int position) {
        // Беремо конкретний десерт зі списку за його номером
        DessertEntity currentDessert = dessertList.get(position);

        // Тепер заповнюємо текстові поля в нашому ViewHolder'і даними з десерту
        holder.dessertNameText.setText(currentDessert.getName()); // Встановлюємо назву

        // Формуємо рядок з типом та ціною для другого текстового поля
        String info = String.format(Locale.getDefault(), "%s - %.2f грн",
                currentDessert.getType(), currentDessert.getPrice());
        holder.dessertInfoText.setText(info); // Встановлюємо тип і ціну

        // Налаштовуємо реакцію на короткий клік по цьому елементу списку
        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onDessertClick(currentDessert); // Повідомляємо "слухача" про клік
            }
        });

        // Налаштовуємо реакцію на довгий клік
        holder.itemView.setOnLongClickListener(view -> {
            if (longClickListener != null) {
                longClickListener.onDessertLongClick(currentDessert); // Повідомляємо "слухача"
                return true; // Кажемо системі, що ми обробили цей довгий клік
            }
            return false; // Якщо слухача немає, кажемо, що не обробили
        });
    }

    /**
     * Повертає кількість десертів у нашому списку.
     * RecyclerView використовує це, щоб знати, скільки елементів малювати.
     */
    @Override
    public int getItemCount() {
        // Якщо список не порожній, повертаємо його розмір, інакше 0
        return dessertList != null ? dessertList.size() : 0;
    }

    /**
     * Цей метод дозволяє оновити список десертів в адаптері.
     * Наприклад, коли ми додали новий десерт або видалили старий.
     * @param newDessertList Новий список десертів.
     */
    public void setDessertList(List<DessertEntity> newDessertList) { // Змінили назву методу і параметра
        this.dessertList = newDessertList;
        notifyDataSetChanged(); // Дуже важлива команда! Повідомляє RecyclerView, що дані змінилися і треба все перемалювати.
    }

    /**
     * ViewHolder – це такий собі "тримач" для елементів вигляду (View) одного рядка списку.
     * Замість того, щоб кожного разу шукати TextView за ID, ми робимо це один раз
     * і зберігаємо посилання тут. Це робить прокрутку списку швидшою.
     */
    public static class DessertViewHolder extends RecyclerView.ViewHolder {
        TextView dessertNameText;  // Тут буде назва десерту
        TextView dessertInfoText;  // Тут буде тип і ціна

        public DessertViewHolder(@NonNull View itemView) {
            super(itemView); // Викликаємо конструктор батька
            // Знаходимо наші текстові поля в макеті item_dessert.xml за їх ID
            dessertNameText = itemView.findViewById(R.id.textDessertName);
            dessertInfoText = itemView.findViewById(R.id.textDessertInfo);
        }
    }

    // Методи, щоб MainActivity могла "підписатися" на події кліків

    public void setOnDessertClickListener(OnDessertClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnDessertLongClickListener(OnDessertLongClickListener listener) {
        this.longClickListener = listener;
    }
}