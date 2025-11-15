package util;

import model.Student;

import java.util.*;

public class QuickSortStudent {
    public static List<Student> quickSort(Collection<Student> students, Comparator<Student> comparator) {
        if (students == null) {
            throw new IllegalArgumentException("Коллекция не может быть null");
        }

        // Преобразуем коллекцию в список для сортировки
        List<Student> studentList = new ArrayList<>(students);

        // БАЗОВАЯ сортировка по всем 3 полям
        Comparator<Student> baseComparator = createBaseComparator();

        // Если передан внешний компаратор - комбинируем с базовым
        Comparator<Student> finalComparator = (comparator != null)
                ? baseComparator.thenComparing(comparator)
                //TODO:Сдох=(
                : baseComparator;

        // Запускаем рекурсивную сортировку
        quickSort(studentList, 0, studentList.size() - 1, finalComparator);

        return studentList;
    }

    private static Comparator<Student> createBaseComparator() {
        return Comparator
                .comparing(Student::getGroupNumber)                    // по группе ↑
                .thenComparing(Student::getAverageGrade, Comparator.reverseOrder()) // по баллу ↓
                .thenComparing(Student::getRecordBookNumber);          // по номеру зачетки ↑
    }

    private static void quickSort(List<Student> list, int low, int high, Comparator<Student> comparator) {
        if (low < high) {
            // Находим опорный элемент и разделяем список
            int pivotIndex = partition(list, low, high, comparator);

            // Рекурсивно сортируем левую и правую части
            quickSort(list, low, pivotIndex - 1, comparator);
            quickSort(list, pivotIndex + 1, high, comparator);
        }
    }

    private static int partition(List<Student> list, int low, int high, Comparator<Student> comparator) {
        // Выбираем опорный элемент (последний в диапазоне)
        Student pivot = list.get(high);
        int i = low - 1; // индекс меньшего элемента

        for (int j = low; j < high; j++) {
            // Если текущий элемент меньше или равен опорному
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                // Меняем местами
                Collections.swap(list, i, j);
            }
        }

        // Помещаем опорный элемент на правильную позицию
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}