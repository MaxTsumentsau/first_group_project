package util;

import model.Student;

public class StudentValidator {

    public static boolean isValidGroupNumber(String groupNumber) {
        return groupNumber != null && groupNumber.matches("[A-Za-z0-9]{2,10}");
    }

    public static boolean isValidAverageGrade(double grade) {
        return grade >= 0 && grade <= 10;
    }

    public static boolean isValidRecordBookNumber(String recordBookNumber) {
        return recordBookNumber != null && recordBookNumber.matches("[А-Яа-я0-9]{5,15}");
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
}
