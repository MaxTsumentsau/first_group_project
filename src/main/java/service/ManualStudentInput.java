package service;

import collection.MyArrayList;
import model.Student;
import util.StudentValidator;

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
        while (true) {
            try {
                String groupNumber = inputGroupNumber();
                double averageGrade = inputAverageGrade();
                String recordBookNumber = inputRecordBookNumber();

                return StudentValidator.validateAndCreateStudent(groupNumber, averageGrade, recordBookNumber);

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте снова ввести данные студента.\n");
            }
        }
    }

    private String inputGroupNumber() {
        System.out.print("Введите номер группы: ");
        return scanner.nextLine().trim();
    }

    private double inputAverageGrade() {
        System.out.print("Введите средний балл (от 0.0 до 10.0): ");
        String input = scanner.nextLine().trim();
        double grade = Double.parseDouble(input.replace(',', '.'));
        return Math.round(grade * 1000.0) / 1000.0;
    }

    private String inputRecordBookNumber() {
        System.out.print("Введите номер зачетной книжки: ");
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }
}
