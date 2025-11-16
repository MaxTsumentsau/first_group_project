package service;

import collection.MyArrayList;
import model.Student;
import util.StudentValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static service.QuickSortStudent.createTrickyComparator;

public class StudentSortingApp {
    private MyArrayList<Student> students;
    private final Scanner scanner;
    private StudentJsonFileManager fileManager;

    // Стратегии заполнения данных
    private interface DataLoadingStrategy {
        MyArrayList<Student> loadData() throws Exception;
    }

    public StudentSortingApp() {
        this.students = new MyArrayList<>();
        this.scanner = new Scanner(System.in);
        this.fileManager = new StudentJsonFileManager("students.json");
    }

    public void run() {
        System.out.println("=== ПРИЛОЖЕНИЕ ДЛЯ СОРТИРОВКИ СТУДЕНТОВ ===");

        while (true) {
            showMainMenu();
            int choice = getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1 -> loadData();
                    case 2 -> showStudents();
                    case 3 -> sortStudents();
                    case 4 -> saveToFile();
                    case 5 -> loadFromFile();
                    case 6 -> clearData();
                    case 0 -> {
                        System.out.println("Выход из программы...");
                        return;
                    }
                    default -> System.out.println("Неверный выбор!");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            System.out.println("\n" + "=".repeat(50));
        }
    }

    private void showMainMenu() {
        System.out.println("\nГЛАВНОЕ МЕНЮ:");
        System.out.println("1. Загрузить данные");
        System.out.println("2. Показать студентов");
        System.out.println("3. Отсортировать студентов");
        System.out.println("4. Сохранить в файл");
        System.out.println("5. Загрузить из файла");
        System.out.println("6. Очистить данные");
        System.out.println("0. Выход");
    }

    private void loadData() throws Exception {
        System.out.println("\n--- ЗАГРУЗКА ДАННЫХ ---");
        System.out.println("1. Случайная генерация");
        System.out.println("2. Ручной ввод");
        System.out.println("3. Назад");

        int choice = getIntInput("Выберите способ: ");

        DataLoadingStrategy strategy = switch (choice) {
            case 1 -> this::loadRandomData;
            case 2 -> this::loadManualData;
            case 3 -> null;
            default -> throw new IllegalArgumentException("Неверный выбор");
        };

        if (strategy != null) {
            MyArrayList<Student> newStudents = strategy.loadData();
            this.students = newStudents;
            System.out.println("Данные успешно загружены! Загружено студентов: " + newStudents.size());
        }
    }

    private MyArrayList<Student> loadRandomData() {
        StudentGenerator generator = new StudentGenerator();
        MyArrayList<Student> generated = generator.generateStudents();

        // Валидация сгенерированных данных
        return validateStudents(generated);
    }

    private MyArrayList<Student> loadManualData() {
        ManualStudentInput manualInput = new ManualStudentInput();
        MyArrayList<Student> manualStudents = manualInput.createStudentsManually();

        // Валидация введенных данных
        return validateStudents(manualStudents);
    }

    private MyArrayList<Student> validateStudents(MyArrayList<Student> studentsToValidate) {
        MyArrayList<Student> validatedStudents = new MyArrayList<>();

        for (Student student : studentsToValidate) {
            try {
                // Используем валидатор для создания нового валидированного студента
                Student validatedStudent = StudentValidator.validateAndCreateStudent(
                        student.getGroupNumber(),
                        student.getAverageGrade(),
                        student.getRecordBookNumber()
                );
                validatedStudents.add(validatedStudent);
            } catch (IllegalArgumentException e) {
                System.out.println("Студент не прошел валидацию: " + e.getMessage());
            }
        }

        return validatedStudents;
    }

    private void showStudents() {
        if (students.isEmpty()) {
            System.out.println("Список студентов пуст!");
            return;
        }

        System.out.println("\n--- СПИСОК СТУДЕНТОВ (" + students.size() + ") ---");
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.printf("%d. %s\n", i + 1, student);
        }
    }

    private void sortStudents() {
        if (students.isEmpty()) {
            System.out.println("Нет данных для сортировки!");
            return;
        }

        System.out.println("\n--- СОРТИРОВКА СТУДЕНТОВ ---");
        System.out.println("1. Базовая сортировка");
        System.out.println("2. Хитроделанная сортировка(Пока не реализована!)");
        System.out.println("3. Назад");

        int choice = getIntInput("Выберите тип сортировки: ");

        Comparator<Student> comparator = switch (choice) {
            case 1 -> null; // Базовая сортировка
            case 2 -> QuickSortStudent.createTrickyComparator();
            case 3 -> null;
            default -> throw new IllegalArgumentException("Неверный выбор");
        };

        //TODO:доделать как положено, выдохся((
        if (comparator != null || choice == 1) {
            List<Student> sortedList = QuickSortStudent.quickSort(students, comparator);
            // Преобразуем обратно в MyArrayList
            students = new MyArrayList<>();
            students.addAll(sortedList);
            System.out.println("Сортировка завершена!");
        }
    }

    private void saveToFile() {
        if (students.isEmpty()) {
            System.out.println("Нет данных для сохранения!");
            return;
        }

        try {
            // Очищаем файл и записываем заново
            fileManager.completeJsonFile(); // Завершаем предыдущий JSON если нужно

            for (Student student : students) {
                fileManager.appendStudent(student);
            }
            fileManager.completeJsonFile(); // Завершаем JSON

            System.out.println("Данные успешно сохранены в файл!");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try {
            MyArrayList<Student> fileStudents = fileManager.readAllStudents();

            if (fileStudents.isEmpty()) {
                System.out.println("Файл пуст или не существует!");
                return;
            }

            // Валидация данных из файла
            MyArrayList<Student> validatedStudents = validateStudents(fileStudents);

            if (validatedStudents.isEmpty()) {
                System.out.println("Нет валидных данных в файле!");
                return;
            }

            this.students = validatedStudents;
            System.out.println("Данные успешно загружены из файла! Загружено студентов: " + validatedStudents.size());

        } catch (Exception e) {
            System.out.println("Ошибка при загрузке из файла: " + e.getMessage());
        }
    }

    private void clearData() {
        students.clear();
        System.out.println("Данные очищены!");
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число!");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
