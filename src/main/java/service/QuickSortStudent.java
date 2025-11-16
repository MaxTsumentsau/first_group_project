package service;

import collection.MyArrayList;
import model.Student;

import java.util.*;
import java.util.function.Function;

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
                ? createTrickyComparator()
                : baseComparator;

        // Запускаем рекурсивную quick sort сортировку
        if(comparator == null){
            quickSort(studentList, 0, studentList.size() - 1, finalComparator);
        }
        else {
            trickySort(studentList, Student::getAverageGrade);
        }
        return studentList;
    }

    private static Comparator<Student> createBaseComparator() {
        return Comparator
                .comparing(Student::getGroupNumber)                    // по группе ↑
                .thenComparing(Student::getAverageGrade, Comparator.reverseOrder()) // по баллу ↓
                .thenComparing(Student::getRecordBookNumber);          // по номеру зачетки ↑
    }

    public static Comparator<Student> createTrickyComparator() {
        return Comparator.comparingDouble(Student::getAverageGrade);
    }

    public static MyArrayList<Student> trickySort(List<Student> students,
                                                  Function<Student, Double> fieldExtractor) {

        // Шаг 1: Создаем мапы для сохранения исходных позиций
        Map<Integer, Student> evenStudents = new HashMap<>();
        Map<Integer, Student> oddStudents = new HashMap<>();

        // Разделяем на четные и нечетные
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            double value = fieldExtractor.apply(student);

            if (value % 2 == 0) {
                evenStudents.put(i, student);
            } else {
                oddStudents.put(i, student);
            }
        }

        // Шаг 2: Преобразуем четных студентов в список для сортировки
        List<Student> evenStudentsList = new ArrayList<>(evenStudents.values());
        //List<Integer> evenPositions = new ArrayList<>(evenStudents.keySet());

        // Шаг 3: Сортируем четных студентов с помощью quickSort
        if (!evenStudentsList.isEmpty()) {
            Comparator<Student> comparator = Comparator.comparingDouble(fieldExtractor::apply);
            quickSort(evenStudentsList, 0, evenStudentsList.size() - 1, comparator);
        }

        // Шаг 4: Восстанавливаем порядок
        MyArrayList<Student> result = new MyArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            result.add(null); // Заполняем null'ами
        }

        // Распределяем нечетных по исходным позициям
        for (Map.Entry<Integer, Student> entry : oddStudents.entrySet()) {
            result.set(entry.getKey(), entry.getValue());
        }

        // Распределяем отсортированных четных на оставшиеся позиции
        int evenIndex = 0;
        for (int i = 0; i < students.size(); i++) {
            if (result.get(i) == null) {
                result.set(i, evenStudentsList.get(evenIndex++));
            }
        }

        return result;
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