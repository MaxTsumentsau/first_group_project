package util;

import collection.MyArrayList;
import model.Student;

// Утилитный класс
public final class StudentValidator {
    private StudentValidator() {
    }
    public static boolean isValidGroupNumber(String groupNumber) {
        return groupNumber != null && groupNumber.matches("[A-Za-z0-9]{2,10}");
    }

    public static boolean isValidAverageGrade(double grade) {
        return grade >= 0 && grade <= 10;
    }

    public static boolean isValidRecordBookNumber(String recordBookNumber) {
        return recordBookNumber != null && recordBookNumber.matches("[A-Za-z0-9]{5,15}");
    }

    public static Student validateAndCreateStudent(String groupNumber, double averageGrade, String recordBookNumber) {
        if (!isValidGroupNumber(groupNumber)) {
            throw new IllegalArgumentException("Неверный номер группы: " + groupNumber);
        }
        if (!isValidAverageGrade(averageGrade)) {
            throw new IllegalArgumentException("Неверный средний рейтинг: " + averageGrade);
        }
        if (!isValidRecordBookNumber(recordBookNumber)) {
            throw new IllegalArgumentException("Номер зачетной книжки: " + recordBookNumber);
        }

        return new Student.Builder()
                .groupNumber(groupNumber)
                .averageGrade(averageGrade)
                .recordBookNumber(recordBookNumber)
                .build();
    }

    private static MyArrayList<Student> validateStudents(MyArrayList<Student> studentsToValidate) {
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
}
