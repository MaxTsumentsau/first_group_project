package util;

import collection.MyArrayList;
import model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class StudentJsonFileManager {
    private final Path filePath;

    public StudentJsonFileManager(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    // Добавление студента в JSON файл
    public void appendStudent(Student student) throws IOException {
        String json = studentToJson(student) + "," + System.lineSeparator();

        if (!Files.exists(filePath)) {
            // Если файл не существует, начинаем с открывающей скобки
            json = "[" + System.lineSeparator() + json;
        } else {
            // Если файл существует, читаем его и корректируем
            String content = Files.readString(filePath);
            if (content.trim().endsWith("]")) {
                // Убираем закрывающую скобку и добавляем запятую
                content = content.substring(0, content.length() - 1).trim();
                if (!content.endsWith("[")) {
                    content += ",";
                }
                content += System.lineSeparator() + json;
                Files.write(filePath, content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                return;
            }
        }

        Files.write(filePath, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    // Завершение JSON файла (добавление закрывающей скобки)
    public void completeJsonFile() throws IOException {
        if (Files.exists(filePath)) {
            String content = Files.readString(filePath);
            if (!content.trim().endsWith("]")) {
                content = content.trim();
                if (content.endsWith(",")) {
                    content = content.substring(0, content.length() - 1);
                }
                content += System.lineSeparator() + "]";
                Files.write(filePath, content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    // Чтение студентов из JSON файла
    public MyArrayList<Student> readAllStudents() throws IOException {
        MyArrayList<Student> students = new MyArrayList<>();

        if (!Files.exists(filePath)) {
            return students;
        }

        String content = Files.readString(filePath);
        String[] lines = content.split(System.lineSeparator());

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("{") && line.endsWith("},") || line.endsWith("}")) {
                // Убираем запятую в конце если есть
                if (line.endsWith(",")) {
                    line = line.substring(0, line.length() - 1);
                }
                Student student = parseJsonStudent(line);
                if (student != null) {
                    students.add(student);
                }
            }
        }

        return students;
    }

    private String studentToJson(Student student) {
        return String.format("  {\"group\": \"%s\", \"grade\": %.3f, \"recordBook\": \"%s\"}",
                student.getGroupNumber(),
                student.getAverageGrade(),
                student.getRecordBookNumber());
    }

    private Student parseJsonStudent(String json) {
        try {
            // Простой парсинг JSON
            json = json.replace("{", "").replace("}", "").replace("\"", "").trim();
            String[] pairs = json.split(",");

            String group = "";
            double grade = 0;
            String recordBook = "";

            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    switch (key) {
                        case "group":
                            group = value;
                            break;
                        case "grade":
                            grade = Double.parseDouble(value);
                            break;
                        case "recordBook":
                            recordBook = value;
                            break;
                    }
                }
            }
            return StudentValidator.validateAndCreateStudent(group, grade, recordBook);
        } catch (Exception e) {
            System.err.println("Ошибка парсинга JSON: " + json);
            return null;
        }
    }
}