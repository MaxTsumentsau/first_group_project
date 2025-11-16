package model;

import java.util.Objects;

public class Student {
    private String groupNumber;
    private double averageGrade;
    private String recordBookNumber;

    private Student(Builder builder) {
        this.groupNumber = builder.groupNumber;
        this.averageGrade = builder.averageGrade;
        this.recordBookNumber = builder.recordBookNumber;
    }

    //TODO:delete
    public Student(String groupNumber, double averageGrade, String recordBookNumber) {
        this.groupNumber = groupNumber;
        this.averageGrade = averageGrade;
        this.recordBookNumber = recordBookNumber;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public String getRecordBookNumber() {
        return recordBookNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Double.compare(averageGrade, student.averageGrade) == 0 && Objects.equals(groupNumber, student.groupNumber) && Objects.equals(recordBookNumber, student.recordBookNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupNumber, averageGrade, recordBookNumber);
    }

    @Override
    public String toString() {
        return "Student{" +
                "groupNumber='" + groupNumber + '\'' +
                ", averageGrade=" + averageGrade +
                ", recordBookNumber='" + recordBookNumber + '\'' +
                '}';
    }

    public static class Builder {
        private String groupNumber;
        private double averageGrade;
        private String recordBookNumber;

        public Builder groupNumber(String groupNumber) {
            this.groupNumber = groupNumber;
            return this;
        }

        public Builder averageGrade(double averageGrade) {
            this.averageGrade = averageGrade;
            return this;
        }

        public Builder recordBookNumber(String recordBookNumber) {
            this.recordBookNumber = recordBookNumber;
            return this;
        }

        public Student build() {
            if (groupNumber == null || recordBookNumber == null) {
                throw new IllegalStateException("Номер группы и номер зачетки обязательны!");
            }
            if (averageGrade < 0 || averageGrade > 10) {
                throw new IllegalStateException("Средний балл должен быть между 0 и 10!");
            }
            return new Student(this);
        }
    }
}
