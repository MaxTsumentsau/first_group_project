package util;

import collection.MyArrayList;
import model.Student;

import java.util.Scanner;

public class ManualStudentInput {
    private final Scanner scanner;

    public ManualStudentInput() {
        this.scanner = new Scanner(System.in);
    }

    public MyArrayList<Student> createStudentsManually() {
        System.out.print("Введите количество студентов: ");
        int numberOfStudents = scanner.nextInt();
        scanner.nextLine(); // очистка буфера

        if (numberOfStudents <= 0) {
            throw new IllegalArgumentException("Количество студентов должно быть положительным числом");
        }

        MyArrayList<Student> students = new MyArrayList<>();

        for (int i = 0; i < numberOfStudents; i++) {
            System.out.println("\n=== Студент " + (i + 1) + " ===");
            Student student = inputStudentData();
            students.add(student);
            System.out.println("Студент добавлен!");
        }

        return students;
    }

    private Student inputStudentData() {
        String groupNumber = inputGroupNumber();
        double averageGrade = inputAverageGrade();
        String recordBookNumber = inputRecordBookNumber();

        return new Student(groupNumber, averageGrade, recordBookNumber);
    }

    private String inputGroupNumber() {
        System.out.print("Введите номер группы: ");
        return scanner.nextLine().trim();
    }

    private double inputAverageGrade() {
        while (true) {
            try {
                System.out.print("Введите средний балл (от 0.0 до 10.0): ");
                String input = scanner.nextLine().trim();
                double grade = Double.parseDouble(input.replace(',', '.'));

                if (grade >= 0.0 && grade <= 10.0) {
                    // Округление до 3 знаков после запятой
                    return Math.round(grade * 1000.0) / 1000.0;
                } else {
                    System.out.println("Ошибка: средний балл должен быть от 0.0 до 10.0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число (например: 4.25)");
            }
        }
    }

    private String inputRecordBookNumber() {
        System.out.print("Введите номер зачетной книжки: ");
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }
}
