package service;

import collection.MyArrayList;
import model.Student;
import util.StudentValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Scanner;

public class StudentGenerator {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    // Конфигурируемые параметры
    private String[] availableGroups = {"CS101", "CS102", "MATH201", "PHY301", "CHEM401", "BIO501"};
    private double minGrade = 0.0;
    private double maxGrade = 10.0;
    private String recordBookPrefix = "RB";

    public StudentGenerator() {}

    public StudentGenerator(String[] availableGroups, double minGrade, double maxGrade, String recordBookPrefix) {
        this.availableGroups = availableGroups;
        this.minGrade = minGrade;
        this.maxGrade = maxGrade;
        this.recordBookPrefix = recordBookPrefix;
    }

    public MyArrayList<Student> generateStudents() {
        System.out.print("Введите количество студентов для генерации: ");
        int numberOfStudents = scanner.nextInt();
        scanner.nextLine();
        if (numberOfStudents <= 0) {
            throw new IllegalArgumentException("Количество студентов должно быть положительным числом");
        }

        MyArrayList<Student> students = new MyArrayList<>();

        for (int i = 0; i < numberOfStudents; i++) {
            Student student = createRandomStudent();
            students.add(student);
        }

        return students;
    }

    private Student createRandomStudent() {
        String groupNumber = availableGroups[random.nextInt(availableGroups.length)];
        double averageGrade = minGrade + (random.nextDouble() * (maxGrade - minGrade));
        BigDecimal bd = new BigDecimal(averageGrade);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        averageGrade = bd.doubleValue();
        String recordBookNumber = generateRecordBookNumber();

        return StudentValidator.validateAndCreateStudent(groupNumber, averageGrade, recordBookNumber);
    }

    private String generateRecordBookNumber() {
        int number = 10000 + random.nextInt(90000);
        return recordBookPrefix + number;
    }
}
