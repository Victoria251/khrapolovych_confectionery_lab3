package com.example.confectioneryapp; // Переконайтесь, що назва пакету правильна

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast; // Для показу повідомлень користувачу (наприклад, про помилки)

import com.google.android.material.textfield.TextInputEditText;

/**
 * Цей клас відповідає за показ діалогового вікна,
 * де користувач може додати новий десерт або відредагувати існуючий.
 */
public class AddDessertDialog {

    /**
     * Інтерфейс (як контракт), щоб повідомити MainActivity (або хто викликав діалог),
     * коли десерт успішно додано або оновлено.
     */
    public interface AddDessertCallback {
        void onDessertAdded(DessertEntity dessert); // Метод тепер приймає DessertEntity
    }

    /**
     * Головний метод, який створює і показує діалог.
     * @param context Контекст, звідки викликається діалог (зазвичай це MainActivity).
     * @param callback Об'єкт, який реалізує AddDessertCallback, для отримання результату.
     * @param dessertToEdit Десерт, який ми редагуємо. Якщо це null, значить ми додаємо новий десерт.
     */
    public static void show(Context context, AddDessertCallback callback, DessertEntity dessertToEdit) {
        // Беремо наш XML-макет dialog_add_dessert.xml і "надуваємо" його (створюємо View)
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_dessert, null);

        // Знаходимо всі наші поля вводу та Spinner в макеті dialog_add_dessert.xml
        TextInputEditText editName = dialogView.findViewById(R.id.editDessertName);
        TextInputEditText editType = dialogView.findViewById(R.id.editDessertType);
        TextInputEditText editPrice = dialogView.findViewById(R.id.editDessertPrice);
        TextInputEditText editWeight = dialogView.findViewById(R.id.editDessertWeight);
        TextInputEditText editDescription = dialogView.findViewById(R.id.editDessertDescription);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);

        // Готуємо список статусів для нашого випадаючого списку (Spinner)
        String[] statuses = {"В наявності", "Під замовлення", "Немає в наявності"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Визначаємо, чи це режим редагування чи додавання
        boolean isEditing = (dessertToEdit != null);
        String dialogTitle = isEditing ? "Редагувати десерт" : "Додати новий десерт";
        String positiveButtonText = isEditing ? "Оновити" : "Додати";

        // Якщо ми редагуємо існуючий десерт, заповнюємо поля
        if (isEditing) {
            editName.setText(dessertToEdit.getName());
            editType.setText(dessertToEdit.getType());
            editPrice.setText(String.valueOf(dessertToEdit.getPrice()));
            editWeight.setText(String.valueOf(dessertToEdit.getWeight()));
            editDescription.setText(dessertToEdit.getDescription());

            int statusPosition = statusAdapter.getPosition(dessertToEdit.getStatus());
            if (statusPosition >= 0) {
                spinnerStatus.setSelection(statusPosition);
            }
        }

        // Тепер створюємо сам діалог
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setView(dialogView)
                .setPositiveButton(positiveButtonText, null) // Обробник поки null
                .setNegativeButton("Скасувати", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        // Цей код виконається, коли діалог вже готовий показатися.
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> {
                String nameStr = editName.getText().toString().trim();
                String typeStr = editType.getText().toString().trim();
                String priceStr = editPrice.getText().toString().trim();
                String weightStr = editWeight.getText().toString().trim();
                String descriptionStr = editDescription.getText().toString().trim();
                String selectedStatus = spinnerStatus.getSelectedItem().toString();

                if (nameStr.isEmpty() || typeStr.isEmpty() || priceStr.isEmpty() || weightStr.isEmpty()) {
                    Toast.makeText(context, "Назва, тип, ціна та вага є обов'язковими!", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    double price = Double.parseDouble(priceStr);
                    int weight = Integer.parseInt(weightStr);

                    DessertEntity dessertToSave;
                    if (isEditing) {
                        dessertToSave = dessertToEdit;
                    } else {
                        dessertToSave = new DessertEntity();
                    }

                    dessertToSave.setName(nameStr);
                    dessertToSave.setType(typeStr);
                    dessertToSave.setPrice(price);
                    dessertToSave.setWeight(weight);
                    dessertToSave.setDescription(descriptionStr);
                    dessertToSave.setStatus(selectedStatus);

                    callback.onDessertAdded(dessertToSave);
                    dialog.dismiss();

                    // ДОДАНО: Повідомлення про успішне оновлення/додавання
                    if (isEditing) {
                        Toast.makeText(context, "Десерт оновлено успішно!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Десерт додано успішно!", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Будь ласка, введіть правильні числа для ціни та ваги.", Toast.LENGTH_LONG).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(context, "Помилка введення: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }
}